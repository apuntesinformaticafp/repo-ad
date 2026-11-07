"""Laboratorio formativo: dos tablas, restricciones y una base temporal propia."""

from contextlib import closing
from pathlib import Path
import sqlite3
from tempfile import TemporaryDirectory


def crear(conexion):
    conexion.executescript("""
        CREATE TABLE IF NOT EXISTS empresa (
            id TEXT PRIMARY KEY NOT NULL,
            nombre TEXT NOT NULL CHECK (length(trim(nombre)) > 0)
        );
        CREATE TABLE IF NOT EXISTS tutor (
            id TEXT PRIMARY KEY NOT NULL,
            empresa_id TEXT NOT NULL REFERENCES empresa(id)
        );
    """)


def abrir(ruta):
    conexion = sqlite3.connect(ruta)
    conexion.execute("PRAGMA foreign_keys = ON")
    return conexion


def main():
    with TemporaryDirectory(prefix="ad-ud02-") as carpeta:
        ruta = Path(carpeta) / "laboratorio.sqlite"
        with closing(abrir(ruta)) as conexion:
            crear(conexion)
            with conexion:
                conexion.execute(
                    "INSERT INTO empresa VALUES (?, ?)", ("E01", "Empresa A")
                )
            crear(conexion)
            assert conexion.execute("SELECT count(*) FROM empresa").fetchone()[0] == 1
            print("Crear dos veces conserva la fila")

            for nombre in (None, "", "   "):
                try:
                    with conexion:
                        conexion.execute(
                            "INSERT INTO empresa VALUES (?, ?)", ("E02", nombre)
                        )
                except sqlite3.IntegrityError:
                    print(f"Nombre {nombre!r}: rechazado")
                else:
                    raise AssertionError(f"Se esperaba rechazar {nombre!r}")

            try:
                with conexion:
                    conexion.execute("INSERT INTO tutor VALUES (?, ?)", ("T01", "E99"))
            except sqlite3.IntegrityError:
                print("Tutor sin empresa: rechazado")
            else:
                raise AssertionError("Falta comprobar la clave ajena")

            print(
                "Columnas de empresa:",
                conexion.execute("PRAGMA table_info(empresa)").fetchall(),
            )
            print(
                "FK de tutor:",
                conexion.execute("PRAGMA foreign_key_list(tutor)").fetchall(),
            )

        with closing(abrir(ruta)) as otra:
            assert otra.execute("SELECT nombre FROM empresa").fetchone() == (
                "Empresa A",
            )
            print("Otra conexión recupera la fila del mismo archivo")
            otra.execute("DROP TABLE tutor")
            tablas = {
                fila[0]
                for fila in otra.execute(
                    "SELECT name FROM sqlite_master WHERE type='table'"
                )
            }
            assert "tutor" not in tablas
            print("Falta tutor: crear de nuevo lo repone, no es una migración")
            crear(otra)
        print("Fin: se elimina únicamente la carpeta temporal del laboratorio")


if __name__ == "__main__":
    main()
