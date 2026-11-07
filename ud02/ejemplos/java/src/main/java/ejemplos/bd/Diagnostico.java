package ejemplos.bd;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.nio.file.Files;

public class Diagnostico {
    public static void main(String[] args) throws Exception {
        try {
            DriverManager.getDriver("jdbc:inventado:prueba");
            throw new IllegalStateException("Se esperaba una URL sin driver");
        } catch (SQLException esperado) {
            System.out.println("Caso 1: no hay un driver que acepte el protocolo");
        }
        var carpeta = Files.createTempDirectory("ad-fallo-");
        try {
            try (var conexion = DriverManager.getConnection("jdbc:sqlite:" + carpeta.resolve("ausente/base.sqlite"))) {
                throw new IllegalStateException("Se esperaba un directorio inexistente");
            } catch (SQLException esperado) {
                System.out.println("Caso 2: el driver existe; falla la apertura del archivo");
            }
        } finally {
            Files.delete(carpeta);
        }
    }
}
