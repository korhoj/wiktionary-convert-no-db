#!/bin/bash
#Combines files and sorts to a single output file
#Run first conv2unix-no-no.sh
NOW=`date +%Y-%m-%d`
INFILE=wikt-no-no-unsorted.txt
OUTFILE=wikt-no-no-$NOW.txt
# Run as root: localedef -f UTF-8 -i nb_NO nb_NO.UTF-8
# Norwegian Bokmål
export LC_ALL=nb_NO.UTF-8
# or for Nynorsk:
# Run as root: localedef -f UTF-8 -i nn_NO nn_NO.UTF-8
#export LC_ALL=nn_NO.UTF-8
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
