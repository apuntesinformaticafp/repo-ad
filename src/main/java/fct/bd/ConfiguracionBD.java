package fct.bd;

import java.util.Map;
import java.util.Properties;

/** Infraestructura proporcionada: validación previa, sin secretos en toString. */
public final class ConfiguracionBD {
    private final String url;
    private final String usuario;
    private final String password;
    private final String entorno;
    private final String esquema;

    private ConfiguracionBD(String url, String usuario, String password, String entorno, String esquema) {
        this.url = url;
        this.usuario = usuario;
        this.password = password;
        this.entorno = entorno;
        this.esquema = esquema;
    }

    public static ConfiguracionBD desde(Map<String, String> variables) {
        String url = obligatorio(variables, "FCT_JDBC_URL");
        boolean sqlite = url.startsWith("jdbc:sqlite:") && url.length() > "jdbc:sqlite:".length();
        // Pila deliberadamente acotada: no admite credenciales ni opciones dentro de la URL.
        boolean postgres = url.matches("jdbc:postgresql://[^/?@\\s]+/[a-zA-Z0-9_-]+");
        if (!sqlite && !postgres) throw new IllegalArgumentException("FCT_JDBC_URL no tiene un formato admitido");
        String entorno = variables.getOrDefault("FCT_ENTORNO", "desarrollo");
        if (!entorno.equals("desarrollo") && !entorno.equals("pruebas")) {
            throw new IllegalArgumentException("FCT_ENTORNO debe ser desarrollo o pruebas");
        }
        String esquema = variables.getOrDefault("FCT_DB_SCHEMA", "fct_ud02");
        if (!esquema.matches("fct_ud02(?:_[a-z0-9]+)?")) {
            throw new IllegalArgumentException("FCT_DB_SCHEMA debe ser fct_ud02 o fct_ud02_<identificador>");
        }
        String usuario = postgres ? obligatorio(variables, "FCT_DB_USER") : "";
        String password = postgres ? obligatorio(variables, "FCT_DB_PASSWORD") : "";
        return new ConfiguracionBD(url, usuario, password, entorno, esquema);
    }

    private static String obligatorio(Map<String, String> variables, String clave) {
        String valor = variables.get(clave);
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException("Falta " + clave);
        return valor;
    }

    public String url() { return url; }
    public boolean sqlite() { return url.startsWith("jdbc:sqlite:"); }
    public boolean pruebas() { return entorno.equals("pruebas"); }
    public String esquema() { return esquema; }

    public Properties propiedades() {
        var p = new Properties();
        if (sqlite()) {
            p.setProperty("foreign_keys", "true");
            p.setProperty("busy_timeout", "2000");
        } else {
            p.setProperty("user", usuario);
            p.setProperty("password", password);
            p.setProperty("connectTimeout", "2");
            p.setProperty("socketTimeout", "3");
            p.setProperty("logServerErrorDetail", "false");
            p.setProperty("currentSchema", esquema);
        }
        return p;
    }

    @Override public String toString() {
        return "ConfiguracionBD[gestor=" + (sqlite() ? "SQLite" : "PostgreSQL") + ", entorno=" + entorno + "]";
    }
}
