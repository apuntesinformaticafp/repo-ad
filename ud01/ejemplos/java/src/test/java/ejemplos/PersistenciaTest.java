package ejemplos;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PersistenciaTest {
    @TempDir Path carpeta;

    @Test void anexarConservaLasLineasAnteriores() throws Exception {
        Path fichero = carpeta.resolve("lista.txt");
        Files.write(fichero, List.of("Estudiante A"));
        Files.write(fichero, List.of("Estudiante B"), java.nio.file.StandardOpenOption.APPEND);
        assertEquals(List.of("Estudiante A", "Estudiante B"), Files.readAllLines(fichero));
    }

    @Test void falloDeReemplazoConservaElFicheroYLimpiaElTemporal() throws Exception {
        Path fichero = carpeta.resolve("datos.txt");
        Files.writeString(fichero, "anterior");
        assertThrows(IOException.class, () -> Errores.escribirSeguro(fichero, "nuevo", true));
        assertEquals("anterior", Files.readString(fichero));
        try (var entradas = Files.list(carpeta)) { assertEquals(1, entradas.count()); }
    }

    @Test void sePuedeReemplazarYReleerUnFicheroVacio() throws Exception {
        Path fichero = carpeta.resolve("datos.txt");
        Files.writeString(fichero, "anterior");
        Errores.escribirSeguro(fichero, "", false);
        assertEquals("", Files.readString(fichero));
    }
}
