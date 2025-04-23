@echo off
rem Wikt namespace stripping script for processing all supported languages
rem Joel Korhonen 2025-04-23
rem Change the date to match the date of the dump files you have downloaded first 
set EDITION=20250401
if "%WIKTGIT%"=="" set WIKTGIT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKTGIT%\Scripts
cd /D "%SCRIPTS%"\StripNamespaces
chcp 65001
cls
call "StripNamespaces elwiktionary.cmd" %EDITION%
call "StripNamespaces fiwiktionary.cmd" %EDITION%
call "StripNamespaces hewiktionary.cmd" %EDITION%
call "StripNamespaces nnwiktionary.cmd" %EDITION%
call "StripNamespaces nowiktionary.cmd" %EDITION%
rem call "StripNamespaces sqwiktionary.cmd"" %EDITION%
call "StripNamespaces svwiktionary.cmd" %EDITION%
rem I want to process this last, as it is the biggest one
call "StripNamespaces enwiktionary.cmd" %EDITION%
