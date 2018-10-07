@echo off
rem Process Western language entries of English Wiktionary
rem Joel Korhonen 2016-11-07
set JAVA_HOME=C:\PROGRA~1\Java\jdk1.8.0_172
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET PROGDIR=%WIKT%
SET PROG=%WIKT%\JoinDefinitions.jar
SET JCLASS=wiktionary\to\xml\full\JoinDefinitions
SET UTF8=-Dfile.encoding=UTF8
rem SET INFILE=I:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict\sorted.txt
SET INFILE=F:\Users\Joel\OwnStarDict-ready\wikt-en-Western-2018-05-15.txt
rem SET OUTFILE=I:\Dropbox\Dictionary\wikt\Stardict\OwnStarDict\dictfmt-dupls-joined.txt
SET OUTFILE=F:\Users\Joel\OwnStarDict-ready\wikt-en-Western-dictfmt-dupls-joined.txt
%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
pause