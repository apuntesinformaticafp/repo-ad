package fct.bd;

import java.sql.SQLException;

/** Cliente proporcionado: ejecuta los métodos que se completan en esta unidad. */
public class DemoConexion {
    public static void main(String[] args) {
        try {
            String accion = args.length == 0 ? "comprobar" : args[0];
            if (!java.util.Set.of("crear", "comprobar", "reiniciar").contains(accion)) {
                throw new IllegalArgumentException("Acción: crear, comprobar o reiniciar");
            }
            boolean confirmado = args.length == 2 && args[1].equals("--confirmar-pruebas");
            var config = ConfiguracionBD.desde(System.getenv());
            var fabrica = new FabricaConexiones(config);
            PreparacionRelacional preparacion = new EsquemaJdbc(config);
            try (var conexion = fabrica.abrir()) {
                if (!conexion.isValid(2)) throw new SQLException("Conexión no válida");
                switch (accion) {
                    case "crear" -> preparacion.crear(conexion);
                    case "comprobar" -> preparacion.comprobar(conexion);
                    case "reiniciar" -> preparacion.reiniciar(conexion, confirmado);
                    default -> throw new IllegalStateException();
                }
                preparacion.comprobar(conexion);
                System.out.println(config + ": " + accion + " y estructura comprobada");
            }
            System.out.println("Conexión cerrada");
        } catch (SQLException error) {
            // No propagar al terminal mensajes del servidor ni Properties con secretos.
            throw new IllegalStateException("Falló la preparación relacional. SQLState="
                    + error.getSQLState() + "; código=" + error.getErrorCode());
        }
    }
}
