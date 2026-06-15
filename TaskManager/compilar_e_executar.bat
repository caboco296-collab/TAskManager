@echo off
echo === TaskManager — Compilando... ===
mkdir out 2>nul
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d out @sources.txt
if %ERRORLEVEL% == 0 (
    echo Compilacao bem-sucedida!
    echo === Iniciando aplicacao... ===
    java -cp out taskmanager.Main
) else (
    echo Erro na compilacao. Verifique o JDK instalado.
)
pause
