# Práctica 1. Primera persistencia del caso FCT

## El encargo

El centro necesita conservar los datos de estudiantes, empresas, tutores y
asignaciones de su simulador FCT entre ejecuciones. Completa el almacenamiento
en **un único JSON** y genera un **CSV de asignaciones** para consultar el listado.

Trabaja con el [caso FCT](https://www.apuntesinformaticafp.com/actividades/ad/caso_fct.html)
y lo aprendido en [Acceso a ficheros](https://www.apuntesinformaticafp.com/cursos/acceso_a_ficheros.html).
Puedes adaptar los ejemplos y los cambios que hayas probado en las actividades.
Consulta la [rúbrica](rubrica.md) antes de empezar y al revisar.

## Qué tienes preparado

El proyecto está en `ud01/practica/`. Incluye:

- `Modelo`: empresas, tutores, estudiantes, asignaciones y estado completo.
- `AlmacenFct`: contrato con `guardar(Estado)` y `recuperar()`.
- `ServicioFct`: altas, modificaciones, búsquedas, filtro por ciclo y asignaciones.
- `Validacion`: identificadores, campos, relaciones y fechas.
- `EscrituraSegura`: temporal junto al destino, reemplazo atómico y limpieza.
- `DemoFct`, datos ficticios y pruebas de aceptación.

Completa **tres métodos**, sin reconstruir la aplicación. Java 21 y Maven están
preparados en el [entorno de AD](../.devcontainer/README.md). Los ejemplos completos
están en [ejemplos/](ejemplos/README.md) y el laboratorio en
[para_jugar/](para_jugar/README.md). XML se trabajará en la última unidad.

## 1. Recuperar el estado JSON

Completa `RepositorioJson.recuperar()` en `practica/src/main/java/fct/`:

- Lee UTF-8, deserializa a `Estado` con Jackson y aplica `Validacion.validar`.
- Conserva valores y relaciones; una instancia nueva debe recuperar lo guardado.
- Propaga los errores: fichero ausente, cero bytes o documento incompatible no
  se convierten en un estado vacío. Este se inicia guardando `Estado.vacio()`.

La lectura y el binding de Jackson pueden lanzar `IOException` (también para
`{}`, que no cumple la estructura). Las reglas de dominio se comprueban después;
por ejemplo, el valor JSON `null` es rechazado por `Validacion` con
`IllegalArgumentException`. No hace falta inventar una jerarquía de excepciones.

**Referencia:** ejemplo `JsonObjetos`, actividad de JSON y tabla de estados del
fichero de la clase «Probar y documentar la persistencia».

## 2. Guardar y conservar el fichero válido

Completa `RepositorioJson.guardar(Estado)`:

1. Valida el estado.
2. Serializa con Jackson.
3. Llama a `EscrituraSegura.reemplazar(ruta, texto, simularFallo)`.
4. Solo después del éxito, notifica exactamente **`"estado guardado"`**.

El fallo simulado ocurre después de escribir el temporal y antes de reemplazar:
deben conservarse los bytes anteriores, limpiarse el temporal y no emitirse aviso
de éxito. El notificador proporcionado termina sin lanzar excepciones.

Lee el helper y explica su garantía: protege el reemplazo de un fichero cuando
el sistema lo soporta. No ofrece transacciones entre ficheros, concurrencia ni
recuperación ante cualquier corte de alimentación. Si el movimiento atómico no
se admite, propaga el error; no lo sustituyas por escritura directa.

**Referencia:** ejemplos `Errores` y `Componente`, y laboratorio de empresas.

## 3. Exportar asignaciones a CSV

Completa `Intercambio.exportarAsignacionesCsv(Estado, Path)`:

- Valida el estado y usa Commons CSV, con UTF-8 y delimitador `;`.
- Escribe la cabecera exacta `nombre;ciclo;empresa`.
- Genera una fila por asignación, en el orden de la lista de asignaciones.
- Resuelve sus identificadores para obtener nombre y ciclo del estudiante y
  **nombre de la empresa**. No exportes solo sus identificadores.
- Conserva comillas, delimitadores y saltos de línea de los nombres. Sin
  asignaciones, el CSV contiene únicamente la cabecera.

Este CSV es una proyección: no permite reconstruir tutores, fechas ni el estado
completo. No necesitas programar un importador CSV ni un parser propio.

**Referencia:** ejemplos `CsvEscritura` y `Conversion`.

## 4. Comprobar y explicar

Desde la raíz del repositorio:

```bash
mvn -f ud01/practica/pom.xml test
mvn -f ud01/practica/pom.xml -Pcontrato clean verify
mvn -f ud01/practica/pom.xml compile exec:java -Dexec.mainClass=fct.DemoFct
```

El primer comando comprueba la base proporcionada; puede pasar aunque los tres
métodos estén pendientes. El segundo comprueba tu implementación y fallará hasta
completarla. La demo se ejecuta después y escribe en `ud01/practica/datos-demo/`.

Las pruebas de aceptación comprueban relectura, estado vacío, operaciones del
servicio, rechazo de datos inválidos, CSV y conservación ante fallo. Incluyen
copiar el JSON, mover la copia, recuperarla desde otra ruta y borrarla sin alterar
el original. Usa esas pruebas para explicar las operaciones con rutas.

Registra en `ud01/practica.md` un caso normal, la relectura CSV y un caso de error
con resultado esperado y observado. Señala también la prueba de copia/movimiento,
la separación entre servicio y almacén, y para qué sirven las herramientas usadas.
No se exige otra batería de pruebas ni una grabación: comandos, pruebas y una
explicación que permita reproducir el resultado forman la demostración.

## Revisa antes de presentar

Aplica primero tú la rúbrica, comprueba el código y mejora lo que detectes.
Después puedes contrastar tu revisión con tu «compañero virtual»: pídele que
señale posibles errores, formule preguntas y sugiera comprobaciones. Verifica sus
observaciones y decide cuáles aceptar o descartar. Vuelve a ejecutar las pruebas.

Mi planteamiento está en [AI con la IA](https://www.apuntesinformaticafp.com/varios/ai_con_la_ia.html).
El contraste con IA es opcional; no aporta puntos por sí mismo. Declara brevemente
la ayuda significativa utilizada y qué comprobaste, sin adjuntar conversaciones.

## Cómo presentar

El código fuente, datos de entrada y pruebas están organizados dentro de
**`ud01/practica/`**. Crea **`ud01/practica.md`** con la ficha siguiente. No copies
código allí ni entregues `target/`, ejecutables, portadas o capturas. Conserva el
enunciado, la rúbrica y las pruebas de aceptación proporcionadas.

Puedes guardar, subir y mejorar tu trabajo tantas veces como necesites. **La
corrección comienza cuando publiques `practica-ad-01-v1` sobre el commit que quieres
presentar.** Subir cambios a la rama no constituye una presentación. Las revisiones
usan `practica-ad-01-v2`, etc., sin mover ni borrar las etiquetas anteriores.
Sigue el procedimiento de la [0-B](https://www.apuntesinformaticafp.com/actividades/preparacion/repositorio-practicas.html).

## Ficha para `practica.md`

```markdown
# Práctica 1. Primera persistencia del caso FCT

## Ejecución
- Comandos utilizados y configuración necesaria:
- Resultado de la aceptación y de la demo:

## Comprobaciones
| Caso | Resultado esperado | Resultado observado y prueba/archivo |
|---|---|---|
| Guardar y recuperar JSON | | |
| Exportar y releer CSV | | |
| Fallo controlado al guardar | | |

- Copia, movimiento y borrado: qué prueba lo demuestra y por qué no altera el original.

## Decisiones y límites
- Qué hace el servicio y qué hace el almacén:
- Una decisión de mi implementación y su motivo:
- Qué garantiza el guardado y qué queda fuera:
- Para qué utilicé Java/Maven, Jackson, Commons CSV y JUnit:
- Aspectos pendientes, si los hay:

## Mi revisión
| Criterio de la rúbrica | Nivel estimado | Evidencia |
|---|---|---|
| Gestión de ficheros | | |
| Recuperación JSON | | |
| Almacenamiento e intercambio | | |
| Comprobación y documentación | | |
| Diseño del componente | | |
| Herramientas | | |
| Integración del componente | | |

- Qué detecté y mejoré al revisar:
- Ayuda utilizada y comprobaciones realizadas (o ninguna):
- Contraste con IA, si lo utilicé: observación aceptada o descartada y motivo.
```
