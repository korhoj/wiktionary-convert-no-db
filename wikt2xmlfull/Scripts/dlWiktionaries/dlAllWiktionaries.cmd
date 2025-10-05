@echo off
rem DlAllWiktionaries.cmd
rem Downloads the newest dump for all supported Wiktionaries 
rem  Joel Korhonen 2025-Jun-23
rem In Windows, in a cmd shell, run like this:
rem  %WIKTGIT%\Scripts\dlWiktionaries\dlAllWiktionaries.cmd
rem Where WIKTGIT is e.g.: C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
py %WIKTGIT%\Scripts\dlWiktionaries\dlWiktionary.py el
if %ERRORLEVEL% NEQ 0 goto :errEnd
py %WIKTGIT%\Scripts\dlWiktionaries\dlWiktionary.py en
if %ERRORLEVEL% NEQ 0 goto :errEnd
py %WIKTGIT%\Scripts\dlWiktionaries\dlWiktionary.py fi
if %ERRORLEVEL% NEQ 0 goto :errEnd
py %WIKTGIT%\Scripts\dlWiktionaries\dlWiktionary.py nn
if %ERRORLEVEL% NEQ 0 goto :errEnd
py %WIKTGIT%\Scripts\dlWiktionaries\dlWiktionary.py no
if %ERRORLEVEL% NEQ 0 goto :errEnd
py %WIKTGIT%\Scripts\dlWiktionaries\dlWiktionary.py sv
if %ERRORLEVEL% NEQ 0 goto :errEnd
goto end
:errEnd
echo ""
echo !!Ended in error!! ERRORLEVEL == %ERRORLEVEL%
:end
