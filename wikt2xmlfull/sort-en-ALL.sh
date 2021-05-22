#!/bin/bash
#Combines files and sorts to a single output file
#Run first conv2unix-en-ALL.sh
#Joel Korhonen 2021-05-22
NOW=`date +%Y-%m-%d`
INFILE=wikt-en-ALL-unsorted.txt
OUTFILE=wikt-en-ALL-$NOW.txt
export LC_ALL=en_US.UTF-8
locale
echo Sorting...
# This is the best. Greek letters are after Z though
# Also sorts À l'ordinaire after A, whereas could be at l
#sort -t '\n' -k 1,1 --output=$$OUTFILE $INFILE
# 
sort -t$'\t' -k 1,1 --output=$OUTFILE $INFILE
# Due to --dictionary-order, À l'ordinaire is placed at l and Τό πρέπόν before To prepon
# However, this misplaces Œdipus at D and Šiauliai at i.
#sort -t '<' -k 4,4 --dictionary-order --output=sort-test-out.txt sort-test-in.txt
