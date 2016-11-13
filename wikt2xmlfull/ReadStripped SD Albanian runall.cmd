@echo off
rem Process only Albanian
rem Joel Korhonen 2016-Nov-07
SET LANG=ALL
SET LANGCODE=sq
SET SCRIPTDIR=C:\Usr\WiktionaryConvert\wikt2xmlfull
SET PROGDIR=%WIKT%
cd /D %PROGDIR%
call "%SCRIPTDIR%\ReadStripped SD Albanian.cmd" %LANG% %LANGCODE% true false

pause

rem cd /D %PROGDIR%
rem for /F %%i in (%PROGDIR%\continfo.txt) do @set RESTARTATLINE=%%i
rem call "%SCRIPTDIR%\ReadStripped SD Albanian restart.cmd" %RESTARTATLINE% %LANG% %LANGCODE% true false

rem pause

rem cd /D %PROGDIR%
rem for /F %%i in (%PROGDIR%\continfo.txt) do @set RESTARTATLINE=%%i
rem call "%SCRIPTDIR%\ReadStripped SD Albanian restart.cmd" %RESTARTATLINE% %LANG% %LANGCODE% true false

rem pause