# Laboratorio de empresas FCT

Ejemplo completo y formativo para experimentar en Codespaces. Une las piezas de
[ejemplos/](../ejemplos/README.md): empresas con tutores, JSON, CSV de resumen,
validación, escritura segura y notificaciones. No se entrega ni se califica.

## Ejecutar

Desde la raíz del repositorio, elige Java (referencia de la práctica) o Python:

```bash
mvn -q -f ud01/para_jugar/pom.xml compile exec:java -Dexec.mainClass=laboratorio.EmpresasFct
python3 -B ud01/para_jugar/python/ficheros_fct.py --salida datos-demo/laboratorio-python
python3 -B -m unittest discover -s ud01/para_jugar/python -p 'test_*.py'
```

Java está en `java/src/main/java/laboratorio/EmpresasFct.java` y reutiliza los
ejemplos pequeños mediante el POM. Python utiliza solo biblioteca estándar.
Java genera `datos-demo/laboratorio-java/`; Python, la ruta indicada. Al repetir,
cada laboratorio sobrescribe sus archivos de demostración. Usa datos ficticios.

## Observar

En ambos lenguajes se comprueba:

1. JSON: otra instancia recupera los mismos valores desde disco.
2. CSV: nombres conservados al releer; tutores omitidos por la proyección.
3. Duplicado rechazado antes de guardar.
4. Fallo simulado antes del reemplazo: fichero anterior conservado, sin aviso de éxito.
5. Colección vacía guardada y recuperada correctamente.

Abre los JSON y CSV generados. Localiza en el código **validar → serializar →
escribir temporal → reemplazar → notificar**. El aviso debe ocurrir después del
guardado y su receptor debe terminar sin lanzar una excepción.

## Cambia una cosa cada vez

- Añade una empresa con dos tutores. Predice qué aparecerá en JSON y CSV.
- Duplica un ID de tutor. Comprueba el rechazo y que se conserva el fichero previo.
- Modifica una copia del JSON: elimina un campo obligatorio y prueba su lectura.
  Distingue ese error de un JSON truncado.
- Añade al CSV el número de tutores. ¿Permite recuperar sus nombres?
- Guarda una colección vacía y compárala con un archivo de cero bytes.

Comprobación razonada: JSON conserva la lista; CSV solo ID y nombre. El contador
de tutores sigue siendo una proyección. Un duplicado falla antes del reemplazo.
Un documento truncado falla al parsear; un campo obligatorio omitido falla al
interpretar o validar los datos. Un archivo ausente no se convierte en `[]`.

## Hacia la práctica

El laboratorio almacena **solo empresas**. En la [práctica](../enunciado.md), el
modelo y el servicio ya incluyen estudiantes y asignaciones: completa los tres
métodos pendientes aplicando las mismas ideas. Su CSV tiene otra cabecera y una
fila por asignación. El reemplazo protege un fichero; no es una transacción entre
JSON y CSV ni una garantía ante cualquier caída del sistema.
