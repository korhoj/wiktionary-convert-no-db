@echo off
rem Joel Korhonen 2013-Dec-12
rem 2015-05-01 Java 8. Pass language code param
rem set JAVA_HOME=C:\PROGRA~1\Java\jdk1.8.0_102
rem
rem Change EDITION to match with the Wiktionary edition you have downloaded
set EDITION=20161101
rem Finnish
set LANG=Suomi
set LANGID=ALL
set METADATAENGLISH=false
set LANGCODE=fi

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
SET PROG=%PROGDIR%\ReadStripped.jar
SET JCLASS=wiktionary\to\xml\full\ReadStripped
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET INFILE=%DICT%\%LANGCODE%wiktionary-%EDITION%-pages-articles.xml\stripped-ALL.xml
SET OUTFILE=%WIKT%\StarDict\OwnStardict\wikt-%LANGCODE%-ALL-%NOW%.txt
SET UTF8=-Dfile.encoding=UTF-8
SET MEM=-Xmx2600M
SET STCK=-Xss400M
SET OUTTYPE=Stardict

cd %PROGDIR%
chcp 65001

%JAVA% %UTF8% %MEM% %STCK% -jar %PROG% %INFILE% %OUTFILE% %LANG% %METADATAENGLISH% %OUTTYPE% 0 %LANGCODE%
if %ERRORLEVEL% GTR 0 goto :virhe
echo Ready

if "%1" == "" pause
goto end

:virhe
echo Ended in error
pause
exit 8

:end