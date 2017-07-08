package net.snortum.scrabblewords;

import javafx.application.Application;
import javafx.stage.Stage;
import net.snortum.scrabblewords.view.ScrabbleWords;

/**
 * Starts the main GUI for the ScrabbleWords application.
 * 
 * @author Knute Snortum
 * @version 2017.07.05
 */
public class Main extends Application {

	public static void main(String[] args) {
		launch(Main.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		new ScrabbleWords().buildAndShowGui(primaryStage);
	}

}
