@echo off
rem Joel Korhonen 2023-02-19
rem Duplicates Greek words test
set convLangCode="el"
set convLang="el"
set JAVA_HOME=C:\Usr\jdk-19
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET JAR_DIR=G:\Dropbox\Dictionary\wikt
SET WIKT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
SET PROG=%JAR_DIR%\JoinDefinitions.jar
SET JCLASS=wiktionary\to\xml\full\JoinDefinitions
SET UTF8=-Dfile.encoding=UTF8
SET INFILE=dupls-test-el-el.txt
SET OUTFILE=dupls-test-el-el-out.txt
%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%