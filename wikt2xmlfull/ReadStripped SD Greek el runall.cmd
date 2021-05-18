@echo off
rem Greek Wikt main script, run this to start the conversion
rem Joel Korhonen 2021-Apr-05
set EDITION=20210501

rem This param is currently unused
rem set LANGNAME=Greek

set WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKT%
SET CONTINFO_DIR=%WIKT%

set LANG=ALL
set LANGCODE=el
rem set LANGCODE=ALL
set METADATAENGLISH=true
rem True if only languages supplied in a language file are to be processed
rem set ONLYLANGUAGES=false
set ONLYLANGUAGES=true
set WIKTCODE=el

echo "Starting after this pause"
pause

rem Uncomment only when testing
rem goto mainloop

cd /D %SCRIPTS%
call "ReadStripped SD Greek ALL.cmd" %EDITION% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGUAGES% %WIKTCODE%

:mainloop
cd /D %SCRIPTS%
if not exist %CONTINFO_DIR%\continfo.txt goto ending
for /F %%i in (%CONTINFO_DIR%\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD Greek restart.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGUAGES% %WIKTCODE%
goto mainloop

:ending