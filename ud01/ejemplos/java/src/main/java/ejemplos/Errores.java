package ejemplos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardCopyOption;

public class Errores {
    public static void escribirSeguro(Path destino, String texto, boolean fallo) throws IOException {
        Path temporal = Files.createTempFile(destino.toAbsolutePath().getParent(), "ad-", ".tmp");
        try {
            Files.writeString(temporal, texto, StandardCharsets.UTF_8);
            if (fallo) throw new IOException("Fallo antes del reemplazo");
            // Si ATOMIC_MOVE no está soportado, se propaga: no se degrada la garantía.
            Files.move(temporal, destino, StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporal);
        }
    }

    public static void main(String[] args) throws Exception {
        Path carpeta = Files.createTempDirectory("ad-errores-");
        Path fichero = carpeta.resolve("datos.txt");
        try {
            try {
                Files.readString(fichero);
            } catch (NoSuchFileException error) {
                System.out.println("El fichero no existe");
            }
            Files.writeString(fichero, "anterior");
            try {
                escribirSeguro(fichero, "nuevo", true);
            } catch (IOException error) {
                System.out.println(error.getMessage());
            }
            System.out.println(Files.readString(fichero));
        } finally {
            Files.deleteIfExists(fichero);
            Files.delete(carpeta);
        }
    }
}
