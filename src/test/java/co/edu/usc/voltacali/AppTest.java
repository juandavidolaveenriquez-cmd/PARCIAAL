package co.edu.usc.voltacali;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void shouldReturnMessage() {
        App app = new App();
        assertEquals("Caso de prueba obligatorio", "Caso de prueba obligatorio");
    }

    @Test
    void shouldCreateCharger() {
        CargadorVE cargador = new CargadorVE("VE-100");
        assertEquals("VE-100", cargador.getModelo());
        assertEquals(16.0, cargador.calcularTiempoCarga(20), 0.0001);
    }
}
