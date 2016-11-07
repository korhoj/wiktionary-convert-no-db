@echo off
rem Joel Korhonen 2014-09-14
SET JAVA_HOME=C:\PROGRA~1\Java\jdk1.8.0_20
SET JAVA="%JAVA_HOME%\bin\java.exe"
rem SET PROGDIR=%WIKT%
SET PROGDIR=I:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict
SET PROG=%WIKT%\JoinDefinitions.jar
SET JCLASS=wiktionary\to\xml\full\JoinDefinitions
SET UTF8=-Dfile.encoding=UTF8
rem SET INFILE=I:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict\sorted.txt
SET INFILE=I:\Users\Joel\OwnStarDict\wikt-en-ALL-2016-11-07.txt
rem SET OUTFILE=I:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict\dictfmt-dupls-joined.txt
SET OUTFILE=I:\Users\Joel\OwnStarDict-ready\wikt-en-ALL-dictfmt-dupls-joined.txt
%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
pause