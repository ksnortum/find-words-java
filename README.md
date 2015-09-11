# README for scrabble-words-java #

**scrabble-words-java** is a JavaFx implementation of my project [scrabble-words](https://github.com/ksnortum/scrabble-words), originally written in Perl as a command line program. 

## Building ##

This project uses [Maven](http://maven.apache.org/) to build an executable jar file.  From the command line, execute:

	mvn package

## Running ##

If you have Java in your PATH, you can execute the jar file from the command line:

	java -jar Scrabble-Words-0.x.x.jar
	
## TODO ##

* Validate that only one dot is in letters
* Add ability to do two wild cards (dots)
* Better progress bar when using wild cards
* Add help and/or hover text


