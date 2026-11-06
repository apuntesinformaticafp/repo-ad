"""Pruebas de contrato y fallos, no solo de serialización en memoria."""
import json
from pathlib import Path
from tempfile import TemporaryDirectory
import unittest

from ficheros_fct import RepositorioEmpresas


class PersistenciaTest(unittest.TestCase):
    def setUp(self):
        self.temporal = TemporaryDirectory()
        self.addCleanup(self.temporal.cleanup)
        self.ruta = Path(self.temporal.name) / "empresas.json"
        self.datos = [{"id": "E01", "nombre": "Empresa A; formación", "tutores": []}]

    def test_relectura_desde_otra_instancia_y_vacio(self):
        repo = RepositorioEmpresas(self.ruta)
        repo.guardar(self.datos)
        self.assertEqual(self.datos, RepositorioEmpresas(self.ruta).recuperar())
        repo.guardar([])
        self.assertEqual([], RepositorioEmpresas(self.ruta).recuperar())

    def test_duplicado_y_fallo_conservan_datos_y_no_notifican(self):
        eventos = []
        repo = RepositorioEmpresas(self.ruta, eventos.append)
        repo.guardar(self.datos)
        anterior = self.ruta.read_bytes()
        with self.assertRaises(ValueError):
            repo.guardar(self.datos * 2)
        with self.assertRaises(OSError):
            repo.guardar([], simular_fallo=True)
        self.assertEqual(anterior, self.ruta.read_bytes())
        self.assertEqual(["empresas guardadas"], eventos)
        self.assertEqual([self.ruta], list(self.ruta.parent.iterdir()))

    def test_ausencia_y_sintaxis_no_son_coleccion_vacia(self):
        repo = RepositorioEmpresas(self.ruta)
        with self.assertRaises(FileNotFoundError):
            repo.recuperar()
        self.ruta.write_text("[", encoding="utf-8")
        with self.assertRaises(json.JSONDecodeError):
            repo.recuperar()

if __name__ == "__main__":
    unittest.main()
