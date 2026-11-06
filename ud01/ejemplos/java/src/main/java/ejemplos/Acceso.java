package ejemplos;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Acceso {
    public static void main(String[] args) {
        String texto = "cañón";
        System.out.println("Unidades UTF-16: " + texto.length());
        System.out.println("Puntos de código: " + texto.codePointCount(0, texto.length()));
        System.out.println("Bytes UTF-8: " + texto.getBytes(StandardCharsets.UTF_8).length);
        System.out.println("Cuarto registro de 16 bytes: " + 3 * 16);
        var registros = ByteBuffer.wrap("O01\nO02\nO03\n".getBytes(StandardCharsets.US_ASCII));
        registros.position(2 * 4);
        byte[] registro = new byte[4];
        registros.get(registro);
        System.out.print(new String(registro, StandardCharsets.US_ASCII));
    }
}
