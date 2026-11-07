# Práctica 2. Conexión y estructura relacional FCT

Consulta la [rúbrica de UD02](rubrica.md) antes de empezar.

Continúa en tu **mismo repositorio privado**. Incorpora las novedades con `upstream`,
conservando la entrega de UD1. Aquí están el contrato técnico, los comandos y la
aceptación; la rúbrica de autoevaluación está en el archivo enlazado arriba.

## Alcance y correspondencia con UD1

Java 21 + JDBC, SQLite embebido y PostgreSQL 17 independiente. La fábrica de
conexiones y la preparación del esquema aún **no son un repositorio CRUD**.
El dominio de UD01 se conserva. Su proyecto de ficheros sigue en `ud01/practica/`;
el de esta unidad está en `ud02/practica/` y no depende de resolver el anterior.
No importamos automáticamente el JSON ni modificamos las operaciones del servicio.

| Record de UD1 | Tabla de UD2 | Campos |
| --- | --- | --- |
| `Empresa` | `empresa` | `id`, `nombre` |
| `Estudiante` | `estudiante` | `id`, `nombre`, `ciclo` |
| `Tutor` dentro de empresa | `tutor` | `id`, `nombre`, `empresa_id` |
| `Asignacion` | `asignacion` | `id`, `estudiante_id`, `empresa_id`, `tutor_id`, `inicio`, `fin` |

El convenio es un documento de la asignación, no otra relación persistida. No
añadas ofertas, plazas o documentos de seguimiento todavía. La PK conserva
los identificadores internos de UD1; la colección de tutores pasa a varias filas.

## Qué tienes preparado y qué completas

Proporcionados: [configuración validada](practica/src/main/java/fct/bd/ConfiguracionBD.java),
[contrato de preparación](practica/src/main/java/fct/bd/PreparacionRelacional.java),
[cliente de consola](practica/src/main/java/fct/bd/DemoConexion.java),
[DDL por gestor](practica/src/main/resources/sql/ud02) y pruebas.

Completa **cuatro métodos de dos clases**: `abrir` en
[FabricaConexiones](practica/src/main/java/fct/bd/FabricaConexiones.java), y
`crear`, `comprobar`, `reiniciar` en
[EsquemaJdbc](practica/src/main/java/fct/bd/EsquemaJdbc.java):

1. Abrir una conexión JDBC con las propiedades externas y cerrarla desde el cliente.
2. Activar las FK de SQLite en **cada** conexión.
3. Crear las cuatro tablas, sin perder filas si ya existen correctamente.
4. Comprobar columnas, tipos, nulabilidad, PK y FK mediante metadatos.
5. Verificar restricciones con los casos sintéticos proporcionados.
6. Rechazar una preparación incompleta o con columnas erróneas.
7. Reinicializar solo nuestras tablas, con entorno de pruebas y confirmación;
   conservar otras tablas. No usar `DROP DATABASE` ni borrados en cascada.

`CREATE TABLE IF NOT EXISTS`
no repara una tabla incorrecta: la comprobación debe detectarla.

Trabaja por pasos: conexión → creación → comprobación → reinicio. Usa
`Connection.getMetaData()` para inspeccionar columnas, PK y FK; el
[laboratorio](para_jugar/README.md) prepara esa lectura con dos tablas.
La comprobación estructural no inserta ni borra datos. Los casos proporcionados
verifican el comportamiento de `CHECK`, `UNIQUE` y FK en destinos de pruebas.
No tienes que construir un comparador universal de esquemas ni un migrador.

En PostgreSQL, `crear` prepara primero el esquema indicado y lo selecciona;
las demás operaciones lo seleccionan antes de acceder a nuestras tablas.
Los archivos SQL son guiones propios simples: puedes separarlos por `;`,
sin convertir ese recurso en un parser de SQL general.

**Tipos y límites:** PostgreSQL usa `DATE`; SQLite usa `TEXT` con fechas ISO
`AAAA-MM-DD`. Su comparación lexicográfica comprueba el orden de fechas válidas,
no valida por sí sola todo el calendario. La validación Java del dominio sigue
siendo necesaria. La FK compuesta `(tutor_id, empresa_id)` exige que el tutor
pertenezca a la empresa de la asignación. `UNIQUE(id, empresa_id)` habilita esa referencia.

## Entorno y configuración

Reconstruye Codespaces después de sincronizar la UD2. El contenedor añade un
servicio PostgreSQL disponible como host `db`; no se publica a Internet.
`FCT_JDBC_URL` es obligatoria. Java recibe variables de entorno, **no lee `.env`
automáticamente**. Credenciales en variables; no en fuentes, URL, logs o commits.

SQLite (desde la raíz; `datos-demo/` está ignorado):

```bash
mkdir -p datos-demo
export FCT_JDBC_URL='jdbc:sqlite:datos-demo/ud02.sqlite'
export FCT_ENTORNO='pruebas'
python3 ud02/practica/preparar_bd.py crear
python3 ud02/practica/preparar_bd.py comprobar
python3 ud02/practica/preparar_bd.py reiniciar --confirmar-pruebas
```

PostgreSQL en Codespaces:

```bash
export FCT_JDBC_URL='jdbc:postgresql://db:5432/fct_pruebas'
export FCT_DB_USER='fct'
export FCT_DB_PASSWORD='fct_laboratorio'
export FCT_DB_SCHEMA='fct_ud02'
export FCT_ENTORNO='pruebas'
python3 ud02/practica/preparar_bd.py crear
python3 ud02/practica/preparar_bd.py comprobar
```

