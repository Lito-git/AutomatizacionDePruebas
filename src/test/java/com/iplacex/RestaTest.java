package com.iplacex;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Pruebas unitarias para resta.
 * al igual que en suma, cada test verifica un solo comportamiento (atomicidad).
 */
public class RestaTest {

    private Calculadora calculadora;

    // Instancia nueva por test, para asegurar aislamiento y ejecución en cualquier orden
    @Before
    public void setUp() {
        calculadora = new Calculadora();
    }

    @Test
    public void restarDosPositivosRetornaSuDiferencia() {
        assertEquals(3, calculadora.restar(5, 2));
    }

    @Test
    public void restarMayorDesdeMenorRetornaNegativo() {
        assertEquals(-3, calculadora.restar(2, 5));
    }
}