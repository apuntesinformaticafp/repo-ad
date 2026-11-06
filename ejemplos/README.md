# Código ejecutable de los apuntes de AD

Las unidades se reorganizan al prepararlas. Esta carpeta conserva el material
previo de UD02; UD01 tiene ejemplos y práctica dentro de su propia carpeta.

## UD1: acceso a ficheros

Consulta la [guía de ejemplos de UD1](../ud01/ejemplos/README.md). Desde la raíz
del repositorio, ejecuta por ejemplo:

```sh
mvn -f ud01/ejemplos/pom.xml -q compile exec:java -Dexec.mainClass=ejemplos.Rutas
python3 -B ud01/ejemplos/python/rutas.py
```

## UD2: bases de datos

Consulta la [guía de ejemplos de UD2](ud02-bases-datos/README.md). Los ejemplos
usan SQLite por defecto; el devcontainer prepara PostgreSQL para practicar la
conexión independiente.

```sh
mvn -f ejemplos/pom.xml -q compile exec:java -Dexec.mainClass=ejemplos.bd.Modelos
python3 -B ejemplos/ud02-bases-datos/python/modelos.py
```

Los ejemplos son formativos, no entregas. Las instrucciones de las prácticas
están en [UD01](../ud01/enunciado.md) y [UD02](../docs/ud02.md).
