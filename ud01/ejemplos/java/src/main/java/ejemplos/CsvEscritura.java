package ejemplos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/** Una fila es un registro, aunque su nombre contenga saltos de línea. */
public class CsvEscritura {
    public record Fila(String nombre, String ciclo, String empresa) {}

    public static void escribir(List<Fila> filas, Path ruta) throws IOException {
        var formato = CSVFormat.DEFAULT.builder().setDelimiter(';')
                .setHeader("nombre", "ciclo", "empresa").get();
        try (var salida = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8);
             var csv = new CSVPrinter(salida, formato)) {
            for (Fila fila : filas) csv.printRecord(fila.nombre(), fila.ciclo(), fila.empresa());
        }
    }

    public static List<Fila> leer(Path ruta) throws IOException {
        var formato = CSVFormat.DEFAULT.builder().setDelimiter(';')
                .setHeader().setSkipHeaderRecord(true).get();
        try (var entrada = Files.newBufferedReader(ruta, StandardCharsets.UTF_8);
             var csv = formato.parse(entrada)) {
            return csv.getRecords().stream().map(r ->
                    new Fila(r.get("nombre"), r.get("ciclo"), r.get("empresa"))).toList();
        }
    }

    public static void main(String[] args) throws Exception {
        Path ruta = Files.createTempFile("ad-asignaciones-", ".csv");
        try {
            var filas = List.of(new Fila("Estudiante A; \"prueba\"\nsegunda línea", "DAM", "Empresa A"));
            escribir(filas, ruta);
            System.out.println(Files.readString(ruta));
            if (!filas.equals(leer(ruta))) throw new AssertionError("Se perdieron valores CSV");
            System.out.println("Un registro recuperado con todos sus valores");
        } finally {
            Files.deleteIfExists(ruta);
        }
    }
}
