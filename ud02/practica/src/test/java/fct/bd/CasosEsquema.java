package fct.bd;

import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de restricciones con filas sintéticas, no métodos CRUD del componente. */
final class CasosEsquema {
    static void verificar(ConfiguracionBD config) throws Exception {
        var fabrica = new FabricaConexiones(config);
        var esquema = new EsquemaJdbc(config);
        try (var c = fabrica.abrir(); var s = c.createStatement()) {
            assertTrue(c.isValid(2));
            esquema.crear(c);
            esquema.comprobar(c);
            s.executeUpdate("CREATE TABLE ajena (id INTEGER PRIMARY KEY)");
            s.executeUpdate("INSERT INTO ajena VALUES (99)");
            s.executeUpdate("INSERT INTO empresa VALUES ('E01', 'Empresa A'), ('E02', 'Empresa B')");
            s.executeUpdate("INSERT INTO estudiante VALUES ('A01', 'Estudiante A', 'DAM')");
            s.executeUpdate("INSERT INTO tutor VALUES ('T01', 'Tutor A', 'E01')");
            s.executeUpdate("INSERT INTO asignacion VALUES ('AS01', 'A01', 'E01', 'T01', '2026-10-01', '2026-12-01')");
            esquema.crear(c);
            assertThrows(SQLException.class, () -> s.executeUpdate("INSERT INTO empresa VALUES ('E01', 'Duplicada')"));
            assertThrows(SQLException.class, () -> s.executeUpdate("INSERT INTO empresa VALUES ('E03', NULL)"));
            assertThrows(SQLException.class, () -> s.executeUpdate("INSERT INTO empresa VALUES ('E03', '')"));
            assertThrows(SQLException.class, () -> s.executeUpdate("INSERT INTO tutor VALUES ('T99', 'Huérfano', 'E99')"));
            assertThrows(SQLException.class, () -> s.executeUpdate("INSERT INTO asignacion VALUES ('AS02', 'A99', 'E01', 'T01', '2026-10-01', '2026-12-01')"));
            assertThrows(SQLException.class, () -> s.executeUpdate("INSERT INTO asignacion VALUES ('AS02', 'A01', 'E02', 'T01', '2026-10-01', '2026-12-01')"));
            assertThrows(SQLException.class, () -> s.executeUpdate("INSERT INTO asignacion VALUES ('AS02', 'A01', 'E01', 'T01', '2026-12-01', '2026-10-01')"));
            assertThrows(SQLException.class, () -> s.executeUpdate("DELETE FROM empresa WHERE id='E01'"));
            assertThrows(IllegalArgumentException.class, () -> esquema.reiniciar(c, false));
            try (var fila = s.executeQuery("SELECT count(*) FROM asignacion")) {
                assertTrue(fila.next());
                assertEquals(1, fila.getInt(1));
            }
            esquema.reiniciar(c, true);
            try (var fila = s.executeQuery("SELECT count(*) FROM asignacion")) {
                assertTrue(fila.next());
                assertEquals(0, fila.getInt(1));
            }
            try (var fila = s.executeQuery("SELECT id FROM ajena")) {
                assertTrue(fila.next());
                assertEquals(99, fila.getInt(1));
            }
            s.executeUpdate("DROP TABLE asignacion");
            assertThrows(SQLException.class, () -> esquema.comprobar(c));
            esquema.crear(c);
            esquema.comprobar(c);
        }
        try (var reabierta = fabrica.abrir()) { esquema.comprobar(reabierta); }
    }
}
