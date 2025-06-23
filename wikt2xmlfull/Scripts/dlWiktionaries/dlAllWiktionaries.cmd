rem DlAllWiktionaries.cmd
rem Downloads the newest dump for all supported Wiktionaries 
rem  Joel Korhonen 2025-Jun-23
rem In Windows, in a cmd shell, run like this:
rem  %WIKTGIT%\Scripts\dlWiktionaries\dlAllWiktionaries.cmd
py dlWiktionary.py el
py dlWiktionary.py en
py dlWiktionary.py fi
py dlWiktionary.py nn
py dlWiktionary.py no
py dlWiktionary.py sv
