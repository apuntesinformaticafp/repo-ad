package ejemplos.bd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

class InfraestructuraTest {
    @TempDir Path carpeta;

    @Test void estructuraPersisteEntreConexiones() throws Exception {
        String url = "jdbc:sqlite:" + carpeta.resolve("prueba.sqlite");
        try (var c = Esquema.abrir(url)) { Esquema.crear(c); }
        try (var c = Esquema.abrir(url)) { Esquema.comprobar(c); }
    }

    @Test void crearDosVecesNoBorraDatos() throws Exception {
        try (var c = Esquema.abrir("jdbc:sqlite::memory:"); var s = c.createStatement()) {
            Esquema.crear(c);
            s.executeUpdate("INSERT INTO empresa VALUES ('E01', 'Empresa A')");
            Esquema.crear(c);
            assertThrows(SQLException.class, () -> s.executeUpdate("INSERT INTO empresa VALUES ('E01', 'Otra')"));
        }
    }

    @Test void restriccionRechazaTutorHuerfano() throws Exception {
        try (var c = Esquema.abrir("jdbc:sqlite::memory:"); var s = c.createStatement()) {
            Esquema.crear(c);
            assertThrows(SQLException.class, () -> s.executeUpdate("INSERT INTO tutor VALUES ('T01', 'Tutor A', 'E99')"));
        }
    }

    @Test void esquemaIncompletoSeDetecta() throws Exception {
        try (var c = Esquema.abrir("jdbc:sqlite::memory:"); var s = c.createStatement()) {
            Esquema.crear(c);
            s.executeUpdate("DROP TABLE tutor");
            assertThrows(SQLException.class, () -> Esquema.comprobar(c));
        }
    }
}
