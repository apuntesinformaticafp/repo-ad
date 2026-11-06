package fct;

import fct.Modelo.Estado;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

/** Completa esta implementación. Las operaciones pendientes no simulan éxito. */
public class RepositorioJson implements AlmacenFct {
    private final Path ruta;
    private final Consumer<String> notificar;
    private final boolean simularFallo;
    public RepositorioJson(Path ruta) { this(ruta, mensaje -> {}, false); }
    public RepositorioJson(Path ruta, Consumer<String> notificar, boolean simularFallo) {
        this.ruta = ruta;
        this.notificar = notificar;
        this.simularFallo = simularFallo;
    }
    public Estado recuperar() throws IOException {
        // TODO: leer UTF-8, deserializar Estado, validar y propagar errores.
        throw new UnsupportedOperationException("Implementar recuperación JSON");
    }
    public void guardar(Estado estado) throws IOException {
        // TODO: validar, serializar y llamar a EscrituraSegura.reemplazar.
        // Notificar "estado guardado" únicamente después del reemplazo correcto.
        throw new UnsupportedOperationException("Implementar almacenamiento JSON");
    }
}
