# Uso: .\run.ps1        -> executa a demonstração
#      .\run.ps1 test   -> executa os testes
param([string]$modo = "")
$ErrorActionPreference = "Stop"
if (Test-Path out) { Remove-Item -Recurse -Force out }
$fontes = Get-ChildItem -Recurse src\main\java -Filter *.java | ForEach-Object { $_.FullName }
if ($modo -eq "test") {
    $fontes += Get-ChildItem -Recurse src\test\java -Filter *.java | ForEach-Object { $_.FullName }
    javac -encoding UTF-8 --release 17 -d out $fontes
    java -Dstdout.encoding=UTF-8 -cp out br.com.cafeteria.Testes
} else {
    javac -encoding UTF-8 --release 17 -d out $fontes
    java -Dstdout.encoding=UTF-8 -cp out br.com.cafeteria.Main
}
