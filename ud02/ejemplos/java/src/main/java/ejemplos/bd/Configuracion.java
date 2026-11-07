package ejemplos.bd;

import java.util.Map;
import java.util.Properties;

/** Ejemplo pequeño. El proyecto FCT incorpora una validación más completa. */
public class Configuracion {
    public static String url(Map<String, String> entorno) {
        String url = entorno.getOrDefault("FCT_JDBC_URL", "jdbc:sqlite::memory:");
        if (!(url.startsWith("jdbc:sqlite:") || url.startsWith("jdbc:postgresql://"))) {
            throw new IllegalArgumentException("FCT_JDBC_URL debe seleccionar SQLite o PostgreSQL");
        }
        return url;
    }

    public static Properties propiedades(Map<String, String> entorno) {
        var p = new Properties();
        p.setProperty("foreign_keys", "true");
        p.setProperty("connectTimeout", "2");
        p.setProperty("socketTimeout", "3");
        if (url(entorno).startsWith("jdbc:postgresql:")) {
            for (var par : Map.of("FCT_DB_USER", "user", "FCT_DB_PASSWORD", "password").entrySet()) {
                String valor = entorno.get(par.getKey());
                if (valor == null || valor.isBlank()) {
                    throw new IllegalArgumentException("Falta " + par.getKey());
                }
                p.setProperty(par.getValue(), valor);
            }
        }
        return p;
    }

    public static void main(String[] args) {
        var entorno = System.getenv();
        propiedades(entorno);
        System.out.println("Configuración válida para " + (url(entorno).startsWith("jdbc:sqlite:") ? "SQLite" : "PostgreSQL"));
        // No imprimir las Properties: incluyen la contraseña.
    }
}
