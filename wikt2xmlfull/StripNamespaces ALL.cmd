@echo off
rem Process the English Wiktionary
rem Joel Korhonen 2012-Jul-27
rem 2021-04-07 Changed to use variables given in the script, and updated edition

rem Before running, change the variables below as needed:
rem Set JAVA_HOME to point to JDK main dir, e.g. C:\Usr\openjdk-16_windows-x64_bin\jdk-16
rem Set JAR_DIR to the location of the runnable JAR file
rem Set DICT to the location under which you uncompressed the Wiktionary edition you have downloaded
rem e.g. DICT=C:\Temp\enwiktionary\
rem   having uncompressed enwiktionary-%EDITION%-pages-articles.xml.bz2 to
rem   C:\Temp\enwiktionary-%EDITION%-pages-articles.xml
rem Then change EDITION to match the Wiktionary edition you have downloaded

set DICT=F:\Temp\wiktionary-dumps
set JAR_DIR=G:\Dropbox\Dictionary\wikt
set JAVA_HOME=C:\Usr\openjdk-16_windows-x64_bin\jdk-16

set EDITION=20210401
set LANG=en

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