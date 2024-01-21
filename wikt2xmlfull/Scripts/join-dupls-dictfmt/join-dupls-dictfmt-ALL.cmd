@echo off
rem Joel Korhonen 2023-02-19
set convLangCode=%1
set convLang=%2
rem set convDate=%3
SET X=
FOR /F "skip=1 delims=" %%x IN ('wmic os get localdatetime') DO IF NOT DEFINED X SET X=%%x
SET DATE.YEAR=%X:~0,4%
SET DATE.MONTH=%X:~4,2%
SET DATE.DAY=%X:~6,2%
SET convDate=%DATE.YEAR%-%DATE.MONTH%-%DATE.DAY%
rem set JAVA_HOME=C:\Usr\jdk-20.0.2
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET JAR_DIR=G:\Dropbox\Dictionary\wikt
SET WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
SET PROG=%JAR_DIR%\JoinDefinitions.jar
SET JCLASS=wiktionary\to\xml\full\JoinDefinitions
SET UTF8=-Dfile.encoding=UTF8
SET INFILE=G:\Users\Joel\OwnStarDict-ready\wikt-%convLangCode%-%convLang%-%convDate%.txt
SET OUTFILE=G:\Users\Joel\OwnStarDict-ready\wikt-%convLangCode%-%convLang%-dictfmt-dupls-joined.txt
%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%