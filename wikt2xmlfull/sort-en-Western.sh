#!/bin/bash
#Combines files and sorts to a single output file
#Run first conv2unix-en-Western.sh
#Joel Korhonen 2014-09-14
#
# See http://www.madboa.com/geek/utf8/ and http://www.linux.com/archive/feature/39912
# and http://www.rhinocerus.net/forum/lang-awk/653031-informal-poll-fixing-z.html
# Install deb "language-support-fi" in Ubuntu --> language-support-writing-fi etc.
# (manually something like: su -c "localedef -f UTF-8 -i fi_FI fi_FI.UTF-8"
# to generate Finnish in UTF-8 charset and add it to the default locale archive with name fi_FI.UTF-8) 
# After that, "locale -a" should show fi_FI.UTF-8
NOW=`date +%Y-%m-%d`
INFILE=wikt-en-Western-unsorted.txt
OUTFILE=wikt-en-Western-$NOW.txt
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
