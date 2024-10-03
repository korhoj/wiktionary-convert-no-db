#!/bin/bash
convLangCode="nn"
convLang="nn"
#convDate="2024-10-03*"
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