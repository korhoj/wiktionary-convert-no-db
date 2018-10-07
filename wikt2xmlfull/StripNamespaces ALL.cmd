@echo off
rem Process all languages
rem Joel Korhonen 2012-Jul-27
rem 2012-Sep-29 Use sysvar in paths
rem 2013-Oct-24 Changed DICT var path
rem 2013-Nov-27 Add EDITION
rem 2014-08-09 Java 8
rem set JAVA_HOME=C:\PROGRA~1\Java\jdk1.8.0_102
rem Before running, set JAVA_HOME=C:\PROGRA~1\Java\jdk1.8.0_xxx in your environment settings
rem Set WIKT to location of the runnable JAR file
rem Set DICT to location under which you uncompressed the Wiktionary edition you have downloaded
rem e.g. DICT=C:\Temp\enwiktionary\
rem   having uncompressed enwiktionary-%EDITION%-pages-articles.xml.zip to
rem   C:\Temp\enwiktionaryenwiktionary-%EDITION%-pages-articles.xml
rem Then change EDITION to match with the Wiktionary edition you have downloaded
rem 2018-10-03 Updated edition
set EDITION=20181001

SET PROGDIR=%WIKT%
SET PROG=%PROGDIR%\StripNamespaces.jar
SET JCLASS=wiktionary\to\xml\full\StripNamespaces
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET INFILE=%DICT%\enwiktionary-%EDITION%-pages-articles.xml\enwiktionary-%EDITION%-pages-articles.xml
SET OUTFILE=%DICT%\enwiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml
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