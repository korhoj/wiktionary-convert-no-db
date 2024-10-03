#!/bin/bash
convLangCode="sv"
convLang="ALL"
#convDate="2023-02-18"
convDate=`date +%Y-%m-%d`
cp ../pd-header.txt wikt-$convLangCode-$convLang-$convDate-dict.txt
cat ../wikt-$convLangCode-$convLang-dictfmt-dupls-joined.txt | tee -a wikt-$convLangCode-$convLang-$convDate-dict.txt > /dev/null
dictfmt -f --utf8 --allchars wikt-$convLangCode-$convLang-$convDate -u https://dictinfo.com -s wikt-$convLangCode-$convLang-$convDate < wikt-$convLangCode-$convLang-$convDate-dict.txt
dictzip wikt-$convLangCode-$convLang-$convDate.dict
chmod 644 wikt-$convLangCode-$convLang-$convDate.dict.dz
chmod 644 wikt-$convLangCode-$convLang-$convDate.index
7z a wikt-$convLangCode-$convLang-$convDate-dictd.7z wikt-$convLangCode-$convLang-$convDate.{dict.dz,index}