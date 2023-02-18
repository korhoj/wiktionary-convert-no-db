#!/bin/bash
#Combines files and sorts to a single output file
#Run first conv2unix-el-el.sh
#Joel Korhonen 2021-May-23
NOW=`date +%Y-%m-%d`
INFILE=wikt-el-el-unsorted.txt
OUTFILE=wikt-el-el-$NOW.txt
# Run as root: localedef -f UTF-8 -i el_GR el_GR.UTF-8
# to generate Greek in UTF-8 charset and add it to the default locale archive with name el_GR.UTF-8)
# After that, "locale -a" should show el_GR.UTF-8
#export LC_ALL=en_US.UTF-8
export LC_ALL=el_GR.UTF-8
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
