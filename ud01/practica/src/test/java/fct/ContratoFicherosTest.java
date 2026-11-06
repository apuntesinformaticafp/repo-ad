package fct;

import static org.junit.jupiter.api.Assertions.*;
import fct.Modelo.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** Criterios automatizados de aceptación para el trabajo de UD1. */
@Tag("contrato")
@Tag("ud01")
class ContratoFicherosTest {
    @TempDir Path carpeta;
    private Path ruta() { return carpeta.resolve("estado.json"); }
    private Estado inicial() {
        return new Estado(List.of(
                new Empresa("E01", "Empresa A; formación", List.of(new Tutor("T01", "Tutor A"))),
                new Empresa("E02", "Empresa B", List.of())), List.of(
                new Estudiante("A01", "Estudiante A; \"prueba\"\nsegunda línea", "DAM"),
                new Estudiante("A02", "Estudiante B", "DAW")), List.of(
                new Asignacion("AS01", "A01", "E01", "T01", "2026-10-01", "2026-12-01")));
    }
    private RepositorioJson preparado() throws IOException {
        var repo = new RepositorioJson(ruta());
        repo.guardar(inicial());
        return repo;
    }

    @Test void nuevaInstanciaRecuperaValoresYRelaciones() throws Exception {
        preparado();
        assertEquals(inicial(), new RepositorioJson(ruta()).recuperar());
    }
    @Test void vacioValidoSeGuardaYRecupera() throws Exception {
        var repo = preparado();
        repo.guardar(Estado.vacio());
        assertEquals(Estado.vacio(), new RepositorioJson(ruta()).recuperar());
    }
    @Test void unaCopiaMovidaSeRecuperaDesdeOtraRutaSinAlterarElOriginal() throws Exception {
        preparado();
        Path copia = carpeta.resolve("copia.json"), movida = carpeta.resolve("movida.json");
        Files.copy(ruta(), copia);
        Files.move(copia, movida);
        assertEquals(inicial(), new RepositorioJson(movida).recuperar());
        assertFalse(Files.exists(copia));
        Files.delete(movida);
        assertEquals(inicial(), new RepositorioJson(ruta()).recuperar());
    }
    @Test void ficheroAusenteNoDevuelveUnEstadoVacio() {
        assertThrows(IOException.class, () -> new RepositorioJson(ruta()).recuperar());
    }
    @ParameterizedTest @ValueSource(strings = {"", "{", "{}"})
    void contenidoInvalidoNoSePresentaComoVacio(String texto) throws Exception {
        Files.writeString(ruta(), texto, StandardCharsets.UTF_8);
        assertThrows(IOException.class, () -> new RepositorioJson(ruta()).recuperar());
    }
    @Test void jsonNuloNoEsUnEstadoValido() throws Exception {
        Files.writeString(ruta(), "null");
        assertThrows(IllegalArgumentException.class, () -> new RepositorioJson(ruta()).recuperar());
    }
    @Test void esquemaIncompatibleSeRechaza() throws Exception {
        preparado();
        String texto = Files.readString(ruta()).replace("\"empresas\"", "\"organizaciones\"");
        Files.writeString(ruta(), texto);
        assertThrows(IOException.class, () -> new RepositorioJson(ruta()).recuperar());
    }
    @Test void modificarYEliminarConservaElRestoYBusquedaAusente() throws Exception {
        var servicio = new ServicioFct(preparado());
        assertTrue(servicio.buscarEmpresa("E99").isEmpty());
        servicio.guardarEmpresa(new Empresa("E02", "Empresa B modificada", List.of()));
        assertEquals("Empresa B modificada", servicio.buscarEmpresa("E02").orElseThrow().nombre());
        servicio.eliminarEmpresa("E02");
        assertEquals(List.of(inicial().empresas().getFirst()), servicio.listarEmpresas());
    }
    @Test void noSeEliminaUnaEmpresaConAsignaciones() throws Exception {
        var servicio = new ServicioFct(preparado());
        byte[] anterior = Files.readAllBytes(ruta());
        assertThrows(IllegalArgumentException.class, () -> servicio.eliminarEmpresa("E01"));
        assertArrayEquals(anterior, Files.readAllBytes(ruta()));
    }
    @Test void altasFiltroYCierreDeNuevaAsignacion() throws Exception {
        var servicio = new ServicioFct(preparado());
        var estudiante = new Estudiante("A03", "Estudiante C", "DAM");
        servicio.guardarEstudiante(estudiante);
        assertEquals(2, servicio.listarEstudiantes("DAM").size());
        servicio.asignar(new Asignacion("AS02", "A03", "E01", "T01", "2027-01-01", "2027-02-01"));
        assertEquals(2, new RepositorioJson(ruta()).recuperar().asignaciones().size());
    }
    @Test void validacionAnteriorAEscrituraConservaBytesYEventos() throws Exception {
        var eventos = new ArrayList<String>();
        var repo = new RepositorioJson(ruta(), eventos::add, false);
        repo.guardar(inicial());
        byte[] anterior = Files.readAllBytes(ruta());
        var empresasDuplicadas = new Estado(List.of(inicial().empresas().getFirst(),
                inicial().empresas().getFirst()), inicial().estudiantes(), inicial().asignaciones());
        assertThrows(IllegalArgumentException.class, () -> repo.guardar(empresasDuplicadas));
        assertArrayEquals(anterior, Files.readAllBytes(ruta()));
        assertEquals(List.of("estado guardado"), eventos);
    }
    @Test void tutorDuplicadoSeRechazaAntesDeGuardar() throws Exception {
        var repo = preparado();
        var tutor = new Tutor("T01", "Tutor B");
        var estado = new Estado(List.of(inicial().empresas().getFirst(),
                new Empresa("E02", "Empresa B", List.of(tutor))), List.of(), List.of());
        assertThrows(IllegalArgumentException.class, () -> repo.guardar(estado));
        assertEquals(inicial(), repo.recuperar());
    }
    @Test void fechasReferenciasYTutorDeOtraEmpresaNoAlteranEstado() throws Exception {
        var servicio = new ServicioFct(preparado());
        for (Asignacion invalida : List.of(
                new Asignacion("AS02", "A01", "E01", "T01", "2026-12-01", "2026-10-01"),
                new Asignacion("AS02", "A99", "E01", "T01", "2026-10-01", "2026-12-01"),
                new Asignacion("AS02", "A01", "E02", "T01", "2026-10-01", "2026-12-01"),
                inicial().asignaciones().getFirst())) {
            assertThrows(IllegalArgumentException.class, () -> servicio.asignar(invalida));
            assertEquals(inicial(), new RepositorioJson(ruta()).recuperar());
        }
    }
    @Test void falloSimuladoConservaBytesYLimpiaTemporalSinNotificar() throws Exception {
        preparado();
        byte[] anterior = Files.readAllBytes(ruta());
        var eventos = new ArrayList<String>();
        var repo = new RepositorioJson(ruta(), eventos::add, true);
        assertThrows(IOException.class, () -> repo.guardar(Estado.vacio()));
        assertArrayEquals(anterior, Files.readAllBytes(ruta()));
        assertTrue(eventos.isEmpty());
        try (var entradas = Files.list(carpeta)) { assertEquals(1, entradas.count()); }
    }
    @Test void csvConservaDelimitadoresYComillasAlReleer() throws Exception {
        Path csv = carpeta.resolve("asignaciones.csv");
        Intercambio.exportarAsignacionesCsv(inicial(), csv);
        var formato = CSVFormat.DEFAULT.builder().setDelimiter(';')
                .setHeader().setSkipHeaderRecord(true).get();
        try (var lector = Files.newBufferedReader(csv, StandardCharsets.UTF_8);
             var parser = formato.parse(lector)) {
            var filas = parser.getRecords();
            assertEquals(1, filas.size());
            assertEquals(inicial().estudiantes().getFirst().nombre(), filas.getFirst().get("nombre"));
            assertEquals("DAM", filas.getFirst().get("ciclo"));
            assertEquals(inicial().empresas().getFirst().nombre(), filas.getFirst().get("empresa"));
        }
    }

