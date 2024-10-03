#!/bin/bash
convLang="Western"
convLangCode="en"
convDate="2024-09-15"
dictfmt -f --utf8 --allchars wikt-$convLangCode-$convLang-$convDate -u https://dictinfo.com -s wikt-$convLangCode-$convLang-$convDate < ../wikt-$convLangCode-$convLang-$convDate-dict.txt
