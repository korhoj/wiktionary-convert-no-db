@echo off
rem Joel Korhonen 2021-May-15
rem Strips comments etc. namespaces not containing Wiktionary entries from the input file
rem Processes the Wiktionary given as parameter or by default the English Wiktionary
rem Optionally the edition may be specified as the second parameter
rem
rem Before running, change the variables below as needed:
rem Set JAVA_HOME to point to JDK main dir, e.g. C:\Usr\openjdk-16_windows-x64_bin\jdk-16
rem Set JAR_DIR to the location of the runnable JAR file
rem Set DICT to the location under which you uncompressed the Wiktionary edition you have downloaded
rem e.g. DICT=C:\Temp\wiktionary-dumps
rem   having uncompressed enwiktionary-%EDITION%-pages-articles.xml.bz2 to
rem   C:\Temp\enwiktionary-%EDITION%-pages-articles.xml
rem Then change EDITION to match the Wiktionary edition you have downloaded

set DICT=F:\Temp\wiktionary-dumps
set JAR_DIR=G:\Dropbox\Dictionary\wikt
set JAVA_HOME=C:\Usr\jdk-17.0.5

set EDITION=20221120
set LANG=en

rem Language specific scripts, e.g. "StripNamespaces elwiktionary.cmd" call passing LANG as param
if not "%1"=="" set LANG=%1
if not "%2"=="" set EDITION=%2

SET PROGDIR=%JAR_DIR%
SET PROG=%PROGDIR%\StripNamespaces.jar
SET JCLASS=wiktionary\to\xml\full\StripNamespaces
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET INFILE=%DICT%\%LANG%wiktionary-%EDITION%-pages-articles.xml\%LANG%wiktionary-%EDITION%-pages-articles.xml
SET OUTFILE=%DICT%\%LANG%wiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml
SET UTF8=-Dfile.encoding=UTF8
SET MEM=-Xmx2600M
SET STCK=-Xss400M

cd %PROGDIR%
chcp 65001
cls

echo "EDITION: %EDITION%"
echo "PROG: %PROG%"
echo "INFILE: %INFILE%"
echo "OUTFILE: %OUTFILE%"

%JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE%
if %ERRORLEVEL% GTR 0 goto :errEnd
echo Ready
goto end

:errEnd
echo ""
echo Ended in error
pause
exit 8

:end