package fct.bd;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de la base proporcionada, no aceptación de métodos pendientes. */
class ConfiguracionBDTest {
    @Test void exigeUrlSinPublicarSuValor() {
        assertThrows(IllegalArgumentException.class, () -> ConfiguracionBD.desde(Map.of()));
        var error = assertThrows(IllegalArgumentException.class, () -> ConfiguracionBD.desde(Map.of("FCT_JDBC_URL", "secreto")));
        assertFalse(error.getMessage().contains("secreto"));
    }
    @Test void sqliteNoExigePasswordYActivaReferencias() {
        var c = ConfiguracionBD.desde(Map.of("FCT_JDBC_URL", "jdbc:sqlite::memory:"));
        assertTrue(c.sqlite());
        assertEquals("true", c.propiedades().getProperty("foreign_keys"));
    }
    @Test void postgresExigeCredencialesExternas() {
        assertThrows(IllegalArgumentException.class, () -> ConfiguracionBD.desde(Map.of(
                "FCT_JDBC_URL", "jdbc:postgresql://localhost/fct")));
    }
    @Test void descripcionNoRevelaPassword() {
        var c = ConfiguracionBD.desde(Map.of("FCT_JDBC_URL", "jdbc:postgresql://localhost/fct",
                "FCT_DB_USER", "fct", "FCT_DB_PASSWORD", "secreto-de-prueba"));
        assertFalse(c.toString().contains("secreto-de-prueba"));
    }
    @Test void rechazaNamespaceFueraDelLaboratorio() {
        assertThrows(IllegalArgumentException.class, () -> ConfiguracionBD.desde(Map.of(
                "FCT_JDBC_URL", "jdbc:sqlite::memory:", "FCT_DB_SCHEMA", "public")));
    }
}
