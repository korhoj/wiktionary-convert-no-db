#!/usr/bin/python3
import os
import requests
import string
import traceback

# Download the HTML file for each letter of the English alphabet
# defining the 3-letter language codes for each letter.
# E.g. https://en.wiktionary.org/wiki/Module:languages/data/3/a
DLpath = 'G:/Users/Joel/git/wiktionary-convert-no-db/wikt2xmlfull/langs/en'
baseUrl = 'https://en.wiktionary.org/wiki/Module:languages/data/3/'
for letter in string.ascii_lowercase:
    URL = baseUrl + letter
    print(f"Downloading {URL}...")
    DLFullFilePath = os.path.join(DLpath, letter + '.html')

    with open(DLFullFilePath, 'wb') as fDownloaded:
        try:
            fDownloaded.write(requests.get(URL).content)
        except requests.exceptions.ConnectionError as ce:
            print(f"ConnectionError in connecting to URL {URL}")
            raise ce
        except Exception as e:
            print(f"General error in connecting to URL {URL}")
            print(f"Error type: {type(e)}")
            print(e)
            traceback.print_exc()
            raise e
        finally:
            fDownloaded.close()
