package ejemplos.bd;

import java.sql.DriverManager;

/** Resolver el driver NO abre una conexión ni comprueba el servidor. */
public class Conectores {
    public static void main(String[] args) throws Exception {
        for (String url : new String[]{"jdbc:sqlite::memory:", "jdbc:postgresql://localhost/fct"}) {
            var driver = DriverManager.getDriver(url);
            System.out.println(driver.getClass().getName() + " acepta la URL: " + driver.acceptsURL(url));
        }
    }
}
