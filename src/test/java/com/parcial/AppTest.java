package com.parcial;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void shouldReturnMessage() {
        App app = new App();
        assertEquals("Hola desde Maven", app.getMessage());
    }
}
