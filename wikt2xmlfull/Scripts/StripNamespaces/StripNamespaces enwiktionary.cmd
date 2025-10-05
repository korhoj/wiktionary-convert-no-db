@echo off
rem English Wikt namespace stripping script
rem Joel Korhonen 2025-Apr-23
rem 2025-04-23 Either change EDITION below and run this script to use it,
rem or change EDITION to "StripNamespaces ALL.cmd" and run it, for
rem calling each language specific script, such as this one.
set EDITION=20250420

if not "%1"=="" goto :calledByStripNamespacesALL

rem Do these only when this script is called directly by the user:
chcp 65001
cls
goto :mainPart

:calledByStripNamespacesALL
set EDITION=%1

:mainPart
set LANG=en
if "%WIKTGIT%"=="" set WIKTGIT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKTGIT%\Scripts
cd /D "%SCRIPTS%"\StripNamespaces
call "StripNamespaces MAIN.cmd" %LANG% %EDITION%