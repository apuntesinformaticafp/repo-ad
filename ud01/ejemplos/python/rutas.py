"""Crear y repetir un árbol de directorios en una carpeta temporal."""

from pathlib import Path
from shutil import copyfile
from tempfile import TemporaryDirectory

print((Path("fct") / "empresas.json").absolute())
with TemporaryDirectory() as carpeta:
    base = Path(carpeta)
    for ruta in ("alumnos/dam", "alumnos/daw", "empresas", "convenios"):
        (base / ruta).mkdir(parents=True, exist_ok=True)
        (base / ruta).mkdir(parents=True, exist_ok=True)
    origen = base / "informe.txt"
    origen.write_text("Seguimiento de prueba", encoding="utf-8")
    copia = base / "copia.txt"
    copyfile(origen, copia)
    origen.rename(base / "renombrado.txt")
    copia.unlink()
    print(sorted(p.name for p in base.iterdir()))
