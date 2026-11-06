package fct.bd;

import java.sql.Connection;
import java.sql.SQLException;

/** Preparación de UD2, NO es todavía el repositorio de negocio de UD3. */
public interface PreparacionRelacional {
    void crear(Connection conexion) throws SQLException;
    void comprobar(Connection conexion) throws SQLException;
    void reiniciar(Connection conexion, boolean confirmado) throws SQLException;
}
