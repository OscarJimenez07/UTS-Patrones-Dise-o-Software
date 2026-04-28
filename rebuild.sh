#!/bin/bash
# Reconstruye el frontend, lo copia al backend y reempaquetar el JAR.
# Uso:  ./rebuild.sh
set -e

ROOT="$(cd "$(dirname "$0")" && pwd)"
MVN="$ROOT/tools/apache-maven-3.9.15/bin/mvn"

echo ">>> [1/3] Compilando frontend..."
cd "$ROOT/frontend"
npm run build

echo ">>> [2/3] Copiando bundle al backend..."
rm -rf "$ROOT/backend/src/main/resources/static"
mkdir -p "$ROOT/backend/src/main/resources/static"
cp -r dist/* "$ROOT/backend/src/main/resources/static"/

echo ">>> [3/3] Empaquetando JAR..."
cd "$ROOT/backend"
"$MVN" -q -DskipTests package

echo ""
echo "Listo. Para correrlo:"
echo "   java -jar backend/target/gameengine.jar"
