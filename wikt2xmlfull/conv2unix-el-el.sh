#!/bin/bash
convLangCode="el"
convLang="el"
#convDate="2023-02-19*"
#FILES="wikt-$convLangCode-$convLang-$convDate.txt"
# Find out YYYY-MM-DD automatically
FILES="wikt-$convLangCode-$convLang-`date +%Y-%m-%d*.txt`"
[ -f toiconv-wikt-$convLangCode-$convLang.txt ] && rm -v toiconv-wikt-$convLangCode-$convLang.txt
[ -f wikt-$convLangCode-$convLang-unsorted.txt ] && rm -v wikt-$convLangCode-$convLang-unsorted.txt
for f in $FILES
do
  echo Processing file: ${f}
  dos2unix ${f}
  cat ${f} >> toiconv-wikt-$convLangCode-$convLang.txt
done
[ -f utf16.tmp ] && rm utf16.tmp
iconv -c -f utf-8 -t utf-16 toiconv-wikt-$convLangCode-$convLang.txt > utf16.tmp
iconv -c -f utf-16 -t utf-8 utf16.tmp > wikt-$convLangCode-$convLang-unsorted.txt
[ -f utf16.tmp ] && rm utf16.tmp
#iconv -c -f utf-16 -t utf-8 toiconv-wikt-$convLangCode-$convLang.txt > wikt-$convLangCode-$convLang-unsorted.txt
