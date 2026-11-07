from contextlib import closing
import os
import sqlite3

archivo = os.environ.get("AD_SQLITE", ":memory:")
if not archivo.strip():
    raise ValueError("AD_SQLITE no puede quedar vacío")
with closing(sqlite3.connect(archivo)) as conexion:
    print("Configuración externa; comprobación mínima:", conexion.execute("SELECT 1").fetchone())
