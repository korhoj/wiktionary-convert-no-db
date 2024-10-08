#!/bin/bash
convLangCode="no"
convLang="no"
#convDate="2023-02-19*"
#FILES="wikt-$convLangCode-$convLang-$convDate.txt"
# Find out YYYY-MM-DD automatically
FILES="wikt-$convLangCode-$convLang-`date +%Y-%m-%d*.txt`"
[ -f wikt-$convLangCode-$convLang-unsorted.txt ] && rm -v wikt-$convLangCode-$convLang-unsorted.txt
for f in $FILES
do
  echo Processing file: ${f}
  dos2unix ${f}
  cat ${f} >> wikt-$convLangCode-$convLang-unsorted.txt
done