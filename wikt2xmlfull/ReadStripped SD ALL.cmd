@echo off
rem Joel Korhonen 2021-May-22
set DICT=F:\Temp\wiktionary-dumps
set JAR_DIR=G:\Dropbox\Dictionary\wikt
set JAVA_HOME=C:\Usr\jdk-19
set OUT_DIR=G:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict
set WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull

if "%1"=="" goto errEnd

rem Pass as param the EDITION of the Wiktionary edition you have downloaded
set EDITION=%1
set RESTARTATLINE=%2
set LANG=%3
set LANGCODE=%4
set OUTPUTLANGNAMES=%5
rem True if only languages supplied in a language file are to be processed
set ONLYLANGUAGES=%6
set WIKTCODE=%7

echo "EDITION: %EDITION%"
echo "RESTARTATLINE: %RESTARTATLINE%"
echo "LANG: %LANG%"
echo "LANGCODE: %LANGCODE%"
echo "OUTPUTLANGNAMES: %OUTPUTLANGNAMES%"
echo "ONLYLANGUAGES: %ONLYLANGUAGES%"
echo "WIKTCODE: %WIKTCODE%"

SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET DATE.HOUR=%X:~8,2%
SET DATE.MINUTE=%X:~10,2%
SET DATE.SECOND=%X:~12,2%
SET NOW=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%-%DATE.HOUR%-%DATE.MINUTE%-%DATE.SECOND%

SET PROG="%JAR_DIR%\ReadStripped.jar"
SET JCLASS=wiktionary\to\xml\full\ReadStripped
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET INFILE="%DICT%\%WIKTCODE%wiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml"
SET OUTFILE="%OUT_DIR%\wikt-%WIKTCODE%-%LANG%-%NOW%.txt"
SET UTF8=-Dfile.encoding=UTF-8
SET MEM=-Xmx2600M
SET STCK=-Xss400M
SET OUTTYPE=Stardict

cd %JAR_DIR%
chcp 65001

rem -XX:+UseConcMarkSweepGC -XX:-UseGCOverheadLimit 
echo %JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE% %LANG% %OUTPUTLANGNAMES% %OUTTYPE% %RESTARTATLINE% %LANGCODE% %ONLYLANGUAGES% %WIKTCODE%
%JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE% %LANG% %OUTPUTLANGNAMES% %OUTTYPE% %RESTARTATLINE% %LANGCODE% %ONLYLANGUAGES% %WIKTCODE%
if %ERRORLEVEL% GTR 0 goto :errEnd
echo Ready
goto end

:errEnd
echo Ended in error
pause
exit 8

:end