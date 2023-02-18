#!/bin/bash
convLangCode="no"
convLang="ALL"
convDate="2023-02-18"
dictfmt -f --utf8 --allchars wikt-$convLangCode-$convLang-$convDate -u http://dictinfo.com -s wikt-$convLangCode-$convLang-$convDate < ../wikt-$convLangCode-$convLang-$convDate-dict.txt
