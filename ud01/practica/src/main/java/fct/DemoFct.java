package fct;

import com.fasterxml.jackson.databind.ObjectMapper;
import fct.Modelo.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Cliente compartido: ejecutable cuando el adaptador esté implementado. */
public class DemoFct {
    public static void main(String[] args) throws Exception {
        Path proyecto = Path.of(System.getProperty("fct.proyecto", "."));
        Path carpeta = proyecto.resolve("datos-demo");
        Files.createDirectories(carpeta);
        Estado inicial = new ObjectMapper().readValue(
                Files.readString(proyecto.resolve("datos/estado-inicial.json")), Estado.class);
        var eventos = new ArrayList<String>();
        Path ruta = carpeta.resolve("estado.json");
        var repo = new RepositorioJson(ruta, eventos::add, false);
        repo.guardar(inicial);
        if (!new RepositorioJson(ruta).recuperar().equals(inicial))
            throw new AssertionError("Falló la relectura desde un componente nuevo");
        System.out.println("Relectura JSON correcta");
        var servicio = new ServicioFct(repo);
        servicio.guardarEmpresa(new Empresa("E02", "Empresa B modificada", List.of()));
        System.out.println(servicio.buscarEmpresa("E02").orElseThrow().nombre());
        servicio.eliminarEmpresa("E02");
        Intercambio.exportarAsignacionesCsv(repo.recuperar(), carpeta.resolve("asignaciones.csv"));
        System.out.println("CSV generado");
        byte[] anterior = Files.readAllBytes(ruta);
        int avisos = eventos.size();
        try {
            new RepositorioJson(ruta, eventos::add, true).guardar(Estado.vacio());
            throw new AssertionError("No se produjo el fallo previsto");
        } catch (IOException esperado) {
            System.out.println(esperado.getMessage());
        }
        if (!java.util.Arrays.equals(anterior, Files.readAllBytes(ruta)) || eventos.size() != avisos)
            throw new AssertionError("El fallo alteró el fichero o notificó éxito");
        System.out.println("Fichero anterior conservado; sin notificación de éxito en el fallo");
    }
}
