@echo off
rem Wikt language definition parsing script
rem Joel Korhonen 2025-May-06
rem Before running, change either environment variables for your Windows user account,
rem or the variables below as needed. See "Running.txt".
rem
rem Each language has its own output folder, and a similarly named output file
rem in it: parselangs-xx-language_codes.csv 
rem Used for JAR_DIR below, since WIKT is the variable that may have been defined as an env variable already
if "%WIKT%"=="" set WIKT=G:\Dropbox\Dictionary\wikt
rem Set JAR_DIR to the location of the runnable JAR file.
set JAR_DIR=%WIKT%
rem Set JAVA_HOME to point to JDK main dir.
if "%JAVA_HOME%"=="" set JAVA_HOME=C:\Usr\jdk-24
rem This variable is used if this script is run directly.
rem   Change LANG to the language you use.
rem It is overridden by command line parameter %1 by language specific scripts.
rem set EDITION=20250401
set LANG=en
set LANGUCBEG=En
rem Language specific scripts, e.g. "StripNamespaces elwiktionary.cmd" call passing LANG as param
if not "%1"=="" set LANG=%1
if not "%2"=="" set LANGUCBEG=%2

rem chcp 65001
rem cls

SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET DATE.HOUR=%X:~8,2%
SET DATE.MINUTE=%X:~10,2%
SET DATE.SECOND=%X:~12,2%
SET NOW=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%-%DATE.HOUR%-%DATE.MINUTE%-%DATE.SECOND%

SET PROG="%JAR_DIR%\ParseLangs%LANGUCBEG%.jar"
SET JCLASS=wiktionary\to\xml\full\ParseLangs%LANGUCBEG% 
SET JAVA="%JAVA_HOME%\bin\java.exe"
rem SET INFILE="langs/Wikisanakirja-Luettelo_kielikoodeista.htm"
SET INFILE="langs/%LANG%/langs-in.html"
rem SET OUTFILE="%DICT%\%LANG%wiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml"
rem set OUTFILE="src/langs/parselangs-%LANG%-ALL-language_codes.csv"
set OUTFILE="%DICT%\parselangs-%LANG%-ALL-language_codes.csv"
SET UTF8=-Dfile.encoding=UTF8
SET MEM=-Xmx2600M
SET STCK=-Xss400M
rem cd /D "%JAR_DIR%"
echo %JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE% 
%JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE%
if %ERRORLEVEL% GTR 0 goto :errEnd
echo Ready
echo   Parsing langs started at %NOW%
echo   Params were:
echo     %JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE%
SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET DATE.HOUR=%X:~8,2%
SET DATE.MINUTE=%X:~10,2%
SET DATE.SECOND=%X:~12,2%
SET ENDTIME=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%-%DATE.HOUR%-%DATE.MINUTE%-%DATE.SECOND%
echo   Parsings langs ended at %ENDTIME%
goto end

:errEnd
echo ""
echo !!Ended in error!!
echo   Parsing langs started at %NOW%
echo   Params were:
echo     %JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE%
SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET DATE.HOUR=%X:~8,2%
SET DATE.MINUTE=%X:~10,2%
SET DATE.SECOND=%X:~12,2%
SET ENDTIME=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%-%DATE.HOUR%-%DATE.MINUTE%-%DATE.SECOND%
echo   Parsings langs ended at %ENDTIME%
echo !!Ended in error!!
pause
rem exit 8

:end