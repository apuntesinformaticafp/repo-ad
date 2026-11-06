"""Errores de acceso y conservación del fichero anterior."""

from pathlib import Path
from tempfile import NamedTemporaryFile, TemporaryDirectory


def escribir_seguro(destino, texto, *, simular_fallo=False):
    temporal = None
    try:
        with NamedTemporaryFile(
            mode="w", encoding="utf-8", dir=destino.parent, delete=False
        ) as salida:
            temporal = Path(salida.name)
            salida.write(texto)
        if simular_fallo:
            raise OSError("Fallo simulado antes del reemplazo")
        temporal.replace(destino)
    finally:
        if temporal is not None:
            temporal.unlink(missing_ok=True)


with TemporaryDirectory() as carpeta:
    fichero = Path(carpeta) / "datos.txt"
    try:
        fichero.read_text(encoding="utf-8")
    except FileNotFoundError:
        print("El fichero no existe")
    fichero.write_text("anterior", encoding="utf-8")
    try:
        escribir_seguro(fichero, "nuevo", simular_fallo=True)
    except OSError as error:
        print(error)
    print(fichero.read_text(encoding="utf-8"))
