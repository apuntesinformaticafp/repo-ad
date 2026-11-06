package fct;

import java.util.List;

/** Dominio mínimo de UD1, compartido por trabajo y referencia. */
public final class Modelo {
    private Modelo() {}
    public record Tutor(String id, String nombre) {}
    public record Empresa(String id, String nombre, List<Tutor> tutores) {
        public Empresa { tutores = List.copyOf(tutores); }
    }
    public record Estudiante(String id, String nombre, String ciclo) {}
    public record Asignacion(String id, String estudianteId, String empresaId,
                             String tutorId, String inicio, String fin) {}
    public record Estado(List<Empresa> empresas, List<Estudiante> estudiantes,
                         List<Asignacion> asignaciones) {
        public Estado {
            empresas = List.copyOf(empresas);
            estudiantes = List.copyOf(estudiantes);
            asignaciones = List.copyOf(asignaciones);
        }
        public static Estado vacio() { return new Estado(List.of(), List.of(), List.of()); }
    }
}
