param(
    [Parameter(ValueFromRemainingArguments=$true)]
    [string[]]$MavenArgs
)

$MavenVersion = "3.9.6"
$MavenDirName = "apache-maven-$MavenVersion"
$TargetDir = Join-Path $Home ".m2\portable-maven"
$MavenHome = Join-Path $TargetDir $MavenDirName
$MvnPath = Join-Path $MavenHome "bin\mvn.cmd"
$ZipPath = Join-Path $TargetDir "maven.zip"

if (-not (Test-Path $MvnPath)) {
    Write-Host "Portable Maven not found. Downloading Maven $MavenVersion..." -ForegroundColor Cyan
    if (-not (Test-Path $TargetDir)) {
        New-Item -ItemType Directory -Path $TargetDir -Force | Out-Null
    }
    
    $Url = "https://archive.apache.org/dist/maven/maven-3/$MavenVersion/binaries/apache-maven-$MavenVersion-bin.zip"
    Write-Host "Downloading using native curl.exe from $Url..." -ForegroundColor Gray
    
    # Run native curl to download the zip
    & curl.exe -L -o $ZipPath $Url
    
    if (-not (Test-Path $ZipPath)) {
        Write-Error "Failed to download Maven archive."
        exit 1
    }

    Write-Host "Extracting archive to $TargetDir..." -ForegroundColor Gray
    # Use PowerShell's Expand-Archive
    Expand-Archive -Path $ZipPath -DestinationPath $TargetDir -Force
    
    # Clean up zip
    Remove-Item -Path $ZipPath -Force
    Write-Host "Maven installed successfully at $MavenHome" -ForegroundColor Green
}

# Run maven
Write-Host "Running Maven: mvn $MavenArgs" -ForegroundColor Cyan
& $MvnPath @MavenArgs
