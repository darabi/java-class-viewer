cd ..\dist
mkdir plugin
copy ..\..\FormatBMP\dist\FormatBMP.jar   .\plugin
copy ..\..\FormatJPEG\dist\FormatJPEG.jar .\plugin
copy ..\..\FormatPNG\dist\FormatPNG.jar   .\plugin
copy ..\..\FormatZIP\dist\FormatZIP.jar   .\plugin
copy ..\..\FormatPDF\dist\FormatPDF.jar   .\plugin
java -jar BinaryInternalsViewer.jar
pause
