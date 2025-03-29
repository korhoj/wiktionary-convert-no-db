@echo off
rem Process the Norwegian Wiktionary, Bokmål Norwegian only. Run this to start the conversion
rem Joel Korhonen 2021-May-22
rem Change EDITION to match with the Wiktionary edition you have downloaded
set EDITION=20250320
set LANG=ALL
set LANGCODE=nb
set OUTPUTLANGNAMES=true
rem True if only languages supplied in a language file are to be processed
set ONLYLANGUAGES=true
set WIKTCODE=nb

if "%WIKTGIT%"=="" set WIKTGIT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKTGIT%\Scripts
set CONTINFO_DIR=%SCRIPTS%\ReadStripped

rem cd /D %SCRIPTS%\ReadStripped
call "ReadStripped SD ALL.cmd" %EDITION% 0 %LANG% %LANGCODE% %OUTPUTLANGNAMES% %ONLYLANGUAGES% %WIKTCODE%

:mainloop
rem cd /D %SCRIPTS%\ReadStripped
if not exist %CONTINFO_DIR%\continfo.txt goto ending
for /F %%i in (%CONTINFO_DIR%\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %OUTPUTLANGNAMES% %ONLYLANGUAGES% %WIKTCODE%
goto mainloop

:ending