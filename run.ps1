# Script para executar o projeto Pomodoro Timer
# Configure o caminho do seu JDK 21 abaixo
$JDK_PATH = "C:\Program Files\Java\jdk-21"

if (Test-Path $JDK_PATH) {
    $env:JAVA_HOME = $JDK_PATH
    $env:PATH = "$JDK_PATH\bin;$env:PATH"
    Write-Host "JAVA_HOME configurado para: $JDK_PATH" -ForegroundColor Green
    .\mvnw.cmd javafx:run
} else {
    Write-Host "JDK 21 não encontrado em: $JDK_PATH" -ForegroundColor Red
    Write-Host "Por favor, instale o JDK 21 ou ajuste o caminho no script." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Opções:" -ForegroundColor Cyan
    Write-Host "1. Baixar JDK 21 de: https://adoptium.net/" -ForegroundColor White
    Write-Host "2. Ou ajustar a variável `$JDK_PATH neste script com o caminho correto" -ForegroundColor White
}
