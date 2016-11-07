#!/bin/bash
#Combines files and sorts to a single output file
#Run first conv2unix.sh
#Joel Korhonen 2014-09-14

INFILE=wikt-en-ALL.txt
OUTFILE=wikt-en-ALL-sorted.txt

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
