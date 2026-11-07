# Rúbrica de la práctica 2 de AD

**Total: 10 puntos.** Código, comprobaciones y `ud02/practica.md` forman una
única evidencia. Consulta el [enunciado](enunciado.md) antes de implementar.

| Criterio | Máximo | Qué se espera |
|---|---:|---|
| Gestores y preparación | 3 | Ejecutas con SQLite y PostgreSQL; identificas archivo, proceso y servicio. Reproduces la preparación, compruebas persistencia al reabrir y explicas una diferencia y un límite de cada entorno. |
| Conexión desde la aplicación | 3 | Usas configuración externa validada, conectas y cierras recursos en ambos gestores, activas las FK en cada conexión SQLite y reproduces/corriges un fallo de credencial. Distingues dependencia, servicio y autenticación/autorización al diagnosticar. |
| Esquema relacional | 4 | Conservas los identificadores y relaciones FCT, creas y compruebas columnas, tipos, nulabilidad, PK y FK. Justificas la pareja tutor/empresa y las fechas. Demuestras rechazos, detección de estructura incorrecta y reinicio confirmado que conserva tablas ajenas. |

## Niveles para revisar

- **Sin evidencia:** no se demuestra el aspecto o la solución es incorrecta.
- **Parcial:** hay avances, pero faltan comportamientos o comprobaciones.
- **Correcto:** cumple lo pedido con ejecución reproducible y explicación suficiente.
- **Sólido:** conecta resultados, decisiones y límites con precisión.

Los niveles orientan, no son cuatro notas fijas. Los puntos se conceden por los
aspectos acreditados dentro de cada fila. Un error localizado no anula toda la
práctica. El desglose docente justifica estos mismos criterios y máximos, sin
añadir requisitos ocultos. Las ponderaciones del módulo se aplican según la
programación vigente. Explicar los conectores ayuda a entender el trabajo;
el test formativo no sustituye la prueba escrita.

## Antes de presentar

- Ejecuta la base y distingue su resultado de la aceptación de tu implementación.
- Ejecuta `mvn -f ud02/practica/pom.xml -Pcontrato-ud02 clean verify` y, con
  PostgreSQL disponible, añade `,postgresql` al perfil.
- Identifica en `ud02/practica.md` comandos, resultados, decisiones y revisión.
- Revisa primero tú la rúbrica; declara la ayuda significativa utilizada.
- Publica `practica-ad-02-v1`; las revisiones usan la versión siguiente.

Los tests automáticos informan, no calculan por sí solos la nota. La valoración
del contenido y la nota definitiva las reviso yo. El número de commits no puntúa.
