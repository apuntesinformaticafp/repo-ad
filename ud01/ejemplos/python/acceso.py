"""Longitudes y posiciones: no confundir caracteres con bytes."""
from io import BytesIO

texto = "cañón"
print("Puntos de código:", len(texto))
print("Bytes UTF-8:", len(texto.encode("utf-8")))
print("Cuarto registro de 16 bytes:", 3 * 16)
registros = BytesIO(b"O01\nO02\nO03\n")
registros.seek(2 * 4)
print(registros.read(4))
