package ejemplos.bd;

import java.nio.file.Files;
import java.sql.DriverManager;

/** Persiste una estructura entre conexiones, sin un servidor SQLite. */
public class Gestores {
    public static void main(String[] args) throws Exception {
        var archivo = Files.createTempFile("ad-ud02-", ".sqlite");
        try {
            String url = "jdbc:sqlite:" + archivo;
            try (var conexion = DriverManager.getConnection(url);
                 var sentencia = conexion.createStatement()) {
                sentencia.executeUpdate("CREATE TABLE marca (id TEXT PRIMARY KEY NOT NULL)");
            }
            try (var conexion = DriverManager.getConnection(url);
                 var tablas = conexion.getMetaData().getTables(null, null, "marca", new String[]{"TABLE"})) {
                System.out.println("Gestor: " + conexion.getMetaData().getDatabaseProductName());
                System.out.println("Tabla tras reabrir: " + tablas.next());
            }
        } finally {
            Files.deleteIfExists(archivo);
        }
    }
}
