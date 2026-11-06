# Rúbrica de autoevaluación — UD2

Utiliza esta rúbrica para revisar el enunciado de [UD2](ud02.md) antes de
presentar tu trabajo. Se valora una única evidencia en el mismo repositorio
privado. La calificación numérica depende de la programación vigente.

En cada criterio, marca el nivel que mejor describa tu evidencia y anota dónde
puede comprobarse. No se puntúa el número de commits.

| Criterio | Parcial | Correcta | Sólida |
|---|---|---|---|
| Gestores y preparación | Solo prepara o prueba uno de los gestores, o la ejecución no se puede repetir. | Prepara y comprueba SQLite y PostgreSQL; explica archivo/servicio y conserva datos al repetir la preparación. | Además contrasta sus diferencias y demuestra un límite de cada entorno con resultados. |
| Conexión desde la aplicación | La conexión depende de valores en el código o no se diagnostica un fallo. | Configura fuera del código, conecta y cierra recursos en ambos gestores; demuestra un error de credenciales y su corrección. | Además distingue fallos de dependencia, servicio y autorización con evidencia. |
| Esquema relacional | El esquema no refleja las relaciones o no comprueba restricciones. | Crea y verifica tablas, columnas, tipos, nulabilidad, PK y FK; comprueba rechazos y una reinicialización segura. | Además justifica restricciones y demuestra que se conserva una tabla ajena al caso. |

## Antes de presentar

- Ejecuta `mvn -Pcontrato-ud02 clean verify` para SQLite y
  `mvn -Pcontrato-ud02,postgresql clean verify` con PostgreSQL.
- Completa `docs/entregas/ud02.md` con comandos, resultados, demostración y
  autoevaluación breve.
- Publica `practica-ad-02-v1`; una revisión usa la versión siguiente, sin borrar
  etiquetas anteriores.
