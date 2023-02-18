#!/bin/bash
#Combines files and sorts to a single output file
#Run first conv2unix-fi-fi.sh
#Joel Korhonen 2021-05-22
NOW=`date +%Y-%m-%d`
INFILE=wikt-fi-fi-unsorted.txt
OUTFILE=wikt-fi-fi-$NOW.txt
# Run as root: localedef -f UTF-8 -i fi_FI fi_FI.UTF-8
# to generate Finnish in UTF-8 charset and add it to the default locale archive with name fi_FI.UTF-8) 
# After that, "locale -a" should show fi_FI.UTF-8
export LC_ALL=fi_FI.UTF-8
locale
echo Sorting...
# This is the best. Greek letters are after Z though
# Also sorts À l'ordinaire after A, whereas could be at l
#sort -t '\n' -k 1,1 --output=$OUTDIR/$OUTFILE $INDIR/$INFILE
# 
sort -t$'\t' -k 1,1 --output=$OUTFILE $INFILE
# Due to --dictionary-order, À l'ordinaire is placed at l and Τό πρέπόν before To prepon
# However, this misplaces Œdipus at D and Šiauliai at i.
#sort -t '<' -k 4,4 --dictionary-order --output=Sort_VnW_test_out6.xml Sort_VnW_test_in.xml

#sort all.txt > sorted.txt
