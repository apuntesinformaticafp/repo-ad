from contextlib import closing
from pathlib import Path
import sqlite3
from tempfile import TemporaryDirectory
import unittest
from esquema import abrir, crear, comprobar


class InfraestructuraTest(unittest.TestCase):
    def test_reapertura(self):
        with TemporaryDirectory() as carpeta:
            archivo = Path(carpeta) / "prueba.sqlite"
            with closing(abrir(archivo)) as c:
                crear(c)
            with closing(abrir(archivo)) as c:
                comprobar(c)

    def test_creacion_repetida_conserva_fila(self):
        with closing(abrir()) as c:
            crear(c)
            c.execute("INSERT INTO empresa VALUES ('E01', 'Empresa A')")
            c.commit()
            crear(c)
            self.assertEqual(c.execute("SELECT count(*) FROM empresa").fetchone()[0], 1)

    def test_rechazo_tutor_huerfano(self):
        with closing(abrir()) as c:
            crear(c)
            with self.assertRaises(sqlite3.IntegrityError):
                c.execute("INSERT INTO tutor VALUES ('T01', 'Tutor A', 'E99')")

    def test_preparacion_incompleta(self):
        with closing(abrir()) as c:
            crear(c)
            c.execute("DROP TABLE tutor")
            with self.assertRaises(ValueError):
                comprobar(c)


if __name__ == "__main__":
    unittest.main()
