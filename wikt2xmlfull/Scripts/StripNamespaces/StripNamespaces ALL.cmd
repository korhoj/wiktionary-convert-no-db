@echo off
rem Wikt namespace stripping script for processing all supported languages
rem Joel Korhonen 2025-04-23
rem Change the date to match the date of the dump files you have downloaded first 
if "%WIKTGIT%"=="" set WIKTGIT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKTGIT%\Scripts
cd /D "%SCRIPTS%"\StripNamespaces
chcp 65001
cls
set EDITION=20251001
call "StripNamespaces elwiktionary.cmd" %EDITION%

set EDITION=20250920
call "StripNamespaces fiwiktionary.cmd" %EDITION%

rem This should be commented out for now, he isn't supported
rem call "StripNamespaces hewiktionary.cmd" %EDITION%

call "StripNamespaces nnwiktionary.cmd" %EDITION%
call "StripNamespaces nowiktionary.cmd" %EDITION%

rem This should be commented out for now, sq isn't supported
rem call "StripNamespaces sqwiktionary.cmd"" %EDITION%

set EDITION=20251001
call "StripNamespaces svwiktionary.cmd" %EDITION%

rem I want to process this last, as it is the biggest one
set EDITION=20250920
call "StripNamespaces enwiktionary.cmd" %EDITION%
