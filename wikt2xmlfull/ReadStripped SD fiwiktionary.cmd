@echo off
rem Process the Finnish Wiktionary. No separate restart/runall script as the Wikt is enough small to be
rem processed in one run.
rem Joel Korhonen 2021-May-15
set DICT=F:\Temp\wiktionary-dumps
set JAR_DIR=G:\Dropbox\Dictionary\wikt
set JAVA_HOME=C:\Usr\openjdk-16_windows-x64_bin\jdk-16
set OUT_DIR=G:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict
set WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
rem Change EDITION to match with the Wiktionary edition you have downloaded
set EDITION=20210501
rem Finnish
rem set LANG=Suomi
set LANG=fi
set LANGID=ALL
set METADATAENGLISH=false
set LANGCODE=fi
rem True if only languages supplied in a language file are to be processed
set ONLYLANGUAGES=false

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
rem if "%LANGCODE%" == "fi" SET PROG="%JAR_DIR%\ReadStripped_%LANGCODE%.jar"
SET JCLASS=wiktionary\to\xml\full\ReadStripped
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET INFILE="%DICT%\%LANGCODE%wiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml"
if "%LANG%" == "ALL" SET INFILE="%DICT%\%LANGCODE%wiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml"
SET OUTFILE="%OUT_DIR%\wikt-%LANG%-%LANGCODE%-%NOW%.txt"
if "%LANG%" == "ALL" SET OUTFILE="%OUT_DIR%\wikt-%LANGCODE%-%LANGCODE%-%NOW%.txt"
SET UTF8=-Dfile.encoding=UTF-8
SET MEM=-Xmx2600M
SET STCK=-Xss400M
SET OUTTYPE=Stardict

cd %PROGDIR%
chcp 65001

echo %JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE% %LANG% %METADATAENGLISH% %OUTTYPE% 0 %LANGCODE% %ONLYLANGUAGES%
%JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE% %LANG% %METADATAENGLISH% %OUTTYPE% 0 %LANGCODE% %ONLYLANGUAGES%
if %ERRORLEVEL% GTR 0 goto :errEnd
echo Ready
goto end

:errEnd
echo Ended in error
pause
exit 8

:end