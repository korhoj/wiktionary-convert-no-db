#!/bin/bash
DATENOW=`date +%Y-%m-%d`
cp_and_compress() {
  echo "cp -v wikt-$LANG-$LCODE-$DATENOW.{dict.dz,idx,ifo} $LANG-StarDict"
  cp -v wikt-$LANG-$LCODE-$DATENOW.{dict.dz,idx,ifo} $LANG-StarDict
  [ $? -ne 0 ] && exit 8
  cd $LANG-StarDict/
  [ $? -ne 0 ] && exit 8
  7z a wikt-$LANG-$LCODE-$DATENOW.7z wikt-$LANG-$LCODE-$DATENOW.{dict.dz,idx,ifo}
  [ $? -ne 0 ] && exit 8
  cd ..
  [ $? -ne 0 ] && exit 8
}

LANG=el && LCODE=ALL && cp_and_compress
LCODE=el && cp_and_compress

LANG=en && LCODE=ALL && cp_and_compress
LCODE=en && cp_and_compress

LCODE=Western && cp_and_compress

LCODE=Western_Greek_Slavonic && cp_and_compress

LANG=fi && LCODE=ALL && cp_and_compress
LCODE=fi && cp_and_compress

LANG=nn && LCODE=ALL && cp_and_compress
LCODE=nn && cp_and_compress

LANG=no && LCODE=ALL && cp_and_compress
LCODE=no && cp_and_compress

LANG=sv && LCODE=ALL && cp_and_compress
LCODE=sv && cp_and_compress