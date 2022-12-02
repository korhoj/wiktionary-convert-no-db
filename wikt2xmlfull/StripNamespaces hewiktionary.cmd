@echo off
rem Hebrew Wikt namespace stripping script
rem Joel Korhonen 2022-Dec-01
set EDITION=20221120
set LANG=he

set WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKT%

cd /D %SCRIPTS%
call "StripNamespaces ALL.cmd" %LANG% %EDITION%