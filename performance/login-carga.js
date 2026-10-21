// Prueba de carga del inicio de sesión con k6.
// Simula usuarios concurrentes enviando credenciales válidas a POST /login.
import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    // Escenario de carga: sube a 20 usuarios virtuales, se mantiene y baja
    stages: [
        { duration: '10s', target: 20 },
        { duration: '30s', target: 20 },
        { duration: '10s', target: 0 },
    ],
    // Umbrales de calidad: si alguno no se cumple, k6 termina con error y el pipeline falla
    thresholds: {
        http_req_duration: ['p(95)<500'], // Latencia: el 95 % de las respuestas en menos de 500 ms
        http_req_failed: ['rate<0.01'],   // Errores: menos del 1 % de solicitudes fallidas
        http_reqs: ['rate>50'],           // TPS: al menos 50 solicitudes por segundo
    },
};

const URL = __ENV.BASE_URL || 'http://localhost:8080/login';

export default function () {
    // Envía el formulario de login con el usuario de prueba
    const respuesta = http.post(URL, { correo: 'ana@iplacex.cl', contrasena: 'Clave123' });

    // Valida que cada respuesta sea correcta, no solo rápida
    check(respuesta, {
        'estado 200': (r) => r.status === 200,
        'mensaje de bienvenida': (r) => r.body.includes('Bienvenido'),
    });

    sleep(0.1); // Pausa breve entre solicitudes de cada usuario virtual
}