    @Test void recuperacionValidaLasRelacionesDelDocumento() throws Exception {
        preparado();
        String texto = Files.readString(ruta()).replace("\"T01\"", "\"\"");
        Files.writeString(ruta(), texto);
        assertThrows(IllegalArgumentException.class, () -> new RepositorioJson(ruta()).recuperar());
    }

    @Test void csvVacioConservaCabeceraYVariasAsignacionesConservanOrden() throws Exception {
        Path csv = carpeta.resolve("asignaciones.csv");
        var formato = CSVFormat.DEFAULT.builder().setDelimiter(';')
                .setHeader().setSkipHeaderRecord(true).get();
        Intercambio.exportarAsignacionesCsv(Estado.vacio(), csv);
        try (var lector = Files.newBufferedReader(csv); var parser = formato.parse(lector)) {
            assertEquals(List.of("nombre", "ciclo", "empresa"), parser.getHeaderNames());
            assertTrue(parser.getRecords().isEmpty());
        }
        Estado estado = inicial();
        var segunda = new Asignacion("AS02", "A02", "E01", "T01", "2027-01-01", "2027-02-01");
        var varias = new Estado(estado.empresas(), estado.estudiantes(),
                List.of(segunda, estado.asignaciones().getFirst()));
        Intercambio.exportarAsignacionesCsv(varias, csv);
        try (var lector = Files.newBufferedReader(csv); var parser = formato.parse(lector)) {
            var filas = parser.getRecords();
            assertEquals(2, filas.size());
            assertEquals("Estudiante B", filas.getFirst().get("nombre"));
            assertEquals(estado.estudiantes().getFirst().nombre(), filas.get(1).get("nombre"));
        }
    }

    @Test void elAvisoSeEmiteCuandoElEstadoYaPuedeReleerse() throws Exception {
        var eventos = new ArrayList<String>();
        var repo = new RepositorioJson(ruta(), mensaje -> {
            assertDoesNotThrow(() -> assertEquals(inicial(), new RepositorioJson(ruta()).recuperar()));
            eventos.add(mensaje);
        }, false);
        repo.guardar(inicial());
        assertEquals(List.of("estado guardado"), eventos);
    }
}
