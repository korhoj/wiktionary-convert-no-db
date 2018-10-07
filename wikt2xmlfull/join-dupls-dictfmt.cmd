@echo off
rem Joel Korhonen 2014-09-14
set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_181
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET PROGDIR=%WIKT%
SET PROG=%WIKT%\JoinDefinitions.jar
SET JCLASS=wiktionary\to\xml\full\JoinDefinitions
SET UTF8=-Dfile.encoding=UTF8
rem SET INFILE=I:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict\sorted.txt
SET INFILE=F:\Users\Joel\OwnStarDict-ready\wikt-en-ALL-2018-10-03.txt
rem SET OUTFILE=I:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict\dictfmt-dupls-joined.txt
SET OUTFILE=F:\Users\Joel\OwnStarDict-ready\wikt-en-ALL-dictfmt-dupls-joined.txt
%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
pause