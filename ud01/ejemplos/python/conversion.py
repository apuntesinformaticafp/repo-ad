"""JSON → modelo intermedio → CSV de resumen. Se omiten los tutores."""

import csv
import io
import json

texto = '{"id":"E01","nombre":"Empresa A; formación","tutores":[{"id":"T01","nombre":"Tutor A"}]}'
empresa = json.loads(texto)
salida = io.StringIO(newline="")
escritor = csv.DictWriter(salida, fieldnames=["id", "nombre"], delimiter=";")
escritor.writeheader()
escritor.writerow({k: empresa[k] for k in ("id", "nombre")})
print(salida.getvalue())
print("Tutores omitidos en CSV:", len(empresa["tutores"]))
