@echo off
rem Finnish Wikt namespace stripping script
rem Joel Korhonen 2021-May-15
set EDITION=20240901
set LANG=fi

set WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKT%\Scripts

cd /D "%SCRIPTS%"\StripNamespaces
call "StripNamespaces ALL.cmd" %LANG% %EDITION%