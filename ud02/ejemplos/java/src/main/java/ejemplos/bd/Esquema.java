package ejemplos.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/** Solo dos tablas para aislar la relación uno a muchos. */
public class Esquema {
    public static Connection abrir(String url) throws SQLException {
        var p = new java.util.Properties();
        p.setProperty("foreign_keys", "true");
        return DriverManager.getConnection(url, p);
    }

    public static void crear(Connection c) throws SQLException {
        try (var s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS empresa (id TEXT PRIMARY KEY NOT NULL, nombre TEXT NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS tutor (id TEXT PRIMARY KEY NOT NULL, nombre TEXT NOT NULL, empresa_id TEXT NOT NULL REFERENCES empresa(id))");
        }
    }

    public static Set<String> tablas(Connection c) throws SQLException {
        var resultado = new HashSet<String>();
        try (var filas = c.getMetaData().getTables(null, null, "%", new String[]{"TABLE"})) {
            while (filas.next()) resultado.add(filas.getString("TABLE_NAME"));
        }
        return resultado;
    }

    public static void comprobar(Connection c) throws SQLException {
        if (!tablas(c).containsAll(Set.of("empresa", "tutor"))) {
            throw new SQLException("Falta una tabla esperada");
        }
        try (var claves = c.getMetaData().getImportedKeys(null, null, "tutor")) {
            if (!claves.next() || !"empresa".equals(claves.getString("PKTABLE_NAME"))) {
                throw new SQLException("Falta la referencia tutor → empresa");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        try (var c = abrir("jdbc:sqlite::memory:")) {
            crear(c);
            comprobar(c);
            System.out.println("Tablas y referencia comprobadas: " + new java.util.TreeSet<>(tablas(c)));
        }
    }
}
