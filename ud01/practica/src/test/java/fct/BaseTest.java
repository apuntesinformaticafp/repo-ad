package fct;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import fct.Modelo.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class BaseTest {
    @Test void datosInicialesSonValidos() throws Exception {
        Estado estado = new ObjectMapper().readValue(
                Files.readString(Path.of("datos/estado-inicial.json")), Estado.class);
        assertDoesNotThrow(() -> Validacion.validar(estado));
        assertEquals(2, estado.empresas().size());
        assertEquals("A01", estado.asignaciones().getFirst().estudianteId());
    }
    @Test void estadoVacioEsValidoSinEquivalerAUnFicheroAusente() {
        assertDoesNotThrow(() -> Validacion.validar(Estado.vacio()));
        assertTrue(Estado.vacio().asignaciones().isEmpty());
    }
    @Test void asignacionConReferenciaInexistenteSeRechaza() {
        var estado = new Estado(List.of(), List.of(), List.of(
                new Asignacion("AS01", "A99", "E99", "T99", "2026-10-01", "2026-12-01")));
        assertThrows(IllegalArgumentException.class, () -> Validacion.validar(estado));
    }
}
