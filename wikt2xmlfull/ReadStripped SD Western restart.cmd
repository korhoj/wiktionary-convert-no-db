@echo off
rem Process only Western languages (western alphabet)
rem Joel Korhonen 2012-Jul-31
rem 2012-Sep-29 Use sysvar in paths
rem 2013-Nov-27 Add EDITION and STCK 
rem 2013-Nov-29 Add RESTARTATLINE
rem 2013-Dec-04 Call from "ReadStripped SD ALL runall.cmd" with argument to pass line to start from
rem 2013-Dec-10 LANG and LANGID passed as param. LANGID not relayed to program
rem 2014-08-09 Java 8
rem set JAVA_HOME=C:\PROGRA~1\Java\jdk1.8.0_102
rem
set DICT=F:\Temp\wiktionary-dumps
set JAVA_HOME=C:\Usr\openjdk-16_windows-x64_bin\jdk-16
set JAR_DIR=G:\Dropbox\Dictionary\wikt
set OUT_DIR=G:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict
set WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
rem
rem Pass as param the EDITION of the Wiktionary edition you have downloaded
set EDITION=%1

set RESTARTATLINE=%2

rem SET LANG="ALL"
set LANG=%3

rem set LANGCODE=Western
set LANGCODE=%3

rem SET LANGID=ALL
rem set LANGID=%4

set METADATAENGLISH=%4
rem Only languages supplied in a language file are to be processed
set ONLYLANGUAGES=%5

SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET DATE.HOUR=%X:~8,2%
SET DATE.MINUTE=%X:~10,2%
SET DATE.SECOND=%X:~12,2%
SET NOW=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%-%DATE.HOUR%-%DATE.MINUTE%-%DATE.SECOND%

SET PROG=%JAR_DIR%\ReadStripped.jar
if "%LANGCODE%" == "Western" SET PROG="%PROGDIR%\ReadStripped_%LANGCODE%.jar"
SET JCLASS=wiktionary\to\xml\full\ReadStripped
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET INFILE=%DICT%\enwiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml
rem SET INFILE="%DICT%\%LANGCODE%wiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml"
if "%LANG%" == "ALL" SET INFILE="%DICT%\enwiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml"

SET OUTFILE="%OUT_DIR%\wikt-%LANG%-%LANGCODE%-%NOW%.txt"
if "%LANG%" == "ALL" SET OUTFILE="%OUT_DIR%\wikt-en-%LANGCODE%-%NOW%.txt"

SET UTF8=-Dfile.encoding=UTF-8
SET MEM=-Xmx2600M
SET STCK=-Xss400M
SET OUTTYPE=Stardict

cd %JAR_DIR%
chcp 65001

echo %JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE% %LANG% %METADATAENGLISH% %OUTTYPE% %RESTARTATLINE% %LANGCODE% %ONLYLANGUAGES%
%JAVA% %UTF8% %MEM% %STCK% -jar "%PROG%" %INFILE% %OUTFILE% %LANG% %METADATAENGLISH% %OUTTYPE% %RESTARTATLINE% %LANGCODE% %ONLYLANGUAGES%
if %ERRORLEVEL% GTR 0 goto :virhe
echo Ready

if "%1" == "" pause
goto end

:virhe
echo Ended in error
pause
exit 8

:end