@echo off
rem Finnish Wikt language definitions parsing script
rem Joel Korhonen 2025-May-06
rem if not "%1"=="" goto :calledByParseLangsALL

rem Do these only when this script is called directly by the user:
chcp 65001
cls
rem goto :mainPart

rem :calledByParseLangsALL
rem set EDITION=%1

rem :mainPart
set LANG=fi
set LANGUCBEG=Fi
 if "%WIKTGIT%"=="" set WIKTGIT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKTGIT%\Scripts
cd /D "%SCRIPTS%"\ParseLangs
rem call "ParseLangs MAIN.cmd" %LANG% %EDITION%
call "ParseLangs MAIN.cmd" %LANG% %LANGUCBEG%
