package laboratorio;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ejemplos.Errores;
import ejemplos.JsonObjetos;
import ejemplos.JsonObjetos.Empresa;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/** Laboratorio completo de empresas. Ejecuta, modifica y comprueba. */
public class EmpresasFct {
    static void texto(String valor) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException("Texto obligatorio");
    }

    static void validar(List<Empresa> empresas) {
        if (empresas == null) throw new IllegalArgumentException("Lista obligatoria");
        var ids = new HashSet<String>();
        var tutores = new HashSet<String>();
        for (var empresa : empresas) {
            if (empresa == null || empresa.tutores() == null) throw new IllegalArgumentException("Empresa incompleta");
            texto(empresa.id()); texto(empresa.nombre());
            if (!ids.add(empresa.id())) throw new IllegalArgumentException("Empresa duplicada");
            for (var tutor : empresa.tutores()) {
                if (tutor == null) throw new IllegalArgumentException("Tutor nulo");
                texto(tutor.id()); texto(tutor.nombre());
                if (!tutores.add(tutor.id())) throw new IllegalArgumentException("Tutor duplicado");
            }
        }
    }

    static class RepositorioEmpresas {
        private final Path ruta;
        private final Consumer<String> notificar;
        RepositorioEmpresas(Path ruta, Consumer<String> notificar) {
            this.ruta = ruta; this.notificar = notificar;
        }
        void guardar(List<Empresa> empresas, boolean fallo) throws IOException {
            validar(empresas);
            String texto = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(empresas);
            Files.createDirectories(ruta.toAbsolutePath().getParent());
            Errores.escribirSeguro(ruta, texto, fallo);
            notificar.accept("empresas guardadas");
        }
        List<Empresa> recuperar() throws IOException {
            List<Empresa> empresas = new ObjectMapper().readValue(Files.readString(ruta), new TypeReference<>() {});
            validar(empresas);
            return empresas;
        }
    }

    static void exportar(List<Empresa> empresas, Path ruta) throws IOException {
        validar(empresas);
        var formato = CSVFormat.DEFAULT.builder().setDelimiter(';').setHeader("id", "nombre").get();
        try (var salida = Files.newBufferedWriter(ruta); var csv = new CSVPrinter(salida, formato)) {
            for (var empresa : empresas) csv.printRecord(empresa.id(), empresa.nombre());
        }
    }

    static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }

    public static void main(String[] args) throws Exception {
        Path carpeta = Path.of(args.length == 0 ? "datos-demo/laboratorio-java" : args[0]);
        Files.createDirectories(carpeta);
        var datos = List.of(JsonObjetos.muestra(), new Empresa("E02", "Empresa B", List.of()));
        var eventos = new ArrayList<String>();
        Path ruta = carpeta.resolve("empresas.json");
        var repo = new RepositorioEmpresas(ruta, eventos::add);
        repo.guardar(datos, false);
        comprobar(datos.equals(new RepositorioEmpresas(ruta, m -> {}).recuperar()), "Relectura JSON");
        System.out.println("JSON: relectura correcta con un componente nuevo");
        Path csv = carpeta.resolve("empresas.csv");
        exportar(datos, csv);
        var formato = CSVFormat.DEFAULT.builder().setDelimiter(';').setHeader().setSkipHeaderRecord(true).get();
        try (var entrada = Files.newBufferedReader(csv); var parser = formato.parse(entrada)) {
            var filas = parser.getRecords();
            comprobar(filas.size() == datos.size(), "Filas CSV");
            for (int i = 0; i < datos.size(); i++) {
                comprobar(filas.get(i).get("id").equals(datos.get(i).id()), "ID CSV");
                comprobar(filas.get(i).get("nombre").equals(datos.get(i).nombre()), "Nombre CSV");
            }
        }
        System.out.println("CSV: nombres conservados; tutores omitidos por la proyección");
        byte[] anterior = Files.readAllBytes(ruta);
        try {
            repo.guardar(List.of(datos.getFirst(), datos.getFirst()), false);
            throw new AssertionError("No rechazó el duplicado");
        } catch (IllegalArgumentException esperado) { System.out.println("Error controlado: " + esperado.getMessage()); }
        try {
            repo.guardar(List.of(), true);
            throw new AssertionError("No ocurrió el fallo simulado");
        } catch (IOException esperado) { System.out.println("Error controlado: " + esperado.getMessage()); }
        comprobar(Arrays.equals(anterior, Files.readAllBytes(ruta)), "Fichero anterior alterado");
        comprobar(eventos.equals(List.of("empresas guardadas")), "Avisos incorrectos");
        try (var archivos = Files.list(carpeta)) {
            comprobar(archivos.noneMatch(p -> p.getFileName().toString().endsWith(".tmp")), "Temporal sin limpiar");
        }
        System.out.println("Fichero previo y notificaciones correctos tras ambos fallos");
        var vacio = new RepositorioEmpresas(carpeta.resolve("vacio.json"), m -> {});
        vacio.guardar(List.of(), false);
        comprobar(vacio.recuperar().isEmpty(), "Colección vacía");
        System.out.println("Colección vacía: comprobación correcta");
    }
}
