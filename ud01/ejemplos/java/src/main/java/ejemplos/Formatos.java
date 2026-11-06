package ejemplos;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.StringReader;
import org.apache.commons.csv.CSVFormat;

public class Formatos {
    public static void main(String[] args) throws Exception {
        var mapper = new ObjectMapper();
        var empresa = mapper.readTree("{\"id\":\"E01\",\"tutores\":[]}");
        System.out.println(empresa.get("tutores").isArray());
        String csv = "nombre;ciclo;empresa\n\"Estudiante A; prueba\";DAM;E01\nEstudiante B;DAW;E02\n";
        var formato = CSVFormat.DEFAULT.builder().setDelimiter(';')
                .setHeader().setSkipHeaderRecord(true).get();
        try (var parser = formato.parse(new StringReader(csv))) {
            for (var fila : parser) {
                if (fila.get("ciclo").equals("DAM")) System.out.println(fila.get("nombre"));
            }
        }
    }
}
