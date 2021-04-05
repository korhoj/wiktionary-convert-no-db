@echo off
rem Joel Korhonen 2018-May-15
rem For including only entries for languages specified in a csv file. Replace language codes.csv inside
rem ReadStripper.jar with the csv list you want, e.g. Western-language codes.csv. After
rem running the script, rename the output file to reflect the languages included
rem Change EDITION to match with the Wiktionary edition you have downloaded
rem 2021-03-27 Updated edition
set EDITION=20210320

set WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTS=%WIKT%
set CONTINFO_DIR=%WIKT%

rem This param is currently unused
rem set LANGNAME=English

rem 20191109 Seems to also work, but not sure why and if in all situations
rem set LANG=ALL

rem 20191109 Testing whether this works instead of ALL
set LANG=en
set LANGCODE=Western
set METADATAENGLISH=true
rem Only languages supplied in a language file are to be processed
set ONLYLANGUAGES=true

echo "Starting after this pause"
pause

rem Keep the following line commented out normally
rem goto mainloop

cd /D %SCRIPTS%
call "ReadStripped SD ALL.cmd" %EDITION% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGUAGES%
pause

:mainloop
cd /D %SCRIPTS%
if not exist %CONTINFO_DIR%\continfo.txt goto ending
for /F %%i in (%CONTINFO_DIR%\continfo.txt) do @set RESTARTATLINE=%%i
call "ReadStripped SD ALL restart.cmd" %EDITION% %RESTARTATLINE% %LANG% %LANGCODE% %METADATAENGLISH% %ONLYLANGUAGES%
goto mainloop

:ending