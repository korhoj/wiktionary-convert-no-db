@echo off
rem Joel Korhonen 2022-05-23
set JAVA_HOME=C:\Usr\openjdk-16.0.1_windows-x64_bin\jdk-16.0.1
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET JAR_DIR=G:\Dropbox\Dictionary\wikt
SET WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
SET PROG=%JAR_DIR%\JoinDefinitions.jar
SET JCLASS=wiktionary\to\xml\full\JoinDefinitions
SET UTF8=-Dfile.encoding=UTF8
SET INFILE=F:\Users\Joel\OwnStarDict-ready\wikt-sv-ALL-2021-05-23.txt
SET OUTFILE=F:\Users\Joel\OwnStarDict-ready\wikt-sv-ALL-dictfmt-dupls-joined.txt
%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
pause