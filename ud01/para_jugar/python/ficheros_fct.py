"""Ejemplo didáctico de UD1: datos ficticios y persistencia CSV/JSON.

Python 3.10 o posterior, solo biblioteca estándar.
Ejecutar desde la raíz: python3 -B ud01/para_jugar/python/ficheros_fct.py --salida datos-demo
Procedencia: laboratorio UD1 de aifp; no es la práctica entregable.
"""

import argparse
import csv
import json
from pathlib import Path
from tempfile import NamedTemporaryFile


def validar(empresas):
    """Esquema acotado: empresas con identificador, nombre y tutores."""
    if not isinstance(empresas, list):
        raise ValueError("Se esperaba una lista de empresas")
    ids = set()
    tutores = set()
    for empresa in empresas:
        if not isinstance(empresa, dict) or set(empresa) != {"id", "nombre", "tutores"}:
            raise ValueError("Campos de empresa incorrectos")
        for campo in ("id", "nombre"):
            if not isinstance(empresa[campo], str) or not empresa[campo].strip():
                raise ValueError(f"Campo de empresa inválido: {campo}")
        if empresa["id"] in ids:
            raise ValueError("Identificador de empresa duplicado")
        ids.add(empresa["id"])
        if not isinstance(empresa["tutores"], list):
            raise ValueError("Los tutores deben ser una lista")
        for tutor in empresa["tutores"]:
            if not isinstance(tutor, dict) or set(tutor) != {"id", "nombre"}:
                raise ValueError("Campos de tutor incorrectos")
            if any(
                not isinstance(tutor[k], str) or not tutor[k].strip()
                for k in ("id", "nombre")
            ):
                raise ValueError("Identificador o nombre de tutor inválido")
            if tutor["id"] in tutores:
                raise ValueError("Identificador de tutor duplicado")
            tutores.add(tutor["id"])


def escribir_seguro(destino, texto, *, simular_fallo=False):
    """Reemplaza un único fichero; no ofrece transacciones entre ficheros.

    simular_fallo permite observar un error tras escribir el temporal, antes
    del reemplazo. El temporal se crea junto al destino y se limpia al terminar.
    """
    destino = Path(destino)
    destino.parent.mkdir(parents=True, exist_ok=True)
    temporal = None
    try:
        with NamedTemporaryFile(
            mode="w", encoding="utf-8", newline="", dir=destino.parent, delete=False
        ) as salida:
            temporal = Path(salida.name)
            salida.write(texto)
        if simular_fallo:
            raise OSError("Fallo simulado antes del reemplazo")
        temporal.replace(destino)
    finally:
        if temporal is not None:
            temporal.unlink(missing_ok=True)


class RepositorioEmpresas:
    """Contrato acotado: guardar una colección completa y recuperar esa colección.

    ruta configura el almacenamiento; notificar recibe un mensaje tras guardar.
    Un fichero inexistente es error. Para iniciar un almacén vacío se guarda [].
    Los errores de acceso, sintaxis y validación se propagan al cliente.
    """

    def __init__(self, ruta, notificar=None):
        self.ruta = Path(ruta)
        self.notificar = notificar

    def guardar(self, empresas, *, simular_fallo=False):
        validar(empresas)
        texto = json.dumps(empresas, ensure_ascii=False, indent=2)
        escribir_seguro(self.ruta, texto, simular_fallo=simular_fallo)
        if self.notificar is not None:
            self.notificar("empresas guardadas")

    def recuperar(self):
        with self.ruta.open(encoding="utf-8") as entrada:
            empresas = json.load(entrada)
        validar(empresas)
        return empresas


def exportar_csv(empresas, ruta):
    """Proyección tabular: id y nombre; no exporta los tutores."""
    validar(empresas)
    with Path(ruta).open("w", encoding="utf-8", newline="") as salida:
        escritor = csv.DictWriter(salida, fieldnames=["id", "nombre"], delimiter=";")
        escritor.writeheader()
        for empresa in empresas:
            escritor.writerow({k: empresa[k] for k in ("id", "nombre")})


def demostrar(carpeta):
    carpeta = Path(carpeta)
    carpeta.mkdir(parents=True, exist_ok=True)
    datos = [
        {
            "id": "E01",
            "nombre": 'Empresa A; formación "técnica"',
            "tutores": [{"id": "T01", "nombre": "Tutor A"}],
        },
        {"id": "E02", "nombre": "Empresa B", "tutores": []},
    ]
    eventos = []
    repo = RepositorioEmpresas(carpeta / "empresas.json", eventos.append)
    repo.guardar(datos)
    nuevo = RepositorioEmpresas(repo.ruta)
    assert nuevo.recuperar() == datos
    print("JSON: relectura correcta con un componente nuevo")

    exportar_csv(datos, carpeta / "empresas.csv")
    with (carpeta / "empresas.csv").open(encoding="utf-8", newline="") as entrada:
        filas = list(csv.DictReader(entrada, delimiter=";"))
    assert filas == [{"id": e["id"], "nombre": e["nombre"]} for e in datos]
    print("CSV: nombres conservados; tutores omitidos por la proyección")

    anterior = repo.ruta.read_bytes()
    for candidato, fallo in [(datos + [datos[0]], False), ([], True)]:
        try:
            repo.guardar(candidato, simular_fallo=fallo)
        except (ValueError, OSError) as error:
            print(f"Error controlado: {error}")
        else:
            raise AssertionError("Se esperaba el rechazo de la operación")
        assert repo.ruta.read_bytes() == anterior
        assert eventos == ["empresas guardadas"]
    print("Fichero previo y notificaciones correctos tras ambos fallos")

    vacio = RepositorioEmpresas(carpeta / "vacio.json")
    vacio.guardar([])
    assert vacio.recuperar() == []
    print("Colección vacía: comprobación correcta")


if __name__ == "__main__":
    argumentos = argparse.ArgumentParser(description=__doc__)
    argumentos.add_argument("--salida", type=Path, default=Path("datos-demo"))
    demostrar(argumentos.parse_args().salida)
