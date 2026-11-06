package fct.bd;

import java.sql.Connection;
import java.sql.SQLException;

/** UD2: usa la URL y Properties validadas; el cliente cierra la conexión. */
public class FabricaConexiones {
    private final ConfiguracionBD config;
    public FabricaConexiones(ConfiguracionBD config) { this.config = config; }

    public Connection abrir() throws SQLException {
        // TODO: conectar por JDBC en ambos gestores, activando las FK en SQLite.
        throw new UnsupportedOperationException("UD2: abrir la conexión");
    }
}
