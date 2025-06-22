param(
[string] $outputFolder, 
[string] $inputFolder

)

$commnad="%JAVA_HOME%\bin\java.exe -jar TcSupportUtility.jar"

$count=1

$inputDir=$inputFolder. Replace(":\","") 
$ispath=Test-Path -Path $outputFolder

if(!$isPath) {
	New-Item -Path $outputFolder ItemType "directory" -ErrorAction SilentlyContinue
}


Get-ChildItem -Path $inputFolder -Recurse File | ForEach-Object {

$folderPath= $ DirectoryName -split $inputDir | Select-Object -index 1
$p=$outputFolder
$p=$p + "\"+$folderPath
$isPath=Test-Path -Path $p

if(!$isPath) {

New Item -Path $p -ItemType "directory" -ErrorAction SilentlyContinue

}

$output=$p+"/"+$_.BaseName+".csv"

$command=$commnad+" -input="+$_.FullName +" -output="+$output

Write-Host "Drop $count"
Write-Host $_.FullName
Write-Host
Invoke-Expression -Command $command
count++
Write-Host
}
