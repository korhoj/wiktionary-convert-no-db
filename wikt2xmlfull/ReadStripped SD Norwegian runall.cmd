@echo off
rem Joel Korhonen 2014-Sep-13
call "ReadStripped SD ALL.cmd" Norwegian no true
cd /D %WIKT%\Scripts

set LANG=Norsk
for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %RESTARTATLINE% %LANG% no true false
cd /D %WIKT%\Scripts

for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %RESTARTATLINE% %LANG% no true false
cd /D %WIKT%\Scripts

for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %RESTARTATLINE% %LANG% no true false
cd /D %WIKT%\Scripts

for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %RESTARTATLINE% %LANG% no true false

pause
