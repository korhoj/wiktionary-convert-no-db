@echo off
rem Joel Korhonen 2018-May-15
rem Change EDITION to match with the Wiktionary edition you have downloaded
set EDITION=20180420

rem This param is currently unused
rem set LANGNAME=English

set LANG=ALL
rem set LANGCODE=en
set LANGCODE=ALL
set METADATAENGLISH=true
rem Only languages supplied in a language file are to be processed
set ONLYLANGS=false

rem call "ReadStripped SD ALL.cmd" ALL ALL true
cd /D %WIKT%\Scripts
rem call "ReadStripped SD ALL.cmd" %EDITION% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGS%

goto startti
cd /D %WIKT%\Scripts
pause

for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
rem call "ReadStripped SD ALL restart.cmd" %RESTARTATLINE% ALL ALL true
call "ReadStripped SD ALL restart.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGS%
cd /D %WIKT%\Scripts

for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGS%
cd /D %WIKT%\Scripts

for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGS%
cd /D %WIKT%\Scripts

:startti
for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGS%
cd /D %WIKT%\Scripts

pause

for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGS%
cd /D %WIKT%\Scripts

pause

cd /D %WIKT%\Scripts
for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGS%
cd /D %WIKT%\Scripts

pause

for /F %%i in (..\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGS%

echo Finished.
pause