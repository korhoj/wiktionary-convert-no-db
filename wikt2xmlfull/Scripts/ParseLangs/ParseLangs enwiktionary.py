#!/usr/bin/python3
import os
#import re
#import requests
# See https://www.crummy.com/software/BeautifulSoup/bs4/doc/
from bs4 import BeautifulSoup
from bs4.diagnose import diagnose
#import string
#import sys
import traceback

# Parse the pre-downloaded HTML file for
# each 3-letter language code.
DLpath = 'G:/Users/Joel/git/wiktionary-convert-no-db/wikt2xmlfull/langs/en'
""" for letter in string.ascii_lowercase:
    DLFullFilePath = os.path.join(DLpath, letter + '.html')
    print(f"Parsing {DLFullFilePath}...")
    
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

    docStr = str(doc) """

letter = 'a'  # Change this to the desired letter to parse
DLFullFilePath = os.path.join(DLpath, letter + '.html')
## OR: Use the short test version of the file
#DLFullFilePath = os.path.join(DLpath, letter + '-short.html')
## OR: Use the complex short test version of the file
#DLFullFilePath = os.path.join(DLpath, letter + '-short-complex.html')
print(f"Parsing {DLFullFilePath}...")
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
#docStr = str(doc)
preSect = doc.pre
#tableRows = doc.find_all('tr')

#print(f"preSect: {preSect}")

outFileName='en-language codes-by-parseEnglLangs.csv'
outFullFilePath = os.path.join(DLpath, outFileName)
print(f"Writing to file: {outFullFilePath}")
# Write the data to a CSV file
with open (outFullFilePath, 'w', encoding='utf-8') as outF:
  try:
    # name;abr
    # Afar;aa
    # Abkhaz;ab
    # ...
    ## TODO: Add langFamName and support for it in Java side
    ##outF.write(f"name;abr;langfam\n")
    outF.write(f"name;abr\n")
  except Exception as e:
    print(f"Error writing to file {outFullFilePath}: {e}")
    traceback.print_exc()
    raise e
  finally:
    outF.close()

tdRows = preSect.find_all('span')

dataLineFound = False
nLineFound = False
pLineFound = False
miLineFound = False

# A generated code for the language, e.g. "aaa", "aab", etc.
langGenCode = None
# A human-readable name for the language, e.g. "Ghotuo", "Alumo-Tesu", etc.
langName = None
# A unique ID number for the language, e.g. 35463
miCode = None
# A 7-letter language code, e.g. "alv-yek", "nic-alu", etc.
langCode = None
# A language family name, e.g. "Latn"
langFamName = None
## And optional rows, e.g.:
###	translit = "cau-nec-translit",
###	override_translit = true,
###	display_text = s["cau-Cyrl-displaytext"],
###	entry_name = s["cau-Cyrl-entryname"],

