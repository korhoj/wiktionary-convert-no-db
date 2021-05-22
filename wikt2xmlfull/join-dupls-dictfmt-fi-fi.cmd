@echo off
rem Joel Korhonen 2022-05-22
SET JAVA_HOME=C:\Usr\openjdk-16_windows-x64_bin\jdk-16
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET JAR_DIR=G:\Dropbox\Dictionary\wikt
SET WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
SET PROG=%JAR_DIR%\JoinDefinitions.jar
SET JCLASS=wiktionary\to\xml\full\JoinDefinitions
SET UTF8=-Dfile.encoding=UTF8
SET INFILE=F:\Users\Joel\OwnStarDict-ready\wikt-fi-fi-2021-05-22.txt
SET OUTFILE=F:\Users\Joel\OwnStarDict-ready\wikt-fi-fi-dictfmt-dupls-joined.txt
%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
pause