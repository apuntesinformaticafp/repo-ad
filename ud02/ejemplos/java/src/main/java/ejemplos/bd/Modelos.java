package ejemplos.bd;

import java.util.List;

/** Un objeto con una colección necesita varias filas relacionadas. */
public class Modelos {
    record Tutor(String id, String nombre) {}
    record Empresa(String id, String nombre, List<Tutor> tutores) {}

    public static void main(String[] args) {
        var empresa = new Empresa("E01", "Empresa A", List.of(
                new Tutor("T01", "Tutor A"), new Tutor("T02", "Tutor B")));
        var otraInstancia = new Empresa(empresa.id(), empresa.nombre(), empresa.tutores());
        System.out.println("Misma instancia: " + (empresa == otraInstancia));
        System.out.println("Mismo valor: " + empresa.equals(otraInstancia));
        System.out.println("empresa: " + empresa.id() + ", " + empresa.nombre());
        for (var tutor : empresa.tutores()) {
            System.out.println("tutor: " + tutor.id() + ", " + tutor.nombre() + ", " + empresa.id());
        }
    }
}
