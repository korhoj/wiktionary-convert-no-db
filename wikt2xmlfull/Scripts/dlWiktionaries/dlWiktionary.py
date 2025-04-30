#!/usr/bin/python3
import os
import re
import requests
# See https://www.crummy.com/software/BeautifulSoup/bs4/doc/
from bs4 import BeautifulSoup
from bs4.diagnose import diagnose
import sys
import traceback

# In Windows, in a PS shell, run this app e.g. like this:
## & C:/Users/korho/AppData/Local/Programs/Python/Python312/python.exe g:/Dropbox/Docs/Programming/Python/YoutubeDL/dlWiktionary.py en
# In Windows, in a cmd shell, run like this:
## %LOCALAPPDATA%\Programs\Python\Python312\python.exe g:/Dropbox/Docs/Programming/Python/YoutubeDL/dlWiktionary.py en

lang = 'fi'
mirrorSite = 'https://mirror.accum.se/mirror/wikimedia.org/dumps/'

if len(sys.argv) <= 1:
  print(f"Using the default params:")
  print(f"  lang='{lang}'")
  print(f"  mirrorSite='{mirrorSite}'")
if len(sys.argv) >= 2:
  lang = sys.argv[1]
  print(f"Using param lang='{lang}'")
if len(sys.argv) == 3:
  mirrorSite = sys.argv[2]
  print(f"Using param mirrorSite='{mirrorSite}'")
if len(sys.argv) > 3:
  print("Usage:")
  print("  dlWiktionary.py [lang] {mirrorSite}")
  print("")
  print("ex.1:")
  print("  dlWiktionary.py fi")
  print("ex.2.:")
  print("  dlWiktionary.py fi https://mirror.accum.se/mirror/wikimedia.org/dumps/")
  sys.exit(2)

URL = mirrorSite + lang + 'wiktionary/'
DLpath = 'G:/Temp/wiktionary-dumps/'
DLfile = lang + '-dumps-index.html'
DLFullFilePath = DLpath + DLfile
with open (DLFullFilePath, 'wb') as fDownloaded:
  try:
    fDownloaded.write(requests.get(URL).content)
  except requests.exceptions.ConnectionError as ce:
    print(f"ConnectionError in connecting to URL {URL}")
    #sys.exit(3)
    raise ce
  except Exception as e:
    print(f"General error in connecting to URL {URL}")
    print(f"Error type: {type(e)}")
    print(e)
    traceback.print_exc()
    raise e
  finally:
    fDownloaded.close()

parser = 'html.parser'
doc = None
with open (DLFullFilePath, 'r', encoding='utf-8') as fInput:
  try:
    doc = BeautifulSoup(fInput, parser)
  except Exception as e:
    print(e)
    traceback.print_exc()
    raise e
  finally:
    fInput.close()

docStr = str(doc)
#print(doc)
#print(doc.prettify())

#### <body>
#### <h1 id="indextitle">Index of /mirror/wikimedia.org/dumps/fiwiktionary</h1>
### <table id="indexlist">
##   <tr class="indexhead"><th class="indexcolicon"><img src="/icons2/blank.png" alt="[ICO]"></th><th class="indexcolname"><a href="?C=N;O=D">Name</a></th><th class="indexcollastmod"><a href="?C=M;O=A">Last modified</a></th><th class="indexcolsize"><a href="?C=S;O=A">Size</a></th></tr>
##   <tr class="indexbreakrow"><th colspan="4"><hr></th></tr>
#    <tr class="even"><td class="indexcolicon"><a href="/mirror/wikimedia.org/dumps/"><img src="/icons2/go-previous.png" alt="[PARENTDIR]"></a></td><td class="indexcolname"><a href="/mirror/wikimedia.org/dumps/">Parent Directory</a></td><td class="indexcollastmod">&nbsp;</td><td class="indexcolsize">  - </td></tr>
#    <tr class="odd"><td class="indexcolicon"><a href="20250201/"><img src="/icons2/folder.png" alt="[DIR]"></a></td><td class="indexcolname"><a href="20250201/">20250201/</a></td><td class="indexcollastmod">2025-03-21 10:35  </td><td class="indexcolsize">  - </td></tr>
# ...
#    <tr class="even"><td class="indexcolicon"><a href="20250320/"><img src="/icons2/folder.png" alt="[DIR]"></a></td><td class="indexcolname"><a href="20250320/">20250320/</a></td><td class="indexcollastmod">2025-03-21 14:13  </td><td class="indexcolsize">  - </td></tr>
#    <tr class="odd"><td class="indexcolicon"><a href="20250401/"><img src="/icons2/folder.png" alt="[DIR]"></a></td><td class="indexcolname"><a href="20250401/">20250401/</a></td><td class="indexcollastmod">2025-04-12 17:52  </td><td class="indexcolsize">  - </td></tr>
##   <tr class="indexbreakrow"><th colspan="4"><hr></th></tr>
### </table>

# Find the two newest dumps from the table above
## Loop each "tr" tag having class="even" or "odd"

table = doc.table
## <td class="indexcolname"><a href="20250401/">20250401/</a></td>
## ...

reYYYYMMDD_and_slash = re.compile('^[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]/$')

dateList = []

tableRows = table.find_all('tr',re.compile("even|odd"))
for tableRow in tableRows:
  tdRows = tableRow.find_all('td','indexcolname')
  for tdRow in tdRows:
    #print('Rivi:')
    #print(tdRow)
    #print('\n')

    #print('  hrefs:')
    aTags = tdRow.a
    #hRefs = has_href_with_date(tdRow)
    ##hRefs = tdRow.a.has_attr('href')
    #hRefs = find_all(tdRow.a(has_href_with_date()))
    for aTag in aTags:
      tagValue = str(aTag)
      if reYYYYMMDD_and_slash.match(tagValue):
        #print('aTag OK')
        #print(' ' + tagValue)
        dateList.append(tagValue.rstrip('/'))

