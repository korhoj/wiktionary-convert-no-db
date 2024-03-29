@echo off
rem Process the Swedish Wiktionary, Swedish only. Run this to start the conversion
rem Joel Korhonen 2021-May-22
rem Change EDITION to match with the Wiktionary edition you have downloaded
set EDITION=20230720
set LANG=sv
set LANGCODE=sv
set OUTPUTLANGNAMES=true
rem True if only languages supplied in a language file are to be processed
set ONLYLANGUAGES=true
set WIKTCODE=sv

set WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKT%
set CONTINFO_DIR=%WIKT%

cd /D %SCRIPTS%
call "ReadStripped SD ALL.cmd" %EDITION% 0 %LANG% %LANGCODE% %OUTPUTLANGNAMES% %ONLYLANGUAGES% %WIKTCODE%

:mainloop
cd /D %SCRIPTS%
if not exist %CONTINFO_DIR%\continfo.txt goto ending
for /F %%i in (%CONTINFO_DIR%\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %OUTPUTLANGNAMES% %ONLYLANGUAGES% %WIKTCODE%
goto mainloop

:ending