@echo off
rem Process Western language entries of English Wiktionary
rem Joel Korhonen 2016-11-07
rem SET JAVA_HOME=C:\PROGRA~1\Java\jdk1.8.0_102
SET JAVA="%JAVA_HOME%\bin\java.exe"
rem SET PROGDIR=%WIKT%
rem SET PROGDIR=I:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict
SET PROG=%WIKT%\JoinDefinitions.jar
SET JCLASS=wiktionary\to\xml\full\JoinDefinitions
SET UTF8=-Dfile.encoding=UTF8

SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET NOW=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%

SET INFILE=I:\Users\Joel\OwnStarDict-ready\wikt-sq-ALL-%NOW%.txt
SET OUTFILE=I:\Users\Joel\OwnStarDict-ready\wikt-sq-ALL-dictfmt-dupls-joined.txt
%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
pause