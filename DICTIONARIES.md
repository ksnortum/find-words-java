# Notes on scrabble-words-java dictionaries

Much thought has gone into selecting Scrabble dictionaries for this app. The goal is to present reasonably current files that can be freely distributed. There is a tension between being up to date and licensing issues. But since this app is just for fun, the lack of being absolutely current and official should not be a problem.

## NASPA

The up-to-date, official Scrabble dictionary for the USA, Canada, and Thailand is called the NASPA [Words](https://scrabbleplayers.org/w/Welcome_to_NASPAWiki) [List](https://en.wikipedia.org/wiki/NASPA_Word_List). This is the dictionary that is used in tournaments but it must be licensed and a fee paid.  For this reason it is not included in the app. 

## TWL

The NASPA Word List is also called TWL. I was able to find a license-free version of this dictionary and it is included in the app.

## Collins

Outside of the USA, the [Collins Scrabble Words](https://en.wikipedia.org/wiki/Collins_Scrabble_Words) is used for tournaments. It used to be called SOWPODS. I was able to find a reasonably current plain text file of this dictionary. It has the most words of any of the dictionaries. 

## OSPD

The OSPD (Official Scrabble Players Dictionary) is an older version that NASPA was built from and was the official dictionary for tournaments in the USA and Canada. "Offensive" words were removed from this version. It has the least number of words of the other dictionaries. 

## Words

*scrabble-words-java* now has a crossword function. For this I thought I would include a standard dictionary to use. I found the *words* file on my Linux box (*/usr/share/dict/words*). I removed words with apostrophes as these would not be able to be processed by the app.

## Roll your own 

Any plain text file that ends in *.txt* and has one word per line can be used. Put the file in *src/main/resources/dicts* and add the name of the file without the *.txt* to *src/main/java/net/snortum/scrabblewords/model/DictionaryNames*.

Have fun!