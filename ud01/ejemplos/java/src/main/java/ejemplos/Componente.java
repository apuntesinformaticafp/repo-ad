package ejemplos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Componente {
    record Empresa(String id, String nombre) {}
    interface Empresas {
        void guardar(Empresa empresa);
        Optional<Empresa> buscar(String id);
    }
    static class EmpresasEnMemoria implements Empresas {
        private final Map<String, Empresa> datos = new HashMap<>();
        public void guardar(Empresa empresa) { datos.put(empresa.id(), empresa); }
        public Optional<Empresa> buscar(String id) { return Optional.ofNullable(datos.get(id)); }
    }
    public static void main(String[] args) {
        Empresas repositorio = new EmpresasEnMemoria();
        repositorio.guardar(new Empresa("E01", "Empresa A"));
        System.out.println(repositorio.buscar("E01"));
        System.out.println(repositorio.buscar("E99"));
    }
}