for tdRow in tdRows:
  #print('Rivi:')
  #print(tdRow)
  #print('\n')
  
  tdRowChildren = tdRow.contents
  #print(f"  tdRowChildren: {tdRowChildren}")
  #print('\n')

  innerSpans = tdRow.find_all('span')
  if innerSpans:
    #print(tdRow)
    ##print(f"  tdRowChildren: {tdRowChildren}")

    for innerSpan in innerSpans:
      #print(f"    span: {innerSpan}")
      if innerSpan.has_attr('data-line'):
        dataLineFound = True
      elif innerSpan.has_attr('class'):
        if 'n' in innerSpan['class']:
          if innerSpan.string == 'm':
            # This is a line with the language code, e.g. "m" in:
            #  <span class="n">m</span>
            #print(f"    nLine: {innerSpan}")
            #innerSpanText = innerSpan.text.strip()
            #if len(innerSpanText) == 1 and innerSpanText == 'm':
            print("      +nLine: m")
            nLineFound = True
        elif 'p' in innerSpan['class']:
          pLineFound = True
        elif 'mi' in innerSpan['class']:
          miLineFound = True
          # This is a line with a unique ID number for the language, e.g. 35463 in:
          #  <span class="mi">35463</span>
          innerSpanText = innerSpan.text.strip()
          if innerSpanText.isdigit():
            miCode = int(innerSpanText)
            print(f"      miCode: {miCode}")
          else:
            print(f"      miCode: {innerSpanText} (not a number)")
        elif 's2' in innerSpan['class']:
          if nLineFound and pLineFound and dataLineFound:
            innerSpanText = innerSpan.text.strip().strip('"')
            if len(innerSpanText) == 3:
              # This is a line with a generated code for the language, e.g. "aaa" in:
              #  <span class="n">m</span> <span class="p">[</span> <span class="s2">"aaa"</span> <span class="p">]</span>
              langGenCode = innerSpanText
              print(f"    langGenCode: {langGenCode}")
            else:
              # Unknown text
              print(f"      !langGenCode not parsed: {innerSpanText}")
            dataLineFound = False
            nLineFound = False
            pLineFound = False
            miLineFound = False
            langCode = None
            langName = None
            langFamName = None
          elif (not nLineFound or not pLineFound):
            if not miLineFound:
              innerSpanText = innerSpan.text.strip().strip('"')
              if len(innerSpanText) > 0:
                if langName is None:
                  # This is a human-readable language name, e.g. "Ghotuo"
                  langName = innerSpanText
                  print(f"      langName: {langName}")
                else:
                  # Languages with complex definitions, such as "aqc", go here.
                  # Their langName doesn't get set yet
                  if langGenCode is not None:
                    print(f"      langGenCode: {langGenCode} (complex definition)")
                    #print("  ERROR: mi not set but langName already set!")
            else: # miLineFound:
              if langGenCode not in ['condNotInUse']:
                # 'abq','ace','acy','adp','ady','agx',
                # 'ahk','akv','alq','alr','alt','ams','amw','ang',
                # 'anp','aqc','arc','atb','awa','axm']:
                # This is a string literal, e.g. "alv-yek" or "Ghotuo"
                innerSpanText = innerSpan.text.strip().strip('"')

                if len(innerSpanText) > 0:
                  if langCode is None:
                    if len(innerSpanText) == 7 and '-' in innerSpanText:
                      # This is either a 3-letter language code, e.g. "paa",
                      # or a 7-letter language code, e.g. "alv-yek"
                      langCode = innerSpanText
                      print(f"      langCode(7): {langCode}")
                    elif len(innerSpanText) == 3:
                      # This is a 3-letter language code, e.g. "paa"
                      langCode = innerSpanText
                      print(f"      langCode(3): {langCode}")
                    else:
                      # Unknown text
                      print(f"      !langCode for {langGenCode} not parsed: {innerSpanText}")
                  elif langName is not None and langFamName is None:
                    # This is a language family name, e.g. "Latn"
                    langFamName = innerSpanText
                    print(f"      langFamName: {langFamName}")
                    # We have collected now all the data for one language, e.g.:
                    #  langGenCode: aaa
                    #    langName: Ghotuo
                    #    langCode: alv-yek
                    #    langFamName: Latn
                    
                    outFileName='en-language codes-by-parseEnglLangs.csv'
                    outFullFilePath = os.path.join(DLpath, outFileName)
                    #print(f"Appending to file: {outFullFilePath}")
                    # Append the data to a CSV file
                    with open (outFullFilePath, 'a', encoding='utf-8') as outF:
                      try:
                          # name;abr
                          # Afar;aa
                          # Abkhaz;ab
                          # ...
                          ## TODO: Add langFamName and support for it in Java side
                          ##outF.write(f"{langName};{langCode};{langFamName}\n")
                          outF.write(f"{langName};{langCode}\n")
                      except Exception as e:
                        print(f"Error writing to file {outFullFilePath}: {e}")
                        traceback.print_exc()
                        raise e
                      finally:
                        outF.close()

                    #langGenCode = None
                    #langCode = None
                    #langName = None
                    #langFamName = None
                    #miCode = None
                    #miLineFound = False
                    #dataLineFound = False
                    #nLineFound = False
                    #pLineFound = False
                    #miLineFound = False
                  else:
                    # Unknown text
                    # Languages with complex definitions, such as "arc", go here,
                    # and have text like e.g.::
                    ## Armi-translit
                    ## Palm-translit
                    ## Hebr-common
                    ## -
                    #print(f"      !langFamName set already: {innerSpanText}")
                    pass
                  #dataLineFound = False
                  #nLineFound = False
                  #pLineFound = False
            # Reset flags for the next language code
            #langGenCode = None
            #langCode = None
            #langName = None
            #langFamName = None

            #dataLineFound = False
            #nLineFound = False
            #pLineFound = False
            #miLineFound = False

# The parsed HTML for the below are LUA structures of type:
# m["aaa"] = {
# 	"Ghotuo",
# 	35463,
# 	"alv-yek",
# 	"Latn",
# }
""" <pre>
...
<span id="L-14">
 <a href="#L-14"><span class="linenos" data-line="14"></span></a>
</span>
<span id="L-15">
 <a href="#L-15"><span class="linenos" data-line="15"></span></a>
 <span class="n">m</span>
 <span class="p">[</span>
 <span class="s2">"aaa"</span>
 <span class="p">]</span>
 <span class="o">=</span>
 <span class="p">{</span>
</span>
<span id="L-16">
 <a href="#L-16"><span class="linenos" data-line="16"></span></a>
 <span class="s2">"Ghotuo"</span>
 <span class="p">,</span>
</span>
<span id="L-17">
 <a href="#L-17"><span class="linenos" data-line="17"></span></a>
 <span class="mi">35463</span>
 <span class="p">,</span>
</span>
<span id="L-18">
 <a href="#L-18"><span class="linenos" data-line="18"></span></a>
 <span class="s2">"alv-yek"</span>
 <span class="p">,</span>
</span>
<span id="L-19">
 <a href="#L-19"><span class="linenos" data-line="19"></span></a>
 <span class="s2">"Latn"</span>
 <span class="p">,</span>
</span>
<span id="L-20">
 <a href="#L-20"><span class="linenos" data-line="20"></span></a>
 <span class="p">}</span>
</span>
</pre> """
