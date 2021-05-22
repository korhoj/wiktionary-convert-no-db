!/bin/bash
FILES="wikt-en-Western_Greek_Slavonic-2021-05-22*.txt"
# Find out YYYY-MM-DD automatically
#FILES="wikt-en-Western_Greek_Slavonic-`date %Y-%m-%d*.txt"
rm wikt-en-Western_Greek_Slavonic.txt
for f in $FILES
do
  echo Processing file: ${f}
  dos2unix ${f}
  cat ${f} >> wikt-en-Western_Greek_Slavonic.txt
done