# Acceso a Datos — Prácticas

Materiales y actividades del módulo AD.

Consulta los [apuntes y materiales del módulo AD](https://www.apuntesinformaticafp.com/modulos/ad.html).

## Contenido

- **ud00/**: [entrevista inicial](ud00/entrevista.md) para conocer tu punto de partida.
- **ud01/**: [acceso a ficheros](ud01/README.md), con ejemplos, laboratorio y práctica FCT.
- **ud02/**: [acceso a bases de datos](ud02/README.md), con ejemplos, laboratorio y preparación relacional FCT.

## Cómo trabajar

Realiza las actividades en tu repositorio privado de prácticas.

Para preparar tu repositorio, sigue la
[práctica 0-B](https://www.apuntesinformaticafp.com/actividades/preparacion/repositorio-practicas.html).
El [entorno común de AD](.devcontainer/README.md) prepara Java, Maven y Python en
Codespaces. El código fuente se conserva en Git; Maven genera la compilación y
las pruebas bajo `target/`, que no se entrega.

UD01 y UD02 forman el bloque de la primera evaluación: primero conservamos el
estado en ficheros; después preparamos conexiones y estructura relacional.
Cada proyecto tiene su POM y sus comprobaciones. El CRUD relacional llegará en
UD03; preparar las tablas no importa automáticamente el JSON de UD01.

## Organización

Todo el material didáctico vive dentro de su `udNN/`. Cada unidad reúne sus
enunciados, rúbricas, ejemplos, laboratorios y proyectos:

```text
README.md
.gitignore
.devcontainer/          # Entorno común y comprobación inicial
.github/               # Comprobaciones automáticas
ud00/
  entrevista.md
ud01/
  README.md
  enunciado.md
  rubrica.md
  ejemplos/
  para_jugar/
  practica/
ud02/
  README.md
  enunciado.md
  rubrica.md
  ejemplos/
  para_jugar/
  practica/
```

`ejemplos/` y `para_jugar/` son material formativo; `practica/` contiene la base
del trabajo que completas. Los enunciados indican cómo presentar cada unidad.
Los comandos se ejecutan desde la raíz, seleccionando el proyecto con
`mvn -f udNN/practica/pom.xml ...`; no hay un proyecto Maven en la raíz.

En la raíz solo se mantienen este índice, `.gitignore` y la configuración común.
Los resultados ignorados, como `target/`, `datos-demo/` o `__pycache__/`, no forman
parte del material distribuido aunque existan en tu copia local.
