#!/bin/bash
convLangCode="en"
convLang="Western_Greek_Slavonic"
convDate="2024-09-15"
dictfmt -f --utf8 --allchars wikt-$convLangCode-$convLang-$convDate -u http://dictinfo.com -s wikt-$convLangCode-$convLang-$convDate < ../wikt-$convLangCode-$convLang-$convDate-dict.txt
