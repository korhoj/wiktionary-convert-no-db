@echo off
rem Joel Korhonen 2021-May-22
if "%DICT%"=="" set DICT=G:\Temp\wiktionary-dumps
if "%WIKT%"=="" set WIKT=G:\Dropbox\Dictionary\wikt
set JAR_DIR=%WIKT%
if "%JAVA_HOME%"=="" set JAVA_HOME=C:\Usr\jdk-24
if "%WIKTSDOUT%"=="" set WIKTSDOUT=G:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict

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

rem cp 65001
rem cls

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
SET OUTFILE="%WIKTSDOUT%\wikt-%WIKTCODE%-%LANG%-%NOW%.txt"
SET UTF8=-Dfile.encoding=UTF-8
SET MEM=-Xmx2600M
SET STCK=-Xss400M
SET OUTTYPE=Stardict

rem cd /D "%JAR_DIR%"

rem -XX:+UseConcMarkSweepGC -XX:-UseGCOverheadLimit 
echo %JAVA% %UTF8% %MEM% %STCK% -jar %PROG% INFILE=%INFILE% OUTFILE=%OUTFILE% LANG=%LANG% OUTPUTLANGNAMES=%OUTPUTLANGNAMES% OUTTYPE=%OUTTYPE% RESTARTATLINE=%RESTARTATLINE% LANGCODE=%LANGCODE% ONLYLANGUAGES=%ONLYLANGUAGES% WIKTCODE=%WIKTCODE%
%JAVA% %UTF8% %MEM% %STCK% -jar %PROG% INFILE=%INFILE% OUTFILE=%OUTFILE% LANG=%LANG% OUTPUTLANGNAMES=%OUTPUTLANGNAMES% OUTTYPE=%OUTTYPE% RESTARTATLINE=%RESTARTATLINE% LANGCODE=%LANGCODE% ONLYLANGUAGES=%ONLYLANGUAGES% WIKTCODE=%WIKTCODE%
if %ERRORLEVEL% GTR 0 goto :errEnd
echo Ready
echo   Conversion started at %NOW%
echo   Params were:
echo     %JAVA% %UTF8% %MEM% %STCK% -jar %PROG% INFILE=%INFILE% OUTFILE=%OUTFILE% LANG=%LANG% OUTPUTLANGNAMES=%OUTPUTLANGNAMES% OUTTYPE=%OUTTYPE% RESTARTATLINE=%RESTARTATLINE% LANGCODE=%LANGCODE% ONLYLANGUAGES=%ONLYLANGUAGES% WIKTCODE=%WIKTCODE%
SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET DATE.HOUR=%X:~8,2%
SET DATE.MINUTE=%X:~10,2%
SET DATE.SECOND=%X:~12,2%
SET ENDTIME=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%-%DATE.HOUR%-%DATE.MINUTE%-%DATE.SECOND%
echo   Conversion ended at %ENDTIME%
goto end

:errEnd
echo ""
echo !!Ended in error!!
echo   Conversion started at %NOW%
echo   Params were:
echo     %JAVA% %UTF8% %MEM% %STCK% -jar %PROG% INFILE=%INFILE% OUTFILE=%OUTFILE% LANG=%LANG% OUTPUTLANGNAMES=%OUTPUTLANGNAMES% OUTTYPE=%OUTTYPE% RESTARTATLINE=%RESTARTATLINE% LANGCODE=%LANGCODE% ONLYLANGUAGES=%ONLYLANGUAGES% WIKTCODE=%WIKTCODE%
SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET DATE.HOUR=%X:~8,2%
SET DATE.MINUTE=%X:~10,2%
SET DATE.SECOND=%X:~12,2%
SET ENDTIME=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%-%DATE.HOUR%-%DATE.MINUTE%-%DATE.SECOND%
echo   Conversion ended at %ENDTIME%
echo !!Ended in error!!
pause
rem exit 8

:end