package fct;

import fct.Modelo.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Operaciones del cliente: no conoce el formato del almacén. */
public class ServicioFct {
    private final AlmacenFct almacen;
    public ServicioFct(AlmacenFct almacen) { this.almacen = almacen; }
    public List<Empresa> listarEmpresas() throws IOException { return almacen.recuperar().empresas(); }
    public Optional<Empresa> buscarEmpresa(String id) throws IOException {
        return listarEmpresas().stream().filter(e -> e.id().equals(id)).findFirst();
    }
    public void guardarEmpresa(Empresa empresa) throws IOException {
        Estado estado = almacen.recuperar();
        var empresas = new ArrayList<>(estado.empresas());
        empresas.removeIf(e -> e.id().equals(empresa.id()));
        empresas.add(empresa);
        guardar(new Estado(empresas, estado.estudiantes(), estado.asignaciones()));
    }
    public void eliminarEmpresa(String id) throws IOException {
        Estado estado = almacen.recuperar();
        if (estado.asignaciones().stream().anyMatch(a -> a.empresaId().equals(id)))
            throw new IllegalArgumentException("Empresa con asignaciones");
        var empresas = new ArrayList<>(estado.empresas());
        empresas.removeIf(e -> e.id().equals(id));
        guardar(new Estado(empresas, estado.estudiantes(), estado.asignaciones()));
    }
    public void guardarEstudiante(Estudiante estudiante) throws IOException {
        Estado estado = almacen.recuperar();
        var estudiantes = new ArrayList<>(estado.estudiantes());
        estudiantes.removeIf(e -> e.id().equals(estudiante.id()));
        estudiantes.add(estudiante);
        guardar(new Estado(estado.empresas(), estudiantes, estado.asignaciones()));
    }
    public List<Estudiante> listarEstudiantes(String ciclo) throws IOException {
        return almacen.recuperar().estudiantes().stream().filter(e -> e.ciclo().equals(ciclo)).toList();
    }
    public void asignar(Asignacion asignacion) throws IOException {
        Estado estado = almacen.recuperar();
        var asignaciones = new ArrayList<>(estado.asignaciones());
        asignaciones.add(asignacion);
        guardar(new Estado(estado.empresas(), estado.estudiantes(), asignaciones));
    }
    private void guardar(Estado estado) throws IOException {
        Validacion.validar(estado);
        almacen.guardar(estado);
    }
}
