# Laboratorio — ¿Conectar significa tener la base preparada?

Este laboratorio es formativo. Utiliza solo empresa y tutor para observar una
idea cada vez; no resuelve las cuatro tablas ni los métodos de la práctica FCT.
No hay entrega ni calificación. Guarda tus experimentos en esta carpeta si te ayudan.

## 1. Predecir y ejecutar

Desde la raíz del repositorio:

```bash
python3 -B ud02/para_jugar/laboratorio.py
```

Antes de ejecutarlo, predice: ¿crear dos veces borra la empresa?, ¿se admiten
`None`, `""` y `"   "` como nombre?, ¿puede existir un tutor de E99?, ¿qué ocurre
al reabrir el archivo? El programa muestra las respuestas y termina con error
si una expectativa no se cumple. Solo crea una carpeta temporal y la elimina.

## 2. Cambiar una condición

Haz un cambio cada vez y vuelve después al estado inicial:

- Quita `CHECK`: la cadena vacía deja de rechazarse y falla la expectativa.
- Quita `NOT NULL`: `NULL` no queda prohibido por ese `CHECK`; no son equivalentes.
- Desactiva `foreign_keys` en `abrir`: el tutor huérfano deja de rechazarse.
- Explica por qué cerrar la conexión no borra el archivo; es la limpieza del
  directorio temporal al terminar lo que lo elimina.

Las inserciones son sondas de restricciones, no el CRUD que desarrollarás en UD03.
Python usa `with conexion` para confirmar/revertir; `closing` realiza el cierre.

## 3. Leer metadatos en JDBC

```bash
mvn -f ud02/ejemplos/pom.xml compile exec:java -Dexec.mainClass=ejemplos.bd.Metadatos
```

Compara la salida con `PRAGMA table_info` y `foreign_key_list` del laboratorio.
Localiza nombre, tipo, nulabilidad, PK y FK. Cambia en tu copia formativa el tipo
de `empresa.nombre` a `INTEGER`: la tabla sigue existiendo, pero el metadato
cambia. Explica por qué comprobar solo el nombre de las tablas sería insuficiente.

En PostgreSQL hay que limitar la consulta al esquema de trabajo. Algunos métodos
de `DatabaseMetaData`, como `getColumns`, reciben **patrones**: `_` y `%` son
comodines; se escapan con `getSearchStringEscape()`. `getPrimaryKeys` y
`getImportedKeys` reciben nombres exactos de esquema y tabla. Consulta la
[API de Java 21](https://docs.oracle.com/en/java/javase/21/docs/api/java.sql/java/sql/DatabaseMetaData.html).

## 4. Pasar a FCT

Relaciona cada experimento con [el encargo](../enunciado.md): abrir, crear,
comprobar y reiniciar tienen efectos diferentes. En FCT hay cuatro tablas,
una FK compuesta y dos gestores. El DDL y los casos de aceptación ya están
proporcionados: no necesitas diseñar otro dominio ni escribir un ORM.