`fct_laboratorio` es una contraseña ficticia **solo del contenedor didáctico**.
En Docker local, arranca `docker compose -f .devcontainer/compose.yaml up -d db`
y cambia el endpoint a `localhost:55432`, el puerto del laboratorio compartido.

El namespace es `fct_ud02` o `fct_ud02_<identificador>`; se valida antes de usarlo
como identificador SQL. No se prepara ni reinicia `public`. SQLite usa un archivo
dedicado a pruebas: la confirmación no detecta por sí sola un archivo mal elegido.
No cambies el entorno a `pruebas` para operar sobre datos ajenos.

El guion compila desde limpio y propaga el código de salida de Java.

## Pruebas por fase

```bash
# Base proporcionada: debe pasar antes de implementar.
mvn -B -f ud02/practica/pom.xml clean test
# Entrega de UD1: conserva la aceptación de ficheros, no exige UD2 terminada.
mvn -B -f ud01/practica/pom.xml -Pcontrato clean verify
# Trabajo de UD2: SQLite; al principio falla por métodos pendientes.
mvn -B -f ud02/practica/pom.xml -Pcontrato-ud02 clean verify
# Trabajo de UD2: ambos gestores, con PostgreSQL configurado y arrancado.
mvn -B -f ud02/practica/pom.xml -Pcontrato-ud02,postgresql clean verify
```

El perfil `postgresql` **falla** si no está configurado o si el servicio no
responde; no sustituye el gestor por SQLite ni omite la prueba silenciosamente.
Las pruebas crean un namespace único y lo retiran al terminar. Verifican estructura,
preservación, FK, fechas, nulabilidad, reinicialización y contraseña inválida.
Las inserciones y consultas de esas pruebas son sondas del esquema, no operaciones
de negocio que tengas que implementar ahora. El pool se experimenta en los ejemplos
públicos; no es necesario añadirlo a esta fábrica de conexiones.

## Cierre y demostración

Crea `ud02/practica.md` siguiendo la ficha de abajo.
Conserva decisiones, comandos, resultados esperados/observados y autoevaluación.
Código, demostración y documentación son **una evidencia**, no tres entregas.

Guion de demostración sobre trabajo:

1. Crear y comprobar el esquema en SQLite; cerrar y comprobar desde otra ejecución.
2. Repetir con PostgreSQL y explicar proceso, endpoint y controlador.
3. Mostrar un rechazo de credencial; corregirlo y volver a conectar.
4. Mostrar la aceptación de restricciones y detectar una tabla de prueba ausente.
5. Reinicializar con confirmación; verificar que una tabla ajena permanece.
6. Ejecutar la aceptación en ambos gestores y explicar un límite de la solución.

No necesitas una grabación ni otra batería de pruebas: identifica qué pruebas
demuestran cada caso y explica el resultado. Documenta también por qué JDBC es
una API, cuáles son los drivers elegidos y qué cambia respecto al JSON de UD01.

## Revisión y presentación

Aplica primero tú la rúbrica. Después puedes contrastar tu revisión con IA según
[AI con la IA](https://www.apuntesinformaticafp.com/varios/ai_con_la_ia.html): pide
preguntas, posibles errores y comprobaciones; verifica sus propuestas y decide.
Declara la ayuda significativa y qué comprobaste. Ese contraste es opcional.

Conserva fuentes y pruebas en `ud02/practica/`, y tu ficha en `ud02/practica.md`.
No entregues `target/`, bases generadas, contraseñas reales, capturas ni portadas.
No cambies las pruebas de aceptación para hacerlas pasar; experimenta en el laboratorio.

**La corrección comienza cuando publiques `practica-ad-02-v1` sobre el commit
que quieres presentar.** Los commits y subidas ordinarias no son presentaciones.
Las revisiones usan `practica-ad-02-v2`, etc., sin mover ni borrar etiquetas
anteriores. Sigue la [0-B](https://www.apuntesinformaticafp.com/actividades/preparacion/repositorio-practicas.html).

## Ficha para `practica.md`

```markdown
# Práctica 2. Conexión y estructura relacional FCT

## Ejecución
- Comandos y versiones:
- Destinos SQLite y PostgreSQL, sin secretos:
- Resultado de la base y de la aceptación en cada gestor:

## Comprobaciones
| Caso | Resultado esperado | Resultado observado y prueba/archivo |
|---|---|---|
| Crear dos veces y reabrir | | |
| Conexión válida y cierre | | |
| Credencial incorrecta y recuperación | | |
| FK, pareja tutor/empresa y fechas | | |
| Tabla ausente o columnas incorrectas | | |
| Reinicio rechazado sin confirmación | | |
| Reinicio confirmado conserva tabla ajena | | |

## Decisiones y límites
- Qué conserva el dominio de UD01 y qué cambia al representarlo en tablas:
- Archivo/proceso/servicio: diferencias y un límite de cada gestor:
- API y conectores elegidos; dependencias y configuración externa:
- Quién cierra las conexiones y qué información uso para diagnosticar:
- Por qué la FK compuesta y qué comprueban los tipos/fechas en cada gestor:
- Diferencia entre crear, comprobar y reiniciar; aspectos pendientes:

## Mi revisión
| Criterio de la rúbrica | Nivel estimado | Evidencia |
|---|---|---|
| Gestores y preparación | | |
| Conexión desde la aplicación | | |
| Esquema relacional | | |

- Qué detecté y mejoré:
- Ayuda significativa utilizada y comprobada (o ninguna):
- Contraste opcional con IA: observación aceptada/descartada y motivo.
```
