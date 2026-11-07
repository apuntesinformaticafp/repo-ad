package fct.bd;

import java.sql.Connection;
import java.sql.SQLException;

/** Completa esta infraestructura; no implementes el CRUD de UD3. */
public class EsquemaJdbc implements PreparacionRelacional {
    private final ConfiguracionBD config;
    public EsquemaJdbc(ConfiguracionBD config) { this.config = config; }

    @Override public void crear(Connection conexion) throws SQLException {
        // TODO: DDL de cuatro tablas; adaptar fechas y preparar el namespace PostgreSQL.
        throw new UnsupportedOperationException("UD2: crear el esquema");
    }

    @Override public void comprobar(Connection conexion) throws SQLException {
        // TODO: tablas, columnas, nulabilidad, claves y referencias; no basta SELECT 1.
        throw new UnsupportedOperationException("UD2: comprobar el esquema");
    }

    @Override public void reiniciar(Connection conexion, boolean confirmado) throws SQLException {
        // TODO: exigir entorno de pruebas y confirmación; preservar tablas ajenas.
        throw new UnsupportedOperationException("UD2: reiniciar el esquema de pruebas");
    }
}
