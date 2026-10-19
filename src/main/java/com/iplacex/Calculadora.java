package com.iplacex;

/**
 * Lógica de negocio con operaciones de suma y resta.
 * Clase sin estado: cada método depende solo de sus parámetros,
 * para poder probarla de forma aislada y repetible.
 */
public class Calculadora {

    /** Retorna la suma de dos enteros. */
    public int sumar(int a, int b) {
        return a + b;
    }

    /** Retorna la resta de dos enteros (a - b). */
    public int restar(int a, int b) {
        return a - b;
    }
}