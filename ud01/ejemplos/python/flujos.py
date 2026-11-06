"""Lectura, relectura y anexado sin modificar archivos del usuario."""
from pathlib import Path
from tempfile import TemporaryDirectory

with TemporaryDirectory() as carpeta:
    fichero = Path(carpeta) / "alumnos.txt"
    fichero.write_text("Estudiante A\nEstudiante B\nEstudiante C\n", encoding="utf-8")
    with fichero.open("a", encoding="utf-8") as salida:
        salida.write("Estudiante D\n")
    with fichero.open(encoding="utf-8") as entrada:
        for linea in entrada:
            print(linea.rstrip("\n"))
