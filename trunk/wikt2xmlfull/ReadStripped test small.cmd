@echo off
rem Joel Korhonen 2012-04-22
SET PROGDIR=D:\Var\MyDocs\Omat\Dictionary\wikt
SET PROG=%PROGDIR%\ReadStripped.jar
SET JCLASS=wiktionary\to\xml\full\ReadStripped
rem SET JAVA="C:\Program Files (x86)\Java\jre6\bin\java.exe"
SET JAVA="%JAVA_HOME%\bin\java.exe"
SET INFILE=D:\Var\MyDocs\Omat\Dictionary\General\Wiktionary\enwiktionary-20120406-pages-articles.xml\encyclopedia.xml
SET OUTFILE=D:\Var\MyDocs\Omat\Dictionary\General\Wiktionary\enwiktionary-20120406-pages-articles.xml\encyclopedia-parsed.xml
SET UTF8=-Dfile.encoding=UTF-8

cd %PROGDIR%
chcp 65001

%JAVA% %UTF8% -jar %PROG% %INFILE% %OUTFILE%
if %ERRORLEVEL% GTR 0 goto :virhe

if "%1" == "" pause
goto end

:virhe
pause
exit 8

:end
