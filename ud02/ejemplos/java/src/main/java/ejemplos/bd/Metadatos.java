package ejemplos.bd;

/** Inspección pequeña: muestra metadatos; no implementa el verificador de FCT. */
public class Metadatos {
    public static void main(String[] args) throws Exception {
        try (var c = Esquema.abrir("jdbc:sqlite::memory:")) {
            Esquema.crear(c);
            var meta = c.getMetaData();
            for (String tabla : new String[]{"empresa", "tutor"}) {
                System.out.println("Tabla: " + tabla);
                try (var columnas = meta.getColumns(null, null, tabla, "%")) {
                    while (columnas.next()) {
                        System.out.printf("  %s: %s, NULLABLE=%d%n",
                                columnas.getString("COLUMN_NAME"), columnas.getString("TYPE_NAME"),
                                columnas.getInt("NULLABLE"));
                    }
                }
                try (var claves = meta.getPrimaryKeys(null, null, tabla)) {
                    while (claves.next()) System.out.println("  PK: " + claves.getString("COLUMN_NAME"));
                }
                try (var claves = meta.getImportedKeys(null, null, tabla)) {
                    while (claves.next()) {
                        System.out.printf("  FK: %s -> %s.%s (posición %d)%n",
                                claves.getString("FKCOLUMN_NAME"), claves.getString("PKTABLE_NAME"),
                                claves.getString("PKCOLUMN_NAME"), claves.getInt("KEY_SEQ"));
                    }
                }
            }
        }
    }
}
