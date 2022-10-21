package net.snortum.scrabblewords;

import javafx.application.Application;
import javafx.stage.Stage;
import net.snortum.scrabblewords.view.FindWords;

/**
 * Starts the main GUI for the FindWords application.
 * 
 * @author Knute Snortum
 * @version 2017.07.05
 */
public class FindWordsLauncher extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) {
		new FindWords().buildAndShowGui(primaryStage);
	}

}
