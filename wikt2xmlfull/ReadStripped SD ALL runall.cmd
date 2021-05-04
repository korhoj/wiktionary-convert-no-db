@echo off
rem Joel Korhonen 2018-May-15
rem Change EDITION to match with the Wiktionary edition you have downloaded
rem 2021-04-07 Updated edition
set EDITION=20210420

rem This param is currently unused
rem set LANGNAME=English

set WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKT%
SET CONTINFO_DIR=%WIKT%

set LANG=ALL
rem set LANGCODE=en
set LANGCODE=ALL
set METADATAENGLISH=true
rem Only languages supplied in a language file are to be processed
set ONLYLANGUAGES=false

chcp 65001

rem echo "Starting after this pause"
rem pause

rem Normally the following line is kept in comments. Uncommented mainly when testing restarting
rem goto mainloop

cd /D %SCRIPTS%
call "ReadStripped SD ALL.cmd" %EDITION% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGUAGES%

:mainloop
cd /D %SCRIPTS%
if not exist %CONTINFO_DIR%\continfo.txt goto ending
for /F %%i in (%CONTINFO_DIR%\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGUAGES%
goto mainloop

:ending