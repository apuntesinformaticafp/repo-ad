# Entorno de desarrollo de AD

Codespaces es una máquina Linux remota donde trabajas con archivos, terminal,
editor y herramientas como en tu equipo. El **devcontainer** describe cómo
preparar esas herramientas para que todos partamos del mismo entorno.

## Qué prepara

- Ubuntu 24.04, Git y GitHub CLI.
- Java 21 (Temurin), Maven 3.9.9 y Python 3.12.
- Extensiones de Java y Python para el editor.
- PostgreSQL 17 para [UD02](../ud02/README.md); UD01 no lo utiliza.

Los POM fijan las versiones de bibliotecas y plugins. Las imágenes/features
reciben revisiones dentro de las versiones indicadas; el entorno es actualizable,
no una imagen congelada byte a byte. Los cambios de versiones se revisan en Git.

## Primer arranque

Abre Codespaces desde tu copia privada preparada en la
[0-B](https://www.apuntesinformaticafp.com/actividades/preparacion/repositorio-practicas.html).
Sigue la [0-C](https://www.apuntesinformaticafp.com/actividades/preparacion/codespaces.html)
para su manejo. Al crear el entorno se ejecuta `comprobar.sh`, que comprueba las
bases y los ejemplos. No exige completar los métodos pendientes de las prácticas.

En la terminal, desde la raíz del repositorio:

```bash
java -version
mvn -version
python3 --version
mvn -f ud01/ejemplos/pom.xml compile exec:java -Dexec.mainClass=ejemplos.JsonObjetos
```

**Java sí se compila.** Maven hace ese trabajo al ejecutar los comandos: no
necesitas preparar ejecutables a mano. Conserva en Git código fuente, datos de
entrada, configuración y documentación; `target/` y `datos-demo/` son salidas
generadas y están ignoradas. Una comprobación automática no presenta la práctica.

## Actualizar el entorno

1. Guarda tus cambios y sincroniza `upstream` siguiendo la
   [práctica 0-B](https://www.apuntesinformaticafp.com/actividades/preparacion/repositorio-practicas.html), resolviendo los
   conflictos sin descartar tu código.
2. Si cambiaron `.devcontainer/` o las herramientas, usa la paleta de comandos:
   **Codespaces: Rebuild Container**.
3. Si solo cambiaron fuentes o POM, basta volver a ejecutar Maven.
4. Comprueba versiones y ejecuta `bash .devcontainer/comprobar.sh`. Si falla,
   lee qué proyecto y prueba fallaron: no significa necesariamente un fallo de instalación.

La reconstrucción conserva el repositorio en `/workspaces`; las instalaciones
manuales fuera de él pueden perderse. Por eso las herramientas comunes se
declaran aquí. Mantén tus preferencias personales fuera de esta configuración.

## Trabajar localmente

Puedes ejecutar los mismos comandos con Java 21, Maven y Python instalados, o
abrir el repositorio con VS Code, la extensión Dev Containers y Docker compatible
mediante **Dev Containers: Reopen in Container**. En local el proceso se ejecuta
en tu equipo; en Codespaces, en la máquina remota. Las rutas y archivos son los
del entorno donde está abierta la terminal.

## Comprobación del entorno por el docente

Tras actualizar la configuración: crear o reconstruir un Codespace, comprobar
que termina `comprobar.sh`, ejecutar `JsonObjetos`, `CsvEscritura` y ambos
laboratorios, y abrir los archivos generados. La aceptación de la práctica base
debe fallar por los métodos pendientes; eso es distinto de un error del entorno.

Para UD02, ejecuta `Conexion` con el servicio `db`, prueba una contraseña
incorrecta y restaura la configuración. Comprueba también `Metadatos` y el
laboratorio Python. La aceptación completa del esquema en ambos gestores se
ejecuta sobre una implementación terminada, siguiendo el enunciado de UD02.

Referencia: [contenedores de desarrollo en Codespaces](https://docs.github.com/en/codespaces/setting-up-your-project-for-codespaces/introduction-to-dev-containers).
