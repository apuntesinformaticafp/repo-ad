"""Microesquema formativo, distinto del proyecto privado completo."""
from contextlib import closing
import sqlite3


def abrir(archivo=":memory:"):
    conexion = sqlite3.connect(archivo)
    conexion.execute("PRAGMA foreign_keys = ON")
    return conexion


def crear(conexion):
    conexion.execute("CREATE TABLE IF NOT EXISTS empresa (id TEXT PRIMARY KEY NOT NULL, nombre TEXT NOT NULL)")
    conexion.execute("CREATE TABLE IF NOT EXISTS tutor (id TEXT PRIMARY KEY NOT NULL, nombre TEXT NOT NULL, empresa_id TEXT NOT NULL REFERENCES empresa(id))")
    conexion.commit()


def comprobar(conexion):
    tablas = {fila[0] for fila in conexion.execute("SELECT name FROM sqlite_schema WHERE type='table'")}
    if not {"empresa", "tutor"} <= tablas:
        raise ValueError("Falta una tabla esperada")
    referencias = conexion.execute("PRAGMA foreign_key_list(tutor)").fetchall()
    if not referencias or referencias[0][2] != "empresa":
        raise ValueError("Falta la referencia tutor → empresa")


if __name__ == "__main__":
    with closing(abrir()) as conexion:
        crear(conexion)
        crear(conexion)
        comprobar(conexion)
        print("Tablas y referencia comprobadas")
        try:
            conexion.execute("INSERT INTO tutor VALUES ('T01', 'Tutor A', 'E99')")
        except sqlite3.IntegrityError:
            print("El esquema rechaza al tutor huérfano")
