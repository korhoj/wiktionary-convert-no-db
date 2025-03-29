@echo off
rem Hebrew Wikt namespace stripping script
rem Joel Korhonen 2022-Dec-01
set EDITION=20250320
set LANG=he

if "%WIKTGIT%"=="" set WIKTGIT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKTGIT%\Scripts

cd /D "%SCRIPTS%"\StripNamespaces
call "StripNamespaces ALL.cmd" %LANG% %EDITION%