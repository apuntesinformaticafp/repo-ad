package ejemplos;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class Rutas {
    public static void main(String[] args) throws Exception {
        System.out.println(Path.of("fct", "empresas.json").toAbsolutePath());
        Path base = Files.createTempDirectory("ad-rutas-");
        try {
            for (String ruta : new String[]{"alumnos/dam", "alumnos/daw", "empresas", "convenios"}) {
                Files.createDirectories(base.resolve(ruta));
                Files.createDirectories(base.resolve(ruta)); // Repetir es válido.
            }
            Path origen = base.resolve("informe.txt");
            Files.writeString(origen, "Seguimiento de prueba");
            Path copia = base.resolve("copia.txt");
            Files.copy(origen, copia);
            Files.move(origen, base.resolve("renombrado.txt"));
            Files.delete(copia);
            try (var entradas = Files.list(base)) {
                System.out.println(entradas.map(p -> p.getFileName().toString()).sorted().toList());
            }
        } finally {
            try (var entradas = Files.walk(base)) {
                for (Path ruta : entradas.sorted(Comparator.reverseOrder()).toList()) {
                    Files.delete(ruta);
                }
            }
        }
    }
}
