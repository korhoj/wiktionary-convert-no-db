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
rem SET INFILE=I:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict\sorted.txt
SET INFILE=I:\Users\Joel\OwnStarDict-ready\wikt-en-Western-2016-11-07.txt
rem SET OUTFILE=I:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict\dictfmt-dupls-joined.txt
SET OUTFILE=I:\Users\Joel\OwnStarDict-ready\wikt-en-Western-dictfmt-dupls-joined.txt
%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
pause