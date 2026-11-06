package fct;

import fct.Modelo.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/** Reglas del dominio, independientes del formato de persistencia o intercambio. */
public final class Validacion {
    private Validacion() {}
    private static void texto(String valor) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException("Campo obligatorio vacío");
    }
    private static void unico(Set<String> ids, String id) {
        texto(id);
        if (!ids.add(id)) throw new IllegalArgumentException("Identificador duplicado: " + id);
    }

    public static void validar(Estado estado) {
        if (estado == null) throw new IllegalArgumentException("Estado nulo");
        var empresas = new HashSet<String>();
        var estudiantes = new HashSet<String>();
        var tutores = new HashMap<String, String>();
        for (Empresa empresa : estado.empresas()) {
            unico(empresas, empresa.id());
            texto(empresa.nombre());
            for (Tutor tutor : empresa.tutores()) {
                texto(tutor.id());
                texto(tutor.nombre());
                if (tutores.putIfAbsent(tutor.id(), empresa.id()) != null)
                    throw new IllegalArgumentException("Tutor duplicado: " + tutor.id());
            }
        }
        for (Estudiante estudiante : estado.estudiantes()) {
            unico(estudiantes, estudiante.id());
            texto(estudiante.nombre());
            texto(estudiante.ciclo());
        }
        var asignaciones = new HashSet<String>();
        for (Asignacion asignacion : estado.asignaciones()) {
            unico(asignaciones, asignacion.id());
            texto(asignacion.empresaId());
            texto(asignacion.estudianteId());
            texto(asignacion.tutorId());
            if (!estudiantes.contains(asignacion.estudianteId())
                    || !empresas.contains(asignacion.empresaId())
                    || !asignacion.empresaId().equals(tutores.get(asignacion.tutorId())))
                throw new IllegalArgumentException("Referencia de asignación inválida");
            texto(asignacion.inicio());
            texto(asignacion.fin());
            if (LocalDate.parse(asignacion.fin()).isBefore(LocalDate.parse(asignacion.inicio())))
                throw new IllegalArgumentException("La fecha final precede a la inicial");
        }
    }
}
