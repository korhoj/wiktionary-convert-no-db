#!/bin/bash
FILES="wikt-en-en-2018-10-03*.txt"
# Find out YYYY-MM-DD automatically
#FILES="wikt-en-en-`date %Y-%m-%d*.txt"
rm wikt-en-en.txt
for f in $FILES
do
  echo Processing file: ${f}
  dos2unix ${f}
  cat ${f} >> wikt-en-en-unsorted.txt
done
