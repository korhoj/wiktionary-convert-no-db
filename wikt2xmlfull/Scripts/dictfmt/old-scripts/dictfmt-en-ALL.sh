#!/bin/bash
convLangCode="en"
convLang="ALL"
#convDate="2024-10-03"
convDate=`date +%Y-%m-%d`
dictfmt -f --utf8 --allchars wikt-$convLangCode-$convLang-$convDate -u https://dictinfo.com -s wikt-$convLangCode-$convLang-$convDate < ../wikt-$convLangCode-$convLang-$convDate-dict.txt