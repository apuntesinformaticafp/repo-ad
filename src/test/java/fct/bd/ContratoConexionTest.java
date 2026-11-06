package fct.bd;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@Tag("contrato")
@Tag("ud02")
class ContratoConexionTest {
    @TempDir Path carpeta;
    private ConfiguracionBD config(String entorno) {
        return ConfiguracionBD.desde(Map.of("FCT_JDBC_URL", "jdbc:sqlite:" + carpeta.resolve("fct.sqlite"),
                "FCT_ENTORNO", entorno));
    }
    @Test void esquemaYRestriccionesEnSQLite() throws Exception {
        CasosEsquema.verificar(config("pruebas"));
    }
    @Test void cierreRealYReapertura() throws Exception {
        var fabrica = new FabricaConexiones(config("pruebas"));
        var conexion = fabrica.abrir();
        try (conexion) { assertTrue(conexion.isValid(2)); }
        assertTrue(conexion.isClosed());
        try (var otra = fabrica.abrir()) { assertTrue(otra.isValid(2)); }
    }
    @Test void noReiniciaDesarrolloAunqueSeConfirme() throws Exception {
        var config = config("desarrollo");
        var esquema = new EsquemaJdbc(config);
        try (var c = new FabricaConexiones(config).abrir()) {
            esquema.crear(c);
            assertThrows(IllegalArgumentException.class, () -> esquema.reiniciar(c, true));
            esquema.comprobar(c);
        }
    }
    @Test void columnasIncorrectasNoSeConfundenConEsquemaPreparado() throws Exception {
        var config = config("pruebas");
        try (var c = new FabricaConexiones(config).abrir(); var s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE empresa (id INTEGER PRIMARY KEY)");
            var esquema = new EsquemaJdbc(config);
            esquema.crear(c);
            assertThrows(SQLException.class, () -> esquema.comprobar(c));
        }
    }
    @Test void fkDebenEstarActivasEnCadaConexion() throws Exception {
        var config = config("pruebas");
        try (var c = new FabricaConexiones(config).abrir(); var s = c.createStatement()) {
            var esquema = new EsquemaJdbc(config);
            esquema.crear(c);
            s.executeUpdate("PRAGMA foreign_keys = OFF");
            assertThrows(SQLException.class, () -> esquema.comprobar(c));
        }
    }
}
