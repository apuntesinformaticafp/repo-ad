package fct.bd;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/** Perfil explícito: sin servicio/configuración falla, nunca se omite silenciosamente. */
@Tag("contrato")
@Tag("ud02")
@Tag("postgresql")
class ContratoPostgresTest {
    private HashMap<String, String> variables() {
        var variables = new HashMap<>(System.getenv());
        variables.put("FCT_ENTORNO", "pruebas");
        variables.put("FCT_DB_SCHEMA", "fct_ud02_" + UUID.randomUUID().toString().replace("-", ""));
        return variables;
    }
    @Test void esquemaYRestriccionesEnPostgreSQL() throws Exception {
        var config = ConfiguracionBD.desde(variables());
        assertFalse(config.sqlite(), "El perfil postgresql debe probar PostgreSQL real");
        try {
            CasosEsquema.verificar(config);
        } finally {
            // Solo el namespace sintético único de esta prueba, incluso tras una aserción fallida.
            try (var c = DriverManager.getConnection(config.url(), config.propiedades()); var s = c.createStatement()) {
                s.executeUpdate("DROP SCHEMA IF EXISTS \"" + config.esquema() + "\" CASCADE");
            }
        }
    }
    @Test void credencialInvalidaNoDaUnaConexion() {
        var variables = variables();
        variables.put("FCT_DB_PASSWORD", "password-incorrecto-" + UUID.randomUUID());
        var config = ConfiguracionBD.desde(variables);
        assertFalse(config.sqlite());
        var error = assertThrows(SQLException.class, () -> new FabricaConexiones(config).abrir());
        assertNotNull(error.getSQLState());
        assertTrue(error.getSQLState().startsWith("28"), "Debe fallar autenticación, no disponibilidad de servicio");
    }
}
