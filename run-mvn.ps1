param(
    [Parameter(ValueFromRemainingArguments=$true)]
    [string[]]$MavenArgs
)

# Use the pre-installed Maven binary found on the system
$MvnPath = "C:\Users\HP\.m2\wrapper\dists\apache-maven-3.9.14-bin\1cb7fhup6b5n3bed6kckbrnspv\apache-maven-3.9.14\bin\mvn.cmd"

if (-not (Test-Path $MvnPath)) {
    # Fallback to IntelliJ Maven if wrapper isn't found
    $MvnPath = "C:\Program Files\JetBrains\IntelliJ IDEA 2025.3.1\plugins\maven\lib\maven3\bin\mvn.cmd"
}

Write-Host "Running Maven: mvn $MavenArgs" -ForegroundColor Cyan
& $MvnPath @MavenArgs
