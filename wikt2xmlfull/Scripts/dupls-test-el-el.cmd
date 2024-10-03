@echo off
rem Joel Korhonen 2023-02-19
rem Duplicate Greek words test
set convLangCode="el"
set convLang="el"
if "%JAVA_HOME%"=="" set JAVA_HOME=C:\Usr\jdk-23
SET JAVA="%JAVA_HOME%\bin\java.exe"
if "%WIKT%"=="" set WIKT=G:\Dropbox\Dictionary\wikt
set JAR_DIR=%WIKT%
SET PROG=%JAR_DIR%\JoinDefinitions.jar
SET JCLASS=wiktionary\to\xml\full\JoinDefinitions
SET UTF8=-Dfile.encoding=UTF8
SET INFILE=dupls-test-el-el.txt
SET OUTFILE=dupls-test-el-el-out.txt
%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%