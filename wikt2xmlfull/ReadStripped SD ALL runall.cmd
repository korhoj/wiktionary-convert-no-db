@echo off
rem Joel Korhonen 2014-May-17
call "ReadStripped SD ALL.cmd" ALL ALL true
cd /D %WIKT%\Scripts

for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %RESTARTATLINE% ALL ALL true
cd /D %WIKT%\Scripts

for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %RESTARTATLINE% ALL ALL true
cd /D %WIKT%\Scripts

for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %RESTARTATLINE% ALL ALL true
cd /D %WIKT%\Scripts

for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %RESTARTATLINE% ALL ALL true

pause