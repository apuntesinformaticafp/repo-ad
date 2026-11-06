"""CSV con delimitador dentro de un campo y datos JSON anidados."""
import csv
import io
import json

empresa = json.loads('{"id":"E01","tutores":[]}')
print(isinstance(empresa["tutores"], list))
texto = 'nombre;ciclo;empresa\n"Estudiante A; prueba";DAM;E01\nEstudiante B;DAW;E02\n'
for fila in csv.DictReader(io.StringIO(texto), delimiter=";"):
    if fila["ciclo"] == "DAM":
        print(fila["nombre"])
