!/bin/bash
convLang="ALL"
convLangCode="sv"
convDate="2021-05-23"
dictfmt -f --utf8 --allchars wikt-$convLangCode-$convLang-$convDate -u https://dictinfo.com -s wikt-$convLangCode-$convLang-$convDate < ../wikt-$convLangCode-$convLang-$convDate-dict.txt