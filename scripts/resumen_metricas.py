"""
Dashboard de métricas de calidad del pipeline.
Este lee los resultados generados por las pruebas (Surefire, Cucumber y k6) que existan
en target/ y escribe tablas en el resumen de la ejecución de GitHub Actions.
"""
import glob
import json
import os
import xml.etree.ElementTree as ET

# Mismos umbrales definidos en performance/login-carga.js
UMBRAL_P95_MS = 500
UMBRAL_ERRORES_PCT = 1
UMBRAL_TPS = 50


def estado(cumple):
    return "✅" if cumple else "❌"


def metricas_unitarias():
    """Suma los resultados de las pruebas unitarias desde los XML de Surefire."""
    archivos = glob.glob("target/surefire-reports/TEST-*.xml")
    if not archivos:
        return None
    total = {"tests": 0, "fallidas": 0, "omitidas": 0, "tiempo": 0.0}
    for archivo in archivos:
        suite = ET.parse(archivo).getroot()
        # El runner de Cucumber se excluye: los escenarios BDD se reportan aparte
        if suite.get("name", "").endswith("RunCucumberTest"):
            continue
        total["tests"] += int(suite.get("tests", 0))
        total["fallidas"] += int(suite.get("failures", 0)) + int(suite.get("errors", 0))
        total["omitidas"] += int(suite.get("skipped", 0))
        total["tiempo"] += float(suite.get("time", 0))
    return total


def metricas_bdd():
    """Cuenta los escenarios ejecutados y exitosos desde el JSON de Cucumber."""
    ruta = "target/cucumber-report.json"
    if not os.path.exists(ruta):
        return None
    with open(ruta, encoding="utf-8") as archivo:
        features = json.load(archivo)
    total = exitosos = 0
    for feature in features:
        for escenario in feature.get("elements", []):
            if escenario.get("type") != "scenario":
                continue
            total += 1
            pasos = escenario.get("steps", [])
            if all(paso["result"]["status"] == "passed" for paso in pasos):
                exitosos += 1
    return total, exitosos


def metricas_performance():
    """Obtiene TPS, latencia, errores y checks desde el resumen exportado por k6."""
    ruta = "target/k6-resumen.json"
    if not os.path.exists(ruta):
        return None
    with open(ruta, encoding="utf-8") as archivo:
        metricas = json.load(archivo)["metrics"]
    return {
        "tps": metricas["http_reqs"]["rate"],
        "solicitudes": metricas["http_reqs"]["count"],
        "p95": metricas["http_req_duration"]["p(95)"],
        "promedio": metricas["http_req_duration"]["avg"],
        "errores": metricas["http_req_failed"].get("value", 0) * 100,
        "checks": metricas["checks"].get("value", 0) * 100,
    }


def main():
    lineas = []

    unitarias = metricas_unitarias()
    bdd = metricas_bdd()
    if unitarias or bdd:
        lineas += ["## Dashboard de calidad: pruebas funcionales", "",
                   "| Métrica | Valor | Estado |", "|---|---|---|"]
        if unitarias:
            lineas.append(f"| Pruebas unitarias ejecutadas | {unitarias['tests']} | |")
            lineas.append(f"| Pruebas unitarias fallidas | {unitarias['fallidas']} | {estado(unitarias['fallidas'] == 0)} |")
            lineas.append(f"| Duración de pruebas unitarias | {unitarias['tiempo']:.2f} s | |")
        if bdd:
            total, exitosos = bdd
            tasa = (exitosos / total * 100) if total else 0
            lineas.append(f"| Escenarios BDD ejecutados | {total} | |")
            lineas.append(f"| Escenarios BDD exitosos | {exitosos} ({tasa:.0f} %) | {estado(exitosos == total)} |")
        lineas.append("")

    perf = metricas_performance()
    if perf:
        lineas += ["## Dashboard de calidad: performance del inicio de sesión", "",
                   "| Indicador | Valor | Umbral | Estado |", "|---|---|---|---|"]
        lineas.append(f"| TPS (solicitudes por segundo) | {perf['tps']:.1f} | > {UMBRAL_TPS} | {estado(perf['tps'] > UMBRAL_TPS)} |")
        lineas.append(f"| Latencia p95 | {perf['p95']:.1f} ms | < {UMBRAL_P95_MS} ms | {estado(perf['p95'] < UMBRAL_P95_MS)} |")
        lineas.append(f"| Latencia promedio | {perf['promedio']:.1f} ms | | |")
        lineas.append(f"| Tasa de errores | {perf['errores']:.2f} % | < {UMBRAL_ERRORES_PCT} % | {estado(perf['errores'] < UMBRAL_ERRORES_PCT)} |")
        lineas.append(f"| Checks exitosos | {perf['checks']:.1f} % | | |")
        lineas.append(f"| Total de solicitudes | {perf['solicitudes']} | | |")
        lineas.append("")

    texto = "\n".join(lineas) + "\n"
    destino = os.environ.get("GITHUB_STEP_SUMMARY")
    if destino:
        with open(destino, "a", encoding="utf-8") as archivo:
            archivo.write(texto)
    else:
        print(texto)


if __name__ == "__main__":
    main()