package com.iplacex;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Pruebas unitarias para suma.
 * En este caso, cada test verifica un solo comportamiento (atomicidad).
 */
public class SumaTest {

    private Calculadora calculadora;

    // Se crea una instancia nueva antes de cada test y así, ningún test depende de otro
    @Before
    public void setUp() {
        calculadora = new Calculadora();
    }

    @Test
    public void sumarDosPositivosRetornaSuSuma() {
        assertEquals(5, calculadora.sumar(2, 3));
    }

    @Test
    public void sumarConNegativoRetornaResultadoCorrecto() {
        assertEquals(-1, calculadora.sumar(2, -3));
    }
}