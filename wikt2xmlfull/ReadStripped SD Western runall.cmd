@echo off
rem Process only Western languages (western alphabet)
rem Joel Korhonen 2014-May-17
SET LANG="Western"
SET SCRIPTDIR=C:\Usr\WiktionaryConvert\wikt2xmlfull
SET PROGDIR=%WIKT%
cd /D %PROGDIR%
call "%SCRIPTDIR%\ReadStripped SD Western.cmd" %LANG% ALL true true

pause

cd /D %PROGDIR%
for /F %%i in (%PROGDIR%\continfo.txt) do @set RESTARTATLINE=%%i
call "%SCRIPTDIR%\ReadStripped SD Western restart.cmd" %RESTARTATLINE% %LANG% ALL true true

pause

cd /D %PROGDIR%
for /F %%i in (%PROGDIR%\continfo.txt) do @set RESTARTATLINE=%%i
call "%SCRIPTDIR%\ReadStripped SD Western restart.cmd" %RESTARTATLINE% %LANG% ALL true true

pause

cd /D %PROGDIR%
for /F %%i in (%PROGDIR%\continfo.txt) do @set RESTARTATLINE=%%i
call "%SCRIPTDIR%\ReadStripped SD Western restart.cmd" %RESTARTATLINE% %LANG% ALL true true

pause

cd /D %PROGDIR%
for /F %%i in (%PROGDIR%\continfo.txt) do @set RESTARTATLINE=%%i
call "%SCRIPTDIR%\ReadStripped SD Western restart.cmd" %RESTARTATLINE% %LANG% ALL true true

pause

rem cd /D %PROGDIR%
rem for /F %%i in (%PROGDIR%\continfo.txt) do @set RESTARTATLINE=%%i
rem call "%SCRIPTDIR%\ReadStripped SD Western restart.cmd" %RESTARTATLINE% %LANG% ALL true true

rem pause