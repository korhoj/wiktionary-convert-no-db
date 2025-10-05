@echo off
rem English Wikt language definitions parsing output combine + sort script
rem Joel Korhonen 2025-June-25
rem chcp 65001
rem cls
set LANG=en
if "%WIKTGIT%"=="" set WIKTGIT=C:\Users\korho\git\wiktionary-convert-no-db\wikt2xmlfull
set SCRIPTDIR=%WIKTGIT%\Scripts\ParseLangs
set OUTPATH=%WIKTGIT%/langs/%LANG%/out
cd /D "%OUTPATH%"
@echo on
copy "%OUTPATH%\a.csv" + "%OUTPATH%\b.csv" + "%OUTPATH%\c.csv" + "%OUTPATH%\d.csv" "%OUTPATH%\abcd.csv"
copy "%OUTPATH%\e.csv" + "%OUTPATH%\f.csv" + "%OUTPATH%\g.csv" + "%OUTPATH%\h.csv" "%OUTPATH%\efgh.csv
copy "%OUTPATH%\i.csv" + "%OUTPATH%\j.csv" + "%OUTPATH%\k.csv" + "%OUTPATH%\l.csv" "%OUTPATH%\ijkl.csv"
copy "%OUTPATH%\m.csv" + "%OUTPATH%\n.csv" + "%OUTPATH%\o.csv" + "%OUTPATH%\p.csv" "%OUTPATH%\mnop.csv"
copy "%OUTPATH%\q.csv" + "%OUTPATH%\r.csv" + "%OUTPATH%\s.csv" + "%OUTPATH%\t.csv" "%OUTPATH%\qrst.csv"
copy "%OUTPATH%\u.csv" + "%OUTPATH%\v.csv" + "%OUTPATH%\w.csv" + "%OUTPATH%\x.csv" "%OUTPATH%\uvwx.csv"
copy "%OUTPATH%\y.csv" + "%OUTPATH%\z.csv" "%OUTPATH%\yz.csv"
copy "%OUTPATH%\abcd.csv" + "%OUTPATH%\efgh.csv" + "%OUTPATH%\ijkl.csv" + "%OUTPATH%\mnop.csv" + "%OUTPATH%\qrst.csv" + "%OUTPATH%\uvwx.csv" + "%OUTPATH%\yz.csv" "%OUTPATH%\3ltr.csv"
echo name;abr>"%OUTPATH%\header.csv"
copy "%OUTPATH%\..\en-2_letter-language codes.csv" + "%OUTPATH%\3ltr.csv" "%OUTPATH%\combined-2_and_3.csv"
rem Added lines. Copy to ..\en-extra_non_generated-language codes.csv
rem FIXME Is this supposed to be the Git bash v. of grep (GNU grep v.3.0), or some Win version??
grep -xvFf sorted-2_and_3.csv ../old-en-ALL-language\ codes.csv > gen-sorted-added.txt
if %ERRORLEVEL% NEQ 0 goto :errEnd
rem Removed lines:
rem  grep -xvFf sorted-2_and_3.csv ../old-en-ALL-language\ codes.csv > gen-sorted-removed.txt
rem -x: match whole lines
rem -v: lines NOT matching the pattern(s)
rem -F: treat patterns as fixed strings, not regular expressions
rem -f <otherfile>: read the list of patterns from a file
rem See: https://superuser.com/questions/46766/how-can-i-get-diff-to-show-only-added-and-deleted-lines
rem OR: run a diff for this:
rem  diff -u0 sorted-2_and_3.csv ../old-en-ALL-language\ codes.csv > gen-diff.txt
rem  grep '^+' gen-diff-sorted.txt > gen-diff-sorted-added.txt
sort "%OUTPATH%\combined-2_and_3.csv" > "%OUTPATH%\sorted-2_and_3.csv"
rem
copy "%OUTPATH%\..\en-extra_non_generated-language codes.csv" + "%OUTPATH%\combined-2_and_3.csv" "%OUTPATH%\combined.csv"
sort "%OUTPATH%\combined.csv" > "%OUTPATH%\sorted.csv"
copy "%OUTPATH%\header.csv" + "%OUTPATH%\sorted.csv" "%OUTPATH%\en-ALL-language codes.csv" 
cd /D %SCRIPTDIR% 
goto end

:errEnd
echo ""
echo !!Ended in error!! ERRORLEVEL == %ERRORLEVEL%

:end