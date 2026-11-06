"""Guion reproducible de UD2. Java realiza el DDL y comprueba la estructura."""
import argparse
from pathlib import Path
import shutil
import subprocess


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("accion", choices=("crear", "comprobar", "reiniciar"))
    parser.add_argument("--confirmar-pruebas", action="store_true")
    args = parser.parse_args()
    maven = shutil.which("mvn")
    if maven is None:
        parser.error("Maven no está disponible; utiliza el entorno preparado")
    comando = [maven, "-q"]
    accion = args.accion + (" --confirmar-pruebas" if args.confirmar_pruebas else "")
    comando.extend(["clean", "compile", "exec:java", "-Dexec.mainClass=fct.bd.DemoConexion", "-Dexec.args=" + accion])
    # clean evita reutilizar clases entre ejecuciones.
    return subprocess.run(comando, cwd=Path(__file__).resolve().parents[1]).returncode


if __name__ == "__main__":
    raise SystemExit(main())
