@echo off
rem Joel Korhonen 2018-May-15
rem For including only entries for languages specified in a csv file. Replace language codes.csv inside
rem ReadStripper.jar with the csv list you want, e.g. Western-language codes.csv. After
rem running the script, rename the output file to reflect the languages included

rem Change EDITION to match with the Wiktionary edition you have downloaded
set EDITION=20180420

rem This param is currently unused
rem set LANGNAME=Western

set LANG=en
set LANGCODE=Western
rem set LANGCODE=ALL
set METADATAENGLISH=true
rem Only languages supplied in a language file are to be processed
set ONLYLANGS=true

cd /D %WIKT%\Scripts
rem call "ReadStripped SD ALL.cmd" ALL ALL true
call "ReadStripped SD ALL.cmd" %EDITION% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGS%
cd /D %WIKT%\Scripts

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

echo Finished.
pause