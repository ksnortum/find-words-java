package net.snortum.scrabblewords;

/**
 * It is necessary to have a class with a {@code main()} method that does not
 * extend javafx.application.Application to launch the app.  This avoids the 
 * "JavaFX runtime cannot be found" error.   
 * 
 * @author Knute Snortum
 * @version 2.1.0
 */
public class ScrabbleWordsMain {

	public static void main(String[] args) {
		ScrabbleWordsLauncher.main(args);
	}

}
