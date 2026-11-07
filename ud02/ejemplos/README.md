# Ejemplos pequeños de UD02

Ejemplos formativos. Cada programa aísla una idea; la aplicación FCT y su
evidencia están en `ud02/practica/` y `ud02/practica.md`. Ejecuta los comandos desde la
**raíz del repositorio completo**.
Java usa JDBC; Python usa `sqlite3`, no JDBC. Ambos ilustran los mismos conceptos.

## Leer, predecir, ejecutar y cambiar

```bash
mvn -q -f ud02/ejemplos/pom.xml compile exec:java -Dexec.mainClass=ejemplos.bd.Modelos
python3 -B ud02/ejemplos/python/modelos.py
```

Cambia el nombre final de clase para ejecutar el programa correspondiente:

| Clase web | Programa Java | Resultado esperado / cambio propuesto |
| --- | --- | --- |
| Del objeto a la base relacional | [Modelos](java/src/main/java/ejemplos/bd/Modelos.java) | Instancia distinta, valor igual y dos filas de tutores. Añade un tutor. |
| Gestores embebidos e independientes | [Gestores](java/src/main/java/ejemplos/bd/Gestores.java) | SQLite y tabla presente al reabrir. Contrasta con `Conexion` en PostgreSQL. |
| Elegir y preparar el conector | [Conectores](java/src/main/java/ejemplos/bd/Conectores.java) | Dos drivers compatibles; no requiere arrancar PostgreSQL. |
| Abrir y comprobar conexiones | [Conexion](java/src/main/java/ejemplos/bd/Conexion.java) | Conexión válida y cerrada; repite con un endpoint incorrecto. |
| Configuración externa | [Configuracion](java/src/main/java/ejemplos/bd/Configuracion.java) | Cambia el gestor con variables, sin recompilar; nunca imprime secretos. |
| Agrupación de conexiones | [Pool](java/src/main/java/ejemplos/bd/Pool.java) | Agotamiento con un préstamo, recuperación tras devolver. |
| Diagnosticar fallos | [Diagnostico](java/src/main/java/ejemplos/bd/Diagnostico.java) | Separa protocolo sin driver de archivo que no puede abrirse. |
| Traducir el dominio | [Esquema](java/src/main/java/ejemplos/bd/Esquema.java) | Empresa y tutor: una FK en el lado muchos. Dibuja su correspondencia. |
| Crear tablas y restricciones | [InfraestructuraTest](java/src/test/java/ejemplos/bd/InfraestructuraTest.java) | El gestor rechaza al tutor huérfano; compara con quitar la FK. |
| Inicialización reproducible | [Reproducible](java/src/main/java/ejemplos/bd/Reproducible.java) | Creación repetida, detección de tabla ausente y reparación. |
| Comprobación del esquema | [Metadatos](java/src/main/java/ejemplos/bd/Metadatos.java) | Nombres, tipos, nulabilidad, PK y FK: leer antes de comparar. |

Python: [conexión](python/conexion.py), [configuración](python/configuracion.py)
y [esquema](python/esquema.py). `with sqlite3.connect(...)` gestiona la transacción,
pero **no cierra la conexión**: usamos `contextlib.closing` para ese cierre.
Las pequeñas inserciones de prueba verifican restricciones; no son un CRUD de negocio.

## SQLite y PostgreSQL

Sin variables, los ejemplos Java de conexión usan SQLite en memoria. `Gestores`
crea un archivo temporal para demostrar persistencia y lo elimina al terminar.

En Codespaces, después de incorporar esta UD, reconstruye el contenedor para
arrancar el servicio preparado. El host del servidor es `db`, no `localhost`:

```bash
export FCT_JDBC_URL='jdbc:postgresql://db:5432/fct_pruebas'
export FCT_DB_USER='fct'
export FCT_DB_PASSWORD='fct_laboratorio'
mvn -q -f ud02/ejemplos/pom.xml compile exec:java -Dexec.mainClass=ejemplos.bd.Conexion
```

La contraseña publicada es **ficticia y exclusiva del contenedor de laboratorio**.
En un servicio propio utiliza sus parámetros reales fuera de Git. SQLite no usa
autenticación por usuario/contraseña del servidor: el acceso depende del archivo.

Con Docker Compose local, arranca `docker compose -f .devcontainer/compose.yaml up -d db`
y usa `localhost:55432` en la URL JDBC. `docker compose -f .devcontainer/compose.yaml stop db`
detiene solo este servicio; repite la conexión para observar el error y vuelve a
arrancarlo con el comando anterior. No cambies los permisos globales de Docker.

## Comprobaciones y continuación

```bash
mvn -B -f ud02/ejemplos/pom.xml clean test
python3 -B -m unittest discover -s ud02/ejemplos/python -p 'test_*.py'
```

Las pruebas Java automáticas de los ejemplos son SQLite. La comprobación de
PostgreSQL se ejecuta explícitamente con `Conexion`; la aceptación del esquema
FCT en los dos gestores está en [la práctica](../enunciado.md).

Continúa con el [laboratorio](../para_jugar/README.md) para experimentar con
metadatos y restricciones sin depender de tener terminada la práctica.

## Referencias de las herramientas

- [JDBC: conexión y cierre](https://docs.oracle.com/javase/tutorial/jdbc/basics/connecting.html).
- [SQLite JDBC (Xerial)](https://github.com/xerial/sqlite-jdbc): Apache-2.0/BSD-2-Clause.
- [pgJDBC](https://jdbc.postgresql.org/documentation/use/): licencia BSD de PostgreSQL.
- [HikariCP](https://github.com/brettwooldridge/HikariCP): Apache-2.0, Java 11 o posterior.

Las versiones reproducibles están fijadas en el POM; no hay que seguir las
versiones cambiantes de los ejemplos de terceros.
