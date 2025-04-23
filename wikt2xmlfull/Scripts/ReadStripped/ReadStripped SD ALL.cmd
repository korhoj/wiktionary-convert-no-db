@echo off
rem Process the Wiktionaries for all supported languages. 
rem Joel Korhonen 2025-Apr-23
rem Change the date to match the date of the dump files you have downloaded first 
set EDITION=20250401
chcp 65001
cls
call "ReadStripped SD el-ALL.cmd" %EDITION%
call "ReadStripped SD el-el.cmd" %EDITION%
call "ReadStripped SD en-ALL.cmd" %EDITION%
call "ReadStripped SD en-en.cmd" %EDITION%
call "ReadStripped SD en-Western_Greek_Slavonic.cmd" %EDITION%
call "ReadStripped SD en-Western.cmd" %EDITION%
call "ReadStripped SD fi-ALL.cmd" %EDITION%
call "ReadStripped SD fi-fi.cmd" %EDITION%
rem call "ReadStripped SD nb-ALL.cmd" %EDITION%
rem call "ReadStripped SD nb-nb.cmd" %EDITION%
call "ReadStripped SD nn-ALL.cmd" %EDITION%
call "ReadStripped SD nn-nn.cmd" %EDITION%
call "ReadStripped SD no-ALL.cmd" %EDITION%
call "ReadStripped SD no-no.cmd" %EDITION%
rem call "ReadStripped SD sq-ALL.cmd" %EDITION%
call "ReadStripped SD sv-all.cmd" %EDITION%
call "ReadStripped SD sv-sv.cmd" %EDITION%
