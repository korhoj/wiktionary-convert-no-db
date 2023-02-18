#!/bin/bash
convLangCode="fi"
convLang="ALL"
convDate="2023-02-18*"
FILES="wikt-$convLangCode-$convLang-$convDate.txt"
# Find out YYYY-MM-DD automatically
#FILES="wikt-$convLangCode-$convLang-`date %Y-%m-%d*.txt`"
[ -f wikt-$convLangCode-$convLang-unsorted.txt ] && rm -v wikt-$convLangCode-$convLang-unsorted.txt
for f in $FILES
do
  echo Processing file: ${f}
  dos2unix ${f}
  cat ${f} >> wikt-$convLangCode-$convLang-unsorted.txt
done