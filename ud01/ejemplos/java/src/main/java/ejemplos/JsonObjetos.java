package ejemplos;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Objeto → texto JSON → fichero → objeto. El formato no valida el negocio. */
public class JsonObjetos {
    public record Tutor(String id, String nombre) {}
    public record Empresa(String id, String nombre, List<Tutor> tutores) {}

    public static void guardar(Empresa empresa, Path ruta) throws IOException {
        var mapper = new ObjectMapper();
        String texto = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(empresa);
        Files.writeString(ruta, texto, StandardCharsets.UTF_8);
    }

    public static Empresa recuperar(Path ruta) throws IOException {
        var mapper = new ObjectMapper();
        String texto = Files.readString(ruta, StandardCharsets.UTF_8);
        return mapper.readValue(texto, Empresa.class);
    }

    public static Empresa muestra() {
        return new Empresa("E01", "Empresa A; formación \"técnica\"",
                List.of(new Tutor("T01", "Tutor A")));
    }

    public static void main(String[] args) throws Exception {
        Path ruta = Files.createTempFile("ad-empresa-", ".json");
        try {
            Empresa original = muestra();
            guardar(original, ruta);
            System.out.println(Files.readString(ruta));
            Empresa recuperada = recuperar(ruta);
            if (!original.equals(recuperada)) throw new AssertionError("Se perdieron valores");
            System.out.println("Mismos valores: " + original.equals(recuperada));
            System.out.println("Misma instancia: " + (original == recuperada));
        } finally {
            Files.deleteIfExists(ruta);
        }
    }
}
