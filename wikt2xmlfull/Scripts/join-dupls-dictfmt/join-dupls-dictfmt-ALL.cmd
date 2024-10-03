@echo off
rem Joel Korhonen 2024-10-03
set convLangCode=%1
set convLang=%2
rem set convDate=%3
if "%JAVA_HOME%"=="" set JAVA_HOME=C:\Usr\jdk-23
SET JAVA="%JAVA_HOME%\bin\java.exe"
if "%WIKT%"=="" set WIKT=G:\Dropbox\Dictionary\wikt
set JAR_DIR=%WIKT%
SET PROG=%JAR_DIR%\JoinDefinitions.jar
SET JCLASS=wiktionary\to\xml\full\JoinDefinitions
if "%DUPLS_OUT_DIR%"=="" set DUPLS_OUT_DIR=G:\Users\Joel\OwnStarDict-ready
SET UTF8=-Dfile.encoding=UTF8
SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET DATE.HOUR=%X:~8,2%
SET DATE.MINUTE=%X:~10,2%
SET DATE.SECOND=%X:~12,2%
SET NOW=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%-%DATE.HOUR%-%DATE.MINUTE%-%DATE.SECOND%
SET convDate=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%
SET INFILE=%DUPLS_OUT_DIR%\wikt-%convLangCode%-%convLang%-%convDate%.txt
SET OUTFILE=%DUPLS_OUT_DIR%\wikt-%convLangCode%-%convLang%-dictfmt-dupls-joined.txt
echo %JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
if %ERRORLEVEL% GTR 0 goto :errEnd
echo Ready
echo   join-dupls started at %NOW%
echo   Params were:
echo     %JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET DATE.HOUR=%X:~8,2%
SET DATE.MINUTE=%X:~10,2%
SET DATE.SECOND=%X:~12,2%
SET ENDTIME=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%-%DATE.HOUR%-%DATE.MINUTE%-%DATE.SECOND%
echo   join-dupls ended at %ENDTIME%
goto end
:errEnd
echo ""
echo !!Ended in error!!
echo   join-dupls started at %NOW%
echo   Params were:
echo     %JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET DATE.HOUR=%X:~8,2%
SET DATE.MINUTE=%X:~10,2%
SET DATE.SECOND=%X:~12,2%
SET ENDTIME=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%-%DATE.HOUR%-%DATE.MINUTE%-%DATE.SECOND%
echo   join-dupls ended at %ENDTIME%
echo !!Ended in error!!
pause
rem exit 8
:end