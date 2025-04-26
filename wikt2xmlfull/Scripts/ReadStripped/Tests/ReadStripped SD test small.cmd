@echo off
rem Joel Korhonen 2012-04-22
rem 2025-04-26 Updated the whole script.
rem Edit variables below, e.g. LANGCODE and INFILE, to match your test
if "%DICT%"=="" set DICT=G:\Temp\wiktionary-dumps
if "%WIKT%"=="" set WIKT=G:\Dropbox\Dictionary\wikt
set JAR_DIR=%WIKT%
if "%JAVA_HOME%"=="" set JAVA_HOME=C:\Usr\jdk-24
if "%WIKTSDOUT%"=="" set WIKTSDOUT=G:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict
rem Change EDITION to match the Wiktionary edition you have downloaded
set EDITION=20250401
set RESTARTATLINE=21033364
set LANG=ALL
rem set LANGCODE=en
set LANGCODE=sv
set OUTPUTLANGNAMES=true
set ONLYLANGUAGES=true
rem set WIKTCODE=en
set WIKTCODE=sv
chcp 65001
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
rem SET OUTFILE="%WIKTSDOUT%\wikt-%WIKTCODE%-%LANG%-%NOW%.txt"
SET OUTFILE=.\wikt-%WIKTCODE%-%LANG%-test-out.txt

rem set TESTFILE=sv-förmiddans
rem set INFILE=%WIKTGIT%\examples\%TESTFILE%.txt
rem SET OUTFILE=.\wikt-%WIKTCODE%-%LANG%-test-%TESTFILE%-out.txt

SET UTF8=-Dfile.encoding=UTF-8
SET MEM=-Xmx2600M
SET STCK=-Xss400M
SET OUTTYPE=Stardict

rem cd /D "%JAR_DIR%"

echo %JAVA% %UTF8% %MEM% %STCK% -jar %PROG% INFILE=%INFILE% OUTFILE=%OUTFILE% LANG=%LANG% OUTPUTLANGNAMES=%OUTPUTLANGNAMES% OUTTYPE=%OUTTYPE% RESTARTATLINE=%RESTARTATLINE% LANGCODE=%LANGCODE% ONLYLANGUAGES=%ONLYLANGUAGES% WIKTCODE=%WIKTCODE%
%JAVA% %UTF8% %MEM% %STCK% -jar %PROG% INFILE=%INFILE% OUTFILE=%OUTFILE% LANG=%LANG% OUTPUTLANGNAMES=%OUTPUTLANGNAMES% OUTTYPE=%OUTTYPE% RESTARTATLINE=%RESTARTATLINE% LANGCODE=%LANGCODE% ONLYLANGUAGES=%ONLYLANGUAGES% WIKTCODE=%WIKTCODE%

echo Ended with ERRORLEVEL: %ERRORLEVEL%
goto end

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