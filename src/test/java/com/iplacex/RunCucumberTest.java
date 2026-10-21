package com.iplacex;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Runner de Cucumber: Surefire lo ejecuta en la fase test junto a las pruebas unitarias.
 * Genera un reporte HTML navegable y un JSON con los resultados de los escenarios.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.iplacex.steps",
        plugin = {
                "pretty",
                "html:target/cucumber-report.html",
                "json:target/cucumber-report.json"
        }
)
public class RunCucumberTest {
}