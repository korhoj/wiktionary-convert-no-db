@echo off
rem Norwegian Nynorsk Wikt namespace stripping script
rem Joel Korhonen 2021-May-15
rem 2025-04-23 Either change EDITION below and run this script to use it,
rem or change EDITION to "StripNamespaces ALL.cmd" and run it, for
rem calling each language specific script, such as this one.
set EDITION=20250401

if not "%1"=="" goto :calledByStripNamespacesALL

rem Do these only when this script is called directly by the user:
chcp 65001
cls
goto :mainPart

:calledByStripNamespacesALL
set EDITION=%1

:mainPart
set LANG=nn
if "%WIKTGIT%"=="" set WIKTGIT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKTGIT%\Scripts
cd /D "%SCRIPTS%"\StripNamespaces
call "StripNamespaces MAIN.cmd" %LANG% %EDITION%