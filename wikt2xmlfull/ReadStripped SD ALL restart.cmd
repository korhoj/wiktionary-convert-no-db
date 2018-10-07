@echo off
rem Joel Korhonen 2012-Jul-31
rem 2012-Sep-29 Use sysvar in paths
rem 2013-Nov-27 Add EDITION and STCK 
rem 2013-Nov-29 Add RESTARTATLINE
rem 2013-Dec-04 Call from "ReadStripped SD ALL runall.cmd" with argument to pass line to start from
rem 2013-Dec-10 LANG and LANGID passed as param. LANGID not relayed to program
rem 2014-08-09 Java 8
rem 2018-09-14 Java 8 u 181
set JAVA_HOME=C:\PROGRA~1\Java\jdk1.8.0_181
rem
rem Change EDITION to match with the Wiktionary edition you have downloaded
rem set EDITION=20170420
set EDITION=%1

set RESTARTATLINE=%2%
set LANG=%3
set LANGCODE=%4
set METADATAENGLISH=%5
rem Only languages supplied in a language file are to be processed
set ONLYLANGS=%6

SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET DATE.HOUR=%X:~8,2%
SET DATE.MINUTE=%X:~10,2%
SET DATE.SECOND=%X:~12,2%
SET NOW=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%-%DATE.HOUR%-%DATE.MINUTE%-%DATE.SECOND%

SET PROGDIR=%WIKT%
SET PROG="%PROGDIR%\ReadStripped.jar"
rem SET PROG="%PROGDIR%\ReadStripped.20161113.jar"
SET JCLASS=wiktionary\to\xml\full\ReadStripped
SET JAVA="%JAVA_HOME%\bin\java.exe"
rem SET INFILE=%DICT%\General\wiktionary\enwiktionary-%EDITION%-pages-articles.xml\stripped-%LANGID%.xml
SET INFILE="%DICT%\%LANGCODE%wiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml"
if LANG == "ALL" SET INFILE="%DICT%\enwiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml"
SET OUTFILE="%WIKT%\StarDict\OwnStardict\wikt-%LANG%-%LANGCODE%-%NOW%.txt"
if LANG == "ALL" SET OUTFILE="%WIKT%\StarDict\OwnStardict\wikt-en-%LANGCODE%-%NOW%.txt"
SET UTF8=-Dfile.encoding=UTF-8
SET MEM=-Xmx2600M
SET STCK=-Xss400M
SET OUTTYPE=Stardict

cd %PROGDIR%
chcp 65001

rem %JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE% %LANG% %METADATAENGLISH% %OUTTYPE% %RESTARTATLINE%
echo %JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE% %LANG% %METADATAENGLISH% %OUTTYPE% %RESTARTATLINE% %LANGCODE% %ONLYLANGS%
%JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE% %LANG% %METADATAENGLISH% %OUTTYPE% %RESTARTATLINE% %LANGCODE% %ONLYLANGS%
if %ERRORLEVEL% GTR 0 goto :virhe
echo Ready

if "%1" == "" pause
goto end

:virhe
echo Ended in error
pause
exit 8

:end