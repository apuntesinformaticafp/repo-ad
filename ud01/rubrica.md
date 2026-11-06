# Rúbrica de la práctica 1 de AD

**Total: 10 puntos.** Código, comprobaciones y `practica.md` forman una única
evidencia. Se valora el funcionamiento y su explicación, no escribir más.

| Criterio | Máximo | Qué se espera |
|---|---:|---|
| Gestión de ficheros | 1,6 | Usas rutas configurables y explicas la prueba que copia, mueve, recupera y borra una copia sin alterar el original. |
| Recuperación JSON | 1,8 | Recuperas el estado completo desde otra instancia, incluido el estado vacío válido; rechazas ausencia, documento inválido y datos incompatibles. |
| Almacenamiento e intercambio | 1,8 | Guardas todos los valores JSON y exportas el CSV indicado; conservas caracteres especiales y el fichero anterior ante el fallo controlado. |
| Comprobación y documentación | 1,8 | Ejecutas la aceptación y la demo, documentas resultados normales y de error y dejas instrucciones reproducibles. |
| Diseño del componente | 1 | Explicas la separación entre servicio y almacén, su ventaja y un límite de la persistencia en ficheros. |
| Herramientas | 1 | Utilizas y explicas entorno, Maven, bibliotecas JSON/CSV y pruebas; distingues código fuente y salidas generadas. |
| Integración del componente | 1 | Cumples el contrato, configuras la ruta y notificas después del éxito; el servicio funciona con tu almacén. |

## Niveles para revisar

- **Sin evidencia:** el requisito no está demostrado o la solución es incorrecta.
- **Parcial:** se cumple una parte, pero faltan comportamientos o comprobaciones.
- **Correcto:** cumple lo pedido con evidencia reproducible y explicación suficiente.
- **Sólido:** además conecta resultados, decisiones y límites con precisión.

Los niveles orientan; no son cuatro notas fijas. Se conceden los puntos de los
aspectos acreditados hasta el máximo de cada criterio. Un error localizado no
anula toda la práctica. La corrección detallada justifica la puntuación de estas
mismas filas, sin añadir requisitos ocultos. Las ponderaciones del módulo se
aplican según la programación vigente.

## Comprueba antes de presentar

- El código está en `ud01/practica/` y la ficha en `ud01/practica.md`.
- Has distinguido las pruebas iniciales de la aceptación de tu implementación.
- Puedes explicar una relectura, el CSV y el fallo controlado con sus resultados.
- Has revisado cada criterio y declarado la ayuda significativa utilizada.
- El historial identifica versiones; no se puntúa por número de commits.

Las pruebas automáticas aportan información, no calculan por sí solas la nota.
La valoración del contenido y la nota definitiva las reviso yo.
