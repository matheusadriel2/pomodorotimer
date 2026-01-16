# Script para encontrar JDK e executar o projeto
Write-Host "Procurando JDK instalado..." -ForegroundColor Cyan

$possiblePaths = @(
    "C:\Program Files\Java\jdk-21",
    "C:\Program Files\Java\jdk-21.0.1",
    "C:\Program Files\Java\jdk-21.0.2",
    "C:\Program Files\Eclipse Adoptium\jdk-21*",
    "C:\Program Files\Microsoft\jdk-21*",
    "C:\Program Files\Amazon Corretto\jdk21*",
    "$env:LOCALAPPDATA\Programs\Eclipse Adoptium\jdk-21*",
    "$env:ProgramFiles\Java\jdk-21*"
)

$foundJDK = $null

foreach ($path in $possiblePaths) {
    $resolved = Resolve-Path $path -ErrorAction SilentlyContinue
    if ($resolved) {
        $jdkPath = $resolved[0].Path
        if (Test-Path "$jdkPath\bin\javac.exe") {
            $foundJDK = $jdkPath
            break
        }
    }
}

# Se não encontrou, procura em todos os diretórios Java
if (-not $foundJDK) {
    $javaDirs = @(
        "C:\Program Files\Java",
        "C:\Program Files\Eclipse Adoptium",
        "C:\Program Files\Microsoft",
        "$env:LOCALAPPDATA\Programs\Eclipse Adoptium"
    )
    
    foreach ($baseDir in $javaDirs) {
        if (Test-Path $baseDir) {
            $jdkDirs = Get-ChildItem $baseDir -Directory -ErrorAction SilentlyContinue | 
                       Where-Object { $_.Name -like "jdk-21*" -or $_.Name -like "jdk21*" }
            
            foreach ($jdkDir in $jdkDirs) {
                if (Test-Path "$($jdkDir.FullName)\bin\javac.exe") {
                    $foundJDK = $jdkDir.FullName
                    break
                }
            }
        }
        if ($foundJDK) { break }
    }
}

if ($foundJDK) {
    Write-Host "JDK encontrado em: $foundJDK" -ForegroundColor Green
    $env:JAVA_HOME = $foundJDK
    $env:PATH = "$foundJDK\bin;$env:PATH"
    Write-Host "JAVA_HOME configurado. Executando aplicação..." -ForegroundColor Green
    .\mvnw.cmd javafx:run
} else {
    Write-Host "JDK 21 não encontrado!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Por favor, instale o JDK 21:" -ForegroundColor Yellow
    Write-Host "1. Acesse: https://adoptium.net/temurin/releases/?version=21" -ForegroundColor Cyan
    Write-Host "2. Baixe e instale o JDK 21 LTS" -ForegroundColor Cyan
    Write-Host "3. Execute este script novamente" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "OU configure manualmente o JAVA_HOME:" -ForegroundColor Yellow
    Write-Host '  $env:JAVA_HOME = "C:\caminho\para\seu\jdk-21"' -ForegroundColor White
    Write-Host '  .\mvnw.cmd javafx:run' -ForegroundColor White
}
