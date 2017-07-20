# README for scrabble-words-java #

**scrabble-words-java** is a JavaFx implementation of my project [scrabble-words](https://github.com/ksnortum/scrabble-words), originally written in Perl as a command line program. 

## Building ##

This project uses [Maven](http://maven.apache.org/) to build an executable jar file.  From the command line, execute:

	mvn package
	
To get HTML reports including JavaDocs, execute:

	mvn site javadoc:javadoc

## Running ##

If you have Java in your PATH, you can execute the jar file from the command line:

	java -jar Scrabble-Words-1.1.x.jar
	
## TODO ##

* Add ability to do two wild cards (dots) 
* Better scoring of wild cards.  Should not add wild card's value to total score 
* Better progress bar when using wild cards (use two progress bars, new one for alphabet?)

