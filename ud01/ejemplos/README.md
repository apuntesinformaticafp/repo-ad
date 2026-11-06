# Ejemplos pequeños de UD01

**Predecir → ejecutar → cambiar una cosa → comprobar.** Java es la referencia de
la práctica; Python permite observar rápidamente los conceptos. No necesitas
repetir cada actividad en ambos lenguajes. Trabaja en tu copia privada, en
Codespaces o en local, desde la raíz del repositorio.

```bash
mvn -q -f ud01/ejemplos/pom.xml compile exec:java -Dexec.mainClass=ejemplos.Rutas
python3 -B ud01/ejemplos/python/rutas.py
```

Sustituye el nombre por el de la tabla. Maven compila y ejecuta; no tienes que
crear ni entregar ejecutables. Los ejemplos Java están en
`java/src/main/java/ejemplos/` y los Python en `python/`.

| Clase | Ejemplo Java / Python | Cambio pequeño | Qué comprobar |
|---|---|---|---|
| Rutas | `Rutas` / `rutas.py` | Añade un ciclo y repite la creación | Directorio de trabajo y árbol sin duplicados |
| Acceso | `Acceso` / `acceso.py` | Sustituye una letra por un emoji | Bytes, puntos de código y unidades UTF-16 |
| Flujos | `Flujos` / `flujos.py` | Cambia anexado por reemplazo | Qué nombres desaparecen |
| Formatos | `Formatos` / `formatos.py` | Añade una fila DAW | El filtro y el delimitador dentro del nombre |
| Objetos JSON | `JsonObjetos` | Añade otro tutor a `muestra()` | Relectura con los mismos valores, distinta instancia |
| Escritura CSV | `CsvEscritura` | Añade otro registro con comillas | Un registro no equivale a una línea física |
| Transformación | `Conversion` / `conversion.py` | Añade un tutor | El CSV de resumen no conserva tutores |
| Errores | `Errores` / `errores.py` | Desactiva el fallo simulado | `anterior` ante fallo; `nuevo` con éxito |
| Componente | `Componente` / `componente.py` | Busca otro identificador | Ausencia distinta de error de acceso |

`JsonObjetos` usa escritura directa para aislar el binding JSON. La escritura
segura se estudia después con `Errores` y el laboratorio.

## Pruebas

```bash
mvn -f ud01/ejemplos/pom.xml test
```

- `PersistenciaTest`: anexado, conservación y limpieza ante fallo, reemplazo por
  texto de cero bytes. Este último caso **no es** un documento JSON vacío válido.
- `FormatosTest`: ida y vuelta de objetos JSON desde disco, lista de tutores vacía
  frente a fichero de cero bytes, y relectura CSV con separadores, comillas y saltos.

Cambia una expectativa, observa el fallo y corrígela. Los archivos de estos
ejemplos son temporales y se eliminan al terminar. Para inspeccionar archivos
persistentes, usa [el laboratorio](../para_jugar/README.md).

Los antiguos ejercicios de ficheros de la web están integrados en este recorrido.
