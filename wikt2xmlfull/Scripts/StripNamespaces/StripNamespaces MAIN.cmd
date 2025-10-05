@echo off
rem Joel Korhonen 2021-May-15
rem Strips comments etc. namespaces not containing Wiktionary entries from the input file.
rem Processes the Wiktionary given as parameter or by default the English Wiktionary.
rem Optionally the edition may be specified as the second parameter.
rem
rem Before running, change either environment variables for your Windows user account,
rem or the variables below as needed. See "Running.txt".
rem
rem Set DICTDUMPS to the location under which you uncompressed the Wiktionary edition(s)
rem you have downloaded and uncompressed
rem E.g. for en, you should uncompress enwiktionary-%EDITION%-pages-articles.xml.bz2
rem into the folder %DICTDUMPS%\enwiktionary-%EDITION%-pages-articles.xml\
rem The last mentioned folder will also be used as the output folder.
rem Each language has its own output folder, and a similarly named output file
rem in it: stripped-ALL.xml 
if "%DICTDUMPS%"=="" set DICTDUMPS=G:\Temp\wiktionary-dumps
rem Used for JAR_DIR below, since WIKT is the variable that may have been defined as an env variable already
if "%WIKT%"=="" set WIKT=G:\Dropbox\Dictionary\wikt
rem Set JAR_DIR to the location of the runnable JAR file.
set JAR_DIR=%WIKT%
rem Set JAVA_HOME to point to JDK main dir.
if "%JAVA_HOME%"=="" set JAVA_HOME=C:\Usr\jdk-24
rem These two variables are used if this script is run directly.
rem   Change EDITION to match the Wiktionary edition you have downloaded.
rem   Change LANG to the languager you use.
rem They are overridden by command line parameters %1 and %2 by language specific scripts.
set EDITION=20250401
set LANG=en
rem Language specific scripts, e.g. "StripNamespaces elwiktionary.cmd" call passing LANG as param
if not "%1"=="" set LANG=%1
if not "%2"=="" set EDITION=%2

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

SET PROG="%JAR_DIR%\StripNamespaces.jar"
SET JCLASS=wiktionary\to\xml\full\StripNamespaces
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET INFILE="%DICTDUMPS%\%LANG%wiktionary-%EDITION%-pages-articles.xml\%LANG%wiktionary-%EDITION%-pages-articles.xml"
SET OUTFILE="%DICTDUMPS%\%LANG%wiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml"
SET UTF8=-Dfile.encoding=UTF8
SET MEM=-Xmx2600M
SET STCK=-Xss400M

echo %JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE% 
%JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE%
echo Ready
echo   Conversion started at %NOW%
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
echo   Conversion ended at %ENDTIME%
if %ERRORLEVEL% EQU 0 goto :end

:errEnd
echo ""
echo !!Ended in error!! ERRORLEVEL == %ERRORLEVEL% 
pause
exit /B 8

:end