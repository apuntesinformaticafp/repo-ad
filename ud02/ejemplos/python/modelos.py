"""La identidad de almacenamiento no es la identidad de una instancia Python."""
empresa = {"id": "E01", "nombre": "Empresa A", "tutores": [
    {"id": "T01", "nombre": "Tutor A"}, {"id": "T02", "nombre": "Tutor B"}]}
otra = dict(empresa)
print("Misma instancia:", empresa is otra)
print("Mismo valor:", empresa == otra)
print("empresa:", empresa["id"], empresa["nombre"])
for tutor in empresa["tutores"]:
    print("tutor:", tutor["id"], tutor["nombre"], empresa["id"])
