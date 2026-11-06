package ejemplos;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Flujos {
    public static void main(String[] args) throws Exception {
        var fichero = Files.createTempFile("ad-lista-", ".txt");
        try {
            Files.write(fichero, List.of("Estudiante A", "Estudiante B", "Estudiante C"),
                    StandardCharsets.UTF_8);
            Files.write(fichero, List.of("Estudiante D"), StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND);
            try (var entrada = Files.newBufferedReader(fichero, StandardCharsets.UTF_8)) {
                String linea;
                while ((linea = entrada.readLine()) != null) System.out.println(linea);
            }
        } finally {
            Files.deleteIfExists(fichero);
        }
    }
}
