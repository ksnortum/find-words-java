# README for scrabble-words-java

**scrabble-words-java** is a JavaFX implementation of my project [scrabble-words](https://github.com/ksnortum/scrabble-words), originally written in Perl as a command line program.  It takes the information you supply about the tiles in your hand and returns a list of possible words to play, sorted by highest score.

## Requirements

* Java 11 or greater.  For Java 1.8, see the `java8` branch.
* Maven 3.6.1 (Lower versions may work, but this is what the project was tested with)

(The `java8` branch is unfortunately way behind the Java 11 branch.)

## Building

This project uses [Maven](http://maven.apache.org/) to build an executable jar file.  From the command line, execute:

	mvn clean package

## Running

There are two ways to execute scrabble-words-java.  Using Maven, execute:

    mvn exec:java

Or, if you have Java in your PATH, you can execute the jar file from the command line:

	java -jar /path/to/Scrabble-Words-2.x.x.jar

# New in version 2
* faster processing, in general
* wildcards are scored correctly
* ability to have two wildcards in a hand	
* Crossword mode

# TODO
* possibly use FXML files