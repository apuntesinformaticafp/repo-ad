package ejemplos;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FormatosTest {
    @TempDir Path carpeta;

    @Test void jsonReleeObjetosAnidadosYNoDependeDeLaIdentidad() throws Exception {
        var original = JsonObjetos.muestra();
        var ruta = carpeta.resolve("empresa.json");
        JsonObjetos.guardar(original, ruta);
        var recuperada = JsonObjetos.recuperar(ruta);
        assertEquals(original, recuperada);
        assertNotSame(original, recuperada);
    }

    @Test void listaVaciaDeTutoresEsDistintaDeFicheroVacio() throws Exception {
        var ruta = carpeta.resolve("empresa.json");
        var original = new JsonObjetos.Empresa("E01", "Empresa A", List.of());
        JsonObjetos.guardar(original, ruta);
        assertEquals(original, JsonObjetos.recuperar(ruta));
        Files.writeString(ruta, "");
        assertThrows(IOException.class, () -> JsonObjetos.recuperar(ruta));
    }

    @Test void csvConservaSeparadoresComillasSaltosYUnicode() throws Exception {
        var filas = List.of(new CsvEscritura.Fila("Estudiante A; \"ñ\"\nsegunda línea", "DAM", "Empresa A"));
        var ruta = carpeta.resolve("asignaciones.csv");
        CsvEscritura.escribir(filas, ruta);
        assertEquals(filas, CsvEscritura.leer(ruta));
        CsvEscritura.escribir(List.of(), ruta);
        assertEquals(List.of(), CsvEscritura.leer(ruta));
        assertTrue(Files.readString(ruta).startsWith("nombre;ciclo;empresa"));
    }
}
