@echo off
rem Process English  language entries only of English Wiktionary
rem Joel Korhonen 2016-11-07
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_181
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET PROGDIR=%WIKT%
SET PROG=%WIKT%\JoinDefinitions.jar
SET JCLASS=wiktionary\to\xml\full\JoinDefinitions
SET UTF8=-Dfile.encoding=UTF8
SET INFILE=F:\Users\Joel\OwnStarDict-ready\wikt-en-en-2018-10-03.txt
SET OUTFILE=F:\Users\Joel\OwnStarDict-ready\wikt-en-en-dictfmt-dupls-joined.txt
%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
pause