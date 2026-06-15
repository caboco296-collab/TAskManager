#!/bin/bash
# Script para compilar e executar o TaskManager
echo "=== TaskManager — Compilando... ==="
mkdir -p out
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
if [ $? -eq 0 ]; then
    echo "✅ Compilação bem-sucedida!"
    echo "=== Iniciando aplicação... ==="
    java -cp out taskmanager.Main
else
    echo "❌ Erro na compilação. Verifique o JDK instalado (java -version)"
fi
