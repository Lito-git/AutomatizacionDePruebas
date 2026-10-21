package com.iplacex.steps;

import com.iplacex.login.ServicioAutenticacion;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;

/**
 * Step definitions del inicio de sesión.
 * Se mantienen delgados: solo traducen cada paso Gherkin y delegan la lógica en ServicioAutenticacion.
 * Cucumber crea una instancia nueva de esta clase por escenario, por lo que ningún escenario comparte estado con otro.
 */
public class LoginSteps {

    private final ServicioAutenticacion servicio = new ServicioAutenticacion();
    private String mensajeObtenido;

    @Given("que existe el usuario {string} con contraseña {string} y nombre {string}")
    public void queExisteElUsuario(String correo, String contrasena, String nombre) {
        servicio.registrarUsuario(correo, contrasena, nombre);
    }

    @When("ingreso el correo {string} y la contraseña {string}")
    public void ingresoCredenciales(String correo, String contrasena) {
        mensajeObtenido = servicio.iniciarSesion(correo, contrasena);
    }

    @Then("debería ver el mensaje {string}")
    public void deberiaVerElMensaje(String mensajeEsperado) {
        assertEquals(mensajeEsperado, mensajeObtenido);
    }
}