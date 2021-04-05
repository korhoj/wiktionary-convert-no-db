@echo off
rem Process only Western languages (western alphabet)
rem Joel Korhonen 2021-03-28
set EDITION=20210320
set WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKT%
set CONTINFO_DIR=%WIKT%

set LANG=Western
set LANGCODE=ALL

set METADATAENGLISH=true
rem Only languages supplied in a language file are to be processed
set ONLYLANGUAGES=true

echo "Starting after this pause"
pause

cd /D %SCRIPTS%
call "ReadStripped SD Western.cmd" %EDITION% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGUAGES%
pause

:mainloop
cd /D %SCRIPTS%
if not exist %CONTINFO_DIR%\continfo.txt goto ending
for /F %%i in (%CONTINFO_DIR%\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD Western restart.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGUAGES%
goto mainloop

:ending