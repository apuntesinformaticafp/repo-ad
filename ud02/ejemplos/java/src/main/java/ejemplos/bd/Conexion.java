package ejemplos.bd;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    public static void main(String[] args) throws Exception {
        var entorno = System.getenv();
        try (var conexion = DriverManager.getConnection(Configuracion.url(entorno),
                Configuracion.propiedades(entorno))) {
            System.out.println("Gestor: " + conexion.getMetaData().getDatabaseProductName());
            System.out.println("Conexión válida: " + conexion.isValid(2));
        } catch (SQLException error) {
            System.err.println("Conexión rechazada; revisar servicio, acceso y configuración. SQLState=" + error.getSQLState());
            // La excepción sigue indicando fracaso, pero no publica credenciales ni la URL.
            throw new SQLException("Conexión rechazada", error.getSQLState(), error.getErrorCode());
        }
        System.out.println("Conexión cerrada al salir del bloque");
    }
}
