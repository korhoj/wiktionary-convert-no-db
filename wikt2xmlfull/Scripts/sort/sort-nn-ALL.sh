#!/bin/bash
#Combines files and sorts to a single output file
#Run first conv2unix-no-ALL.sh
NOW=`date +%Y-%m-%d`
INFILE=wikt-nn-ALL-unsorted.txt
OUTFILE=wikt-nn-ALL-$NOW.txt
#export LC_ALL=nn_NO.UTF-8
# The next because ALL includes all languages
export LC_ALL=en_US.UTF-8
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
