@echo off
rem Process the Finnish Wiktionary, all languages. Run this to start the conversion
rem Joel Korhonen 2021-May-22
rem Change EDITION to match with the Wiktionary edition you have downloaded
set EDITION=20250501

if not "%1"=="" goto :calledByReadStrippedSDALL

rem Do these only when this script is called directly by the user:
chcp 65001
cls
goto :mainPart

:calledByReadStrippedSDALL
set EDITION=%1

:mainPart
set LANG=ALL
set LANGCODE=fi
set OUTPUTLANGNAMES=true
rem True if only languages supplied in a language file are to be processed
set ONLYLANGUAGES=true
set WIKTCODE=fi

if "%WIKTGIT%"=="" set WIKTGIT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKTGIT%\Scripts
set CONTINFO_DIR=%SCRIPTS%\ReadStripped

rem cd /D %SCRIPTS%\ReadStripped
call "ReadStripped SD MAIN.cmd" %EDITION% 0 %LANG% %LANGCODE% %OUTPUTLANGNAMES% %ONLYLANGUAGES% %WIKTCODE%

:mainloop
rem cd /D %SCRIPTS%\ReadStripped
if not exist %CONTINFO_DIR%\continfo.txt goto ending
for /F %%i in (%CONTINFO_DIR%\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD MAIN.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %OUTPUTLANGNAMES% %ONLYLANGUAGES% %WIKTCODE%
goto mainloop

:ending