@echo off
rem Process Norwegian Wiktionary
rem Joel Korhonen 2013-Dec-12
rem 2015-04-30 Java 8
rem set JAVA_HOME=C:\PROGRA~1\Java\jdk1.8.0_102
rem
rem Before running, set JAVA_HOME=C:\PROGRA~1\Java\jdk1.8.0_xxx in your environment settings
rem Set WIKT to location of the runnable JAR file
rem Set DICT to location under which you uncompressed the Wiktionary edition you have downloaded
rem e.g. DICT=C:\Temp\enwiktionary\
rem   having uncompressed enwiktionary-%EDITION%-pages-articles.xml.zip to
rem   C:\Temp\enwiktionaryenwiktionary-%EDITION%-pages-articles.xml
rem Then change EDITION to match with the Wiktionary edition you have downloaded
set EDITION=20161101
set LANG=no

SET PROGDIR=%WIKT%
SET PROG=%PROGDIR%\StripNamespaces.jar
SET JCLASS=wiktionary\to\xml\full\StripNamespaces
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET INFILE=%DICT%\%LANG%wiktionary-%EDITION%-pages-articles.xml\%LANG%wiktionary-%EDITION%-pages-articles.xml
SET OUTFILE=%DICT%\%LANG%wiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml
SET UTF8=-Dfile.encoding=UTF8

cd %PROGDIR%
chcp 65001

%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
if %ERRORLEVEL% GTR 0 goto :virhe
echo Ready

if "%1" == "" pause
goto end

:virhe
echo Ended in error
pause
exit 8

:end