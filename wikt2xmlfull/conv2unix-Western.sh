#!/bin/bash
FILES="wikt-en-Western-2018-05-15*.txt"
# Find out YYYY-MM-DD automatically
#FILES="wikt-en-Western-`date %Y-%m-%d*.txt`"
rm wikt-en-Western-unsorted.txt
for f in $FILES
do
  echo Processing file: ${f}
  dos2unix ${f}
  cat ${f} >> wikt-en-Western-unsorted.txt
done
