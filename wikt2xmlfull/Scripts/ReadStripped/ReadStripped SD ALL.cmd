@echo off
rem Process the Wiktionaries for all supported languages. 
rem Joel Korhonen 2025-Apr-23

rem Change the value of EDITION to match the date of the dump you want to use for language el 
set EDITION=20250401
call "ReadStripped SD el-ALL.cmd" %EDITION%
if %ERRORLEVEL% NEQ 0 goto :errEnd
call "ReadStripped SD el-el.cmd" %EDITION%
if %ERRORLEVEL% NEQ 0 goto :errEnd

rem If the rest of the dumps use a different edition, set it here.
rem Also, if the dump date for any language below differs from what we set here,
rem add a SET before the "call" of each language with a differing date
set EDITION=20250801

call "ReadStripped SD en-ALL.cmd" %EDITION%
if %ERRORLEVEL% NEQ 0 goto :errEnd
call "ReadStripped SD en-en.cmd" %EDITION%
if %ERRORLEVEL% NEQ 0 goto :errEnd
call "ReadStripped SD en-Western_Greek_Slavonic.cmd" %EDITION%
if %ERRORLEVEL% NEQ 0 goto :errEnd
call "ReadStripped SD en-Western.cmd" %EDITION%
if %ERRORLEVEL% NEQ 0 goto :errEnd
call "ReadStripped SD fi-ALL.cmd" %EDITION%
if %ERRORLEVEL% NEQ 0 goto :errEnd
call "ReadStripped SD fi-fi.cmd" %EDITION%
if %ERRORLEVEL% NEQ 0 goto :errEnd
rem call "ReadStripped SD nb-ALL.cmd" %EDITION%
rem if %ERRORLEVEL% NEQ 0 goto :errEnd
rem call "ReadStripped SD nb-nb.cmd" %EDITION%
rem if %ERRORLEVEL% NEQ 0 goto :errEnd
call "ReadStripped SD nn-ALL.cmd" %EDITION%
if %ERRORLEVEL% NEQ 0 goto :errEnd
call "ReadStripped SD nn-nn.cmd" %EDITION%
if %ERRORLEVEL% NEQ 0 goto :errEnd
call "ReadStripped SD no-ALL.cmd" %EDITION%
if %ERRORLEVEL% NEQ 0 goto :errEnd
call "ReadStripped SD no-no.cmd" %EDITION%
rem call "ReadStripped SD sq-ALL.cmd" %EDITION%
call "ReadStripped SD sv-all.cmd" %EDITION%
if %ERRORLEVEL% NEQ 0 goto :errEnd
call "ReadStripped SD sv-sv.cmd" %EDITION%
if %ERRORLEVEL% NEQ 0 goto :errEnd
goto end
:errEnd
echo ""
echo !!Ended in error!! ERRORLEVEL == %ERRORLEVEL%
:end