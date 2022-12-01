@echo off
rem Norwegian Wikt namespace stripping script
rem Joel Korhonen 2021-May-15
set EDITION=20221120
set LANG=no

set WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKT%

cd /D %SCRIPTS%
call "StripNamespaces ALL.cmd" %LANG% %EDITION%