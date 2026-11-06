package ejemplos;

import java.nio.file.Files;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/** JSON → modelo → CSV de resumen. Los tutores no se exportan. */
public class Conversion {
    public static void main(String[] args) throws Exception {
        var json = Files.createTempFile("ad-modelo-", ".json");
        var csv = Files.createTempFile("ad-resumen-", ".csv");
        try {
            JsonObjetos.guardar(JsonObjetos.muestra(), json);
            var empresa = JsonObjetos.recuperar(json);
            var formato = CSVFormat.DEFAULT.builder().setDelimiter(';').setHeader("id", "nombre").get();
            try (var salida = Files.newBufferedWriter(csv);
                 var escritor = new CSVPrinter(salida, formato)) {
                escritor.printRecord(empresa.id(), empresa.nombre());
            }
            System.out.println(Files.readString(csv));
            System.out.println("Tutores omitidos en CSV: " + empresa.tutores().size());
        } finally {
            Files.deleteIfExists(json);
            Files.deleteIfExists(csv);
        }
    }
}
