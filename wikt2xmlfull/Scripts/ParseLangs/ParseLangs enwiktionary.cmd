@echo off
rem English Wikt language definitions parsing script
rem Joel Korhonen 2025-June-25
chcp 65001
cls
set LANG=en
if "%WIKTGIT%"=="" set WIKTGIT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTDIR=%WIKTGIT%\Scripts\ParseLangs
cd /D "%SCRIPTDIR%"

SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET DATE.HOUR=%X:~8,2%
SET DATE.MINUTE=%X:~10,2%
SET DATE.SECOND=%X:~12,2%
SET NOW=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%-%DATE.HOUR%-%DATE.MINUTE%-%DATE.SECOND%

set INPATH="%WIKTGIT%/langs/%LANG%/dl"
rem set OUTFILE="%DICT%\parselangs-%LANG%-ALL-language_codes.csv"
rem set OUTFILE="langs/parselangs-%LANG%-ALL-language_codes.csv"
rem set OUTFILE=langs/en/parselangs-%LANG%-ALL-%CHAR%.csv
rem set OUTPATH=langs/en
set OUTPATH="%WIKTGIT%/langs/%LANG%/out"

echo Parsing with params: %INPATH% %OUTPATH%
py "%SCRIPTDIR%/ParseLangs %LANG%wiktionary.py" %INPATH% %OUTPATH%
set ERRLVL=%ERRORLEVEL%
echo Ready
echo   Parsing English Wiktionary langs started at %NOW%
echo   Parsed with params:
echo     %INPATH% %OUTPATH%
SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET DATE.HOUR=%X:~8,2%
SET DATE.MINUTE=%X:~10,2%
SET DATE.SECOND=%X:~12,2%
SET ENDTIME=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%-%DATE.HOUR%-%DATE.MINUTE%-%DATE.SECOND%
echo   Parsings English Wiktionary langs ended at %ENDTIME%
if %ERRLVL% GTR 0 goto :parseErrEnd
goto :parsedOk
:parseErrEnd
echo ""
echo !!Parsing ended in error!!
rem exit 8
goto :end
:parsedOk
call "ParseLangs %LANG%wiktionary-combine.cmd"
if %ERRLVL% GTR 0 goto :combErrEnd
goto :pauseSect
:combErrEnd
echo ""
echo !!Combining parsed files ended in error!!
rem exit 8
goto :end
:pauseSect
rem pause
:end