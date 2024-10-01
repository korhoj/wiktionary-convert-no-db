@echo off
rem Albanian Wikt namespace stripping script
rem Joel Korhonen 2021-May-15
set EDITION=20230720
set LANG=sq

if "%WIKTGIT%"=="" set WIKTGIT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKTGIT%\Scripts

cd /D "%SCRIPTS%"\StripNamespaces
call "StripNamespaces ALL.cmd" %LANG% %EDITION%