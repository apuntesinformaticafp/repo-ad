"""Un cliente conoce el contrato, no la representación interna."""
from typing import Protocol


class Empresas(Protocol):
    def guardar(self, empresa: dict) -> None: ...
    def buscar(self, identificador: str) -> dict | None: ...


class EmpresasEnMemoria:
    def __init__(self):
        self.datos = {}

    def guardar(self, empresa):
        self.datos[empresa["id"]] = dict(empresa)

    def buscar(self, identificador):
        empresa = self.datos.get(identificador)
        return None if empresa is None else dict(empresa)


repositorio: Empresas = EmpresasEnMemoria()
repositorio.guardar({"id": "E01", "nombre": "Empresa A"})
print(repositorio.buscar("E01"))
print(repositorio.buscar("E99"))
