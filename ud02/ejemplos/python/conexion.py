"""El gestor SQLite vive en el proceso; la estructura persiste en un archivo."""
from contextlib import closing
from pathlib import Path
import sqlite3
from tempfile import TemporaryDirectory

with TemporaryDirectory() as carpeta:
    archivo = Path(carpeta) / "prueba.sqlite"
    with closing(sqlite3.connect(archivo)) as conexion:
        conexion.execute("CREATE TABLE marca (id TEXT PRIMARY KEY NOT NULL)")
        conexion.commit()
    with closing(sqlite3.connect(archivo)) as conexion:
        print("Tras reabrir:", conexion.execute("PRAGMA table_info(marca)").fetchall())
print("Conexiones cerradas y archivo temporal retirado")