penUltimateDumpDate = dateList[len(dateList)-2]
ultimateDumpDate = dateList[len(dateList)-1]

print(f"Second latest dump: {penUltimateDumpDate}")
print(f"Latest dump:        {ultimateDumpDate}")

## Check whether the newest or second-newest dump for a particular date has been finished
#print('Finding the latest dump that has been finished')

### Sometimes the dump is not ready even though the directory exists already.
### To be sure, open the link to the file status.html in the directory listing. E.g.:
###   https://mirror.accum.se/mirror/wikimedia.org/dumps/fiwiktionary/20240901/status.html
#for dateStr in dateList:
dateStr = ultimateDumpDate
#print(f"  Checking date: {dateStr}")
URLforDate = mirrorSite + lang + 'wiktionary/' + dateStr + '/status.html'

#dateWithoutSlash = dateStr.rstrip('/')
DLfileForDate = lang + '-dumps-'+dateStr+'-status.html'
DLFileForDateFullFilePath = DLpath + DLfileForDate
with open (DLFileForDateFullFilePath, 'wb') as fFileForDateDownloaded:
  try:
    fFileForDateDownloaded.write(requests.get(URLforDate).content)
  except requests.exceptions.ConnectionError as ce:
    print(f"ConnectionError in connecting to URL {URLforDate}")
    #sys.exit(3)
    raise ce
  except Exception as e:
    print(f"General error in connecting to URL {URLforDate}")
    print(f"Error type: {type(e)}")
    print(e)
    traceback.print_exc()
    raise e
  finally:
    fFileForDateDownloaded.close()

docForDate = None
with open (DLFileForDateFullFilePath, 'r', encoding='utf-8') as fForDateInput:
  try:
    docForDate = BeautifulSoup(fForDateInput, parser)
    # It should have a text like this:
    # * 2024-09-06 02:28:29 enwiktionary: Dump complete
    #
    # (where the word "enwiktionary" is currently a slightly _malformed_ link,
    #  https://mirror.accum.se/mirror/wikimedia.org/dumps/enwiktionary/20240901/enwiktionary/20240901,
    # - it's supposed to just point to the dump directory the status.html file is in...)
    ##
    # An actual example:
    # <li>2025-02-06 16:32:57 <a href="fiwiktionary/20250201">fiwiktionary</a>: <span class='done'>Dump complete</span></li>
    #
    
    # If the dump being parsed is the oldest, the directory for it may exist in the
    # server, but be empty. At least mirror.accum.se returns then a 404 error document.
    # Although changed the code to parse just the latest dump anyway as such.
    if docForDate.title is None or docForDate.title.string != '404 Not Found':
      ## Finds the string 'done' from this:
      # <span class='done'>Dump complete</span>
      doneStr = docForDate.span['class'][0]
      if doneStr == 'done':
        pass
        #print('Dump is done.')
        #
        #if ultimateDumpDate != '':
        #  penUltimateDumpDate = ultimateDumpDate
        #ultimateDumpDate = dateWithoutSlash
  except Exception as e:
    print(f"Error in parsing dump status for {DLFileForDateFullFilePath}")
    print(e)
    traceback.print_exc()
    raise e
  finally:
    fForDateInput.close()
# <-- for dateStr in dateList

#if ultimateDumpDate == '':
#  print("Unfortunately, there is no completed dump available presently, aborting!")
#  sys.exit(1)
#print(f"Newest completed dump is: {ultimateDumpDate}")

# Download the *-pages-articles.xml.bz2 file for the latest completed dump. E.g.:
#  https://mirror.accum.se/mirror/wikimedia.org/dumps/fiwiktionary/20250401/fiwiktionary-20250401-pages-articles.xml.bz2
dirURLforLatestCompleted = mirrorSite + lang + 'wiktionary/' + ultimateDumpDate + '/'
# print('dirUrl: ' + dirURLforLatestCompleted)
fileNameForLatestCompleted = lang + 'wiktionary-' + ultimateDumpDate + '-pages-articles.xml.bz2'
pagesArticlesFileURL = dirURLforLatestCompleted + fileNameForLatestCompleted

DLpath = 'G:/Temp/wiktionary-dumps/'
DLPagesArticlesFullFilePath = DLpath + fileNameForLatestCompleted

try:
  fileSize = os.path.getsize(DLPagesArticlesFullFilePath)
  
  if fileSize < 100000:
    print(f"File {pagesArticlesFileURL} already found, but with a small size: {fileSize:#0d}, so redownloading it")
  else:
    print(f"File {pagesArticlesFileURL} already found, size: {fileSize:#0d}, aborting download (remove file to force a redownload)")
    sys.exit()
except FileNotFoundError as fe:
  print(f"File {pagesArticlesFileURL} had not been downloaded yet. Downloading")
except Exception as e:
  print(e)
  traceback.print_exc()
  
  print(f"Might want to try downloading the penultimate dump instead: {penUltimateDumpDate}")
  
  raise e

print(f"Downloading {pagesArticlesFileURL}")
with open (DLPagesArticlesFullFilePath, 'wb') as fPagesArticlesDownloaded:
  try:
    fPagesArticlesDownloaded.write(requests.get(pagesArticlesFileURL).content)
  except Exception as e:
    print(e)
    traceback.print_exc()
    raise e
  finally:
    fPagesArticlesDownloaded.close()

print('Ready.')
