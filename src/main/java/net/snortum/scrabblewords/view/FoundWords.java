package net.snortum.scrabblewords.view;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.snortum.scrabblewords.model.ScrabbleWord;

/**
 * Displays the words that are found after the user enters data
 * and presses "submit."
 * 
 * @author Knute Snortum
 * @version 2017.07.07
 */
public class FoundWords {
	private static final Logger LOG = LogManager.getLogger();
	private static final int VIEW_WIDTH = 200;
	private static final int VIEW_HEIGHT = 300;
	
	private final Set<ScrabbleWord> words;
	private final Stage stage;
	
	/**
	 * @param words
	 *            a set of {@link ScrabbleWord}s
	 * @param stage
	 *            the {@link Stage} to create a modal dialogue on
	 */
	public FoundWords(Set<ScrabbleWord> words, Stage stage) {
		this.words = words;
		this.stage = stage;
	}
	
	/**
	 * Display the set of found Scrabble words and values
	 */
	public void display() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("In display()");
		}

		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		ObservableList<String> data = FXCollections.observableArrayList();

		if (words.isEmpty()) {
			data.add("Nothing found");
		} else {
			words.stream().forEach(word -> data.add(word.toString()));
		}

		ListView<String> listView = new ListView<>(data);
		listView.setPrefSize(VIEW_WIDTH, VIEW_HEIGHT); 
		Scene dialogScene = new Scene(listView);

		dialogScene.setOnKeyPressed((KeyEvent keyEvent) -> {
			if (keyEvent.getCode() == KeyCode.ESCAPE) {
				dialog.close();
			}
		});

		dialog.setScene(dialogScene);
		dialog.show();
	}
}
