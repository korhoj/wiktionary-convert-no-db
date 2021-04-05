@echo off
rem Process Greek Wiktionary
rem Joel Korhonen 2021-Apr-05
rem Before running, set JAVA_HOME to point to JDK main dir, e.g. C:\Usr\openjdk-16_windows-x64_bin\jdk-16
rem Set DICT to location under which you uncompressed the Wiktionary edition you have downloaded
rem e.g. DICT=C:\Temp\elwiktionary\
rem   having uncompressed elwiktionary-%EDITION%-pages-articles.xml.bz2 to
rem   C:\Temp\elwiktionary-%EDITION%-pages-articles.xml
rem Set WIKT to location of the runnable JAR file
rem Then change EDITION to match with the Wiktionary edition you have downloaded
set DICT=F:\Temp\wiktionary-dumps
set JAR_DIR=G:\Dropbox\Dictionary\wikt
set JAVA_HOME=C:\Usr\openjdk-16_windows-x64_bin\jdk-16
set WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull

set EDITION=20210401

SET PROGDIR=%JAR_DIR%
SET PROG=%PROGDIR%\StripNamespaces.jar
SET JCLASS=wiktionary\to\xml\full\StripNamespaces
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET INFILE=%DICT%\elwiktionary-%EDITION%-pages-articles.xml\elwiktionary-%EDITION%-pages-articles.xml
SET OUTFILE=%DICT%\elwiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml
SET UTF8=-Dfile.encoding=UTF8
SET MEM=-Xmx2600M
SET STCK=-Xss400M

cd %PROGDIR%
chcp 65001

%JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE%
if %ERRORLEVEL% GTR 0 goto :virhe
echo Ready

if "%1" == "" pause
goto end

:virhe
echo Ended in error
pause
exit 8

:end