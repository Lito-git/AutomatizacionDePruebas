package com.iplacex.login;

import java.util.HashMap;
import java.util.Map;

/**
 * Lógica de negocio del inicio de sesión.
 * Implementa los criterios de aceptación acordados en la sesión Three Amigos.
 */
public class ServicioAutenticacion {

    static final String MSG_CAMPOS_VACIOS = "Debe ingresar correo y contraseña";
    static final String MSG_CREDENCIALES_INVALIDAS = "Credenciales inválidas";

    // Usuarios registrados en memoria: correo -> datos del usuario
    private final Map<String, Usuario> usuarios = new HashMap<>();

    /** Registra un usuario para que pueda iniciar sesión. */
    public void registrarUsuario(String correo, String contrasena, String nombre) {
        usuarios.put(correo, new Usuario(contrasena, nombre));
    }

    /** Valida las credenciales y retorna el mensaje correspondiente. */
    public String iniciarSesion(String correo, String contrasena) {
        // CA4: campos obligatorios
        if (correo == null || correo.isBlank() || contrasena == null || contrasena.isBlank()) {
            return MSG_CAMPOS_VACIOS;
        }
        // CA2 y CA3: mismo mensaje para no revelar si el correo existe
        Usuario usuario = usuarios.get(correo);
        if (usuario == null || !usuario.contrasena().equals(contrasena)) {
            return MSG_CREDENCIALES_INVALIDAS;
        }
        // CA1: acceso concedido
        return "Bienvenido, " + usuario.nombre();
    }

    private record Usuario(String contrasena, String nombre) { }
}