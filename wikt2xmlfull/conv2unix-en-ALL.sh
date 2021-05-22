#!/bin/bash
FILES="wikt-en-ALL-2018-10-03*.txt"
# Find out YYYY-MM-DD automatically
#FILES="wikt-en-ALL-`date %Y-%m-%d*.txt"
rm wikt-en-ALL-unsorted.txt
for f in $FILES
do
  echo Processing file: ${f}
  dos2unix ${f}
  cat ${f} >> wikt-en-ALL-unsorted.txt
done
