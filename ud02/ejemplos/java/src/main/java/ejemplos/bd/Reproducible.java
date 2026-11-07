package ejemplos.bd;

import java.sql.SQLException;

public class Reproducible {
    public static void main(String[] args) throws Exception {
        // Memoria privada de esta conexión: no se borra una base ajena.
        try (var c = Esquema.abrir("jdbc:sqlite::memory:")) {
            Esquema.crear(c);
            Esquema.crear(c);
            Esquema.comprobar(c);
            System.out.println("Preparar dos veces conserva la estructura");
            try (var s = c.createStatement()) { s.executeUpdate("DROP TABLE tutor"); }
            try {
                Esquema.comprobar(c);
                throw new IllegalStateException("Una estructura incompleta no debe pasar");
            } catch (SQLException esperado) {
                System.out.println("La comprobación detecta la preparación incompleta");
            }
            Esquema.crear(c);
            Esquema.comprobar(c);
            System.out.println("Estructura preparada de nuevo");
        }
    }
}
