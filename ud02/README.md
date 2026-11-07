# UD02 — Acceso a bases de datos

En UD01 conservaste el estado FCT en JSON y exportaste un listado CSV. Ahora
preparas **la conexión y la estructura relacional** del mismo dominio, con
SQLite y PostgreSQL. No hay que migrar datos ni implementar todavía el CRUD.

## Recorrido

1. Lee [los apuntes](https://www.apuntesinformaticafp.com/cursos/acceso_a_bd.html)
   y ejecuta los [ejemplos pequeños](ejemplos/README.md) en cada clase.
2. Experimenta con el [laboratorio](para_jugar/README.md): predice, cambia una
   condición y explica qué comprueba el gestor.
3. Completa la [práctica FCT](enunciado.md), revisa la [rúbrica](rubrica.md) y
   crea `ud02/practica.md` con la ficha del enunciado.

El [caso FCT](https://www.apuntesinformaticafp.com/actividades/ad/caso_fct.html#caso-fct-ud02)
conserva el contexto común. Actividades, ejemplos y laboratorio son formativos,
sin entregas adicionales. La práctica reúne código, comprobaciones y explicación.

| Carpeta/archivo | Para qué sirve |
|---|---|
| `ejemplos/` | Programas Java/JDBC y Python/SQLite completos y acotados. |
| `para_jugar/` | Laboratorio de dos tablas para comprobar restricciones y metadatos. |
| `practica/` | Proyecto Maven, contrato, DDL, pruebas y dos clases por completar. |
| `enunciado.md`, `rubrica.md` | Encargo, ficha sugerida y criterios públicos. |
| `practica.md` | Lo creas tú: ejecución, resultados, decisiones y revisión. |

## Empezar

Continúa en tu repositorio privado y sincroniza `upstream` según la
[0-B](https://www.apuntesinformaticafp.com/actividades/preparacion/repositorio-practicas.html).
El [entorno común](../.devcontainer/README.md) sirve para ambas unidades.
Desde la **raíz del repositorio**:

```bash
mvn -f ud02/ejemplos/pom.xml test
python3 -B ud02/para_jugar/laboratorio.py
mvn -f ud02/practica/pom.xml test
```

Estos comandos funcionan con la base proporcionada. La aceptación con
`-Pcontrato-ud02` fallará hasta completar la práctica. Para PostgreSQL consulta
la configuración del enunciado: `db:5432` dentro del contenedor y
`localhost:55432` desde el equipo que publica el puerto de Docker.

Maven compila Java y genera `target/`; conserva el código fuente, no esos
binarios. Los proyectos de UD01 y UD02 son independientes: puedes comprobar
UD02 aunque tu práctica de ficheros aún tenga algo pendiente. Conserva la
versión presentada de UD01 y su etiqueta; presentar UD02 no vuelve a calificarla.

La corrección de esta unidad comienza al publicar `practica-ad-02-v1`, no al
hacer un commit o subir cambios ordinarios a tu rama.
