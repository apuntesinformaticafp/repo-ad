package ejemplos.bd;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;

/** Un único préstamo permite reproducir el agotamiento sin carga concurrente. */
public class Pool {
    public static void main(String[] args) throws Exception {
        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite::memory:");
        config.setMaximumPoolSize(1);
        config.setConnectionTimeout(300);
        config.setValidationTimeout(250);
        try (var pool = new HikariDataSource(config)) {
            try (var prestada = pool.getConnection()) {
                System.out.println("Primer préstamo válido: " + prestada.isValid(1));
                try (var segunda = pool.getConnection()) {
                    throw new IllegalStateException("El pool no debería prestar una segunda conexión");
                } catch (SQLException esperado) {
                    System.out.println("Segundo préstamo: espera limitada y agotamiento");
                }
            }
            try (var devuelta = pool.getConnection()) {
                System.out.println("Tras devolver: " + devuelta.isValid(1));
            }
        }
    }
}
