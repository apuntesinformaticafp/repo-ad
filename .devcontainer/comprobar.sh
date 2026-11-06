#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
java -version
mvn -version
python3 --version
mvn -B -f ud01/practica/pom.xml test
mvn -B -f ud01/ejemplos/pom.xml test
mvn -B -f ud01/para_jugar/pom.xml compile
python3 -B -m unittest discover -s ud01/para_jugar/python -p 'test_*.py'
# Conserva las comprobaciones del material previo de UD02.
mvn -B test
mvn -B -f ejemplos/pom.xml test
python3 -B -m unittest discover -s ejemplos/ud02-bases-datos/python -p 'test_*.py'
