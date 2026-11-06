package fct;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Reemplazo de un fichero en el mismo sistema de archivos, no transacción multifichero. */
public final class EscrituraSegura {
    private EscrituraSegura() {}
    public static void reemplazar(Path destino, String texto, boolean simularFallo) throws IOException {
        destino = destino.toAbsolutePath();
        Files.createDirectories(destino.getParent());
        Path temporal = Files.createTempFile(destino.getParent(), "fct-", ".tmp");
        try {
            Files.writeString(temporal, texto, StandardCharsets.UTF_8);
            if (simularFallo) throw new IOException("Fallo simulado antes del reemplazo");
            // Si no está soportado, se propaga AtomicMoveNotSupportedException.
            Files.move(temporal, destino, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporal);
        }
    }
}
