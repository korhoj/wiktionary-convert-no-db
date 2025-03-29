@echo off
rem Norwegian Nynorsk Wikt namespace stripping script
rem Joel Korhonen 2021-May-15
set EDITION=20250320
set LANG=nn

if "%WIKTGIT%"=="" set WIKTGIT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKTGIT%\Scripts

cd /D "%SCRIPTS%"\StripNamespaces
call "StripNamespaces ALL.cmd" %LANG% %EDITION%