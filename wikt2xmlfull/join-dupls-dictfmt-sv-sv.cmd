@echo off
rem Joel Korhonen 2023-02-18
set convLangCode="sv"
set convLang="sv"
set convDate="2023-02-18"
set JAVA_HOME=C:\Usr\jdk-19
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET JAR_DIR=G:\Dropbox\Dictionary\wikt
SET WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
SET PROG=%JAR_DIR%\JoinDefinitions.jar
SET JCLASS=wiktionary\to\xml\full\JoinDefinitions
SET UTF8=-Dfile.encoding=UTF8
SET INFILE=F:\Users\Joel\OwnStarDict-ready\wikt-%convLangCode%-%convLang%-%convDate%.txt
SET OUTFILE=F:\Users\Joel\OwnStarDict-ready\wikt-%convLangCode%-%convLang%-dictfmt-dupls-joined.txt
%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
pause