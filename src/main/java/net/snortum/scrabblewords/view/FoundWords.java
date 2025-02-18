package net.snortum.scrabblewords.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.snortum.scrabblewords.model.CustomWord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

/**
 * Displays the words that are found after the user enters data
 * and presses "submit"
 * 
 * @author Knute Snortum
 * @version 2.10.0
 */
public class FoundWords {
	private static final Logger LOG = LogManager.getLogger(FoundWords.class);
	private static final int VIEW_WIDTH = 150;
	private static final int VIEW_WIDTH_DEFINE = 600;
	private static final int VIEW_HEIGHT = 300;
	
	private final Set<CustomWord> words;
	private final Stage stage;
	private final boolean dictionaryDefinitions;
	private final boolean isScrabble;
	
	/**
	 * @param words
	 *            a set of {@link CustomWord}s
	 * @param stage
	 *            the {@link Stage} to create a modal dialogue on
	 * @param dictionaryDefinitions
	 *            does the dictionary that these words are based on have definitions?
	 */
	public FoundWords(Set<CustomWord> words, Stage stage, boolean dictionaryDefinitions, boolean isScrabble) {
		this.words = words;
		this.stage = stage;
		this.dictionaryDefinitions = dictionaryDefinitions;
		this.isScrabble = isScrabble;
	}
	
	/**
	 * Display the set of found Scrabble words and values
	 */
	public void display() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("In display()");
		}

		if (words.isEmpty()) {
			new NothingFound(stage).display();
			return;
		}

		Stage dialog = new Stage();
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(stage.getScene().getWindow());
		ObservableList<CustomWord> data = FXCollections.observableArrayList(words);
		TableView<CustomWord> tableView = new TableView<>(data);
		tableView.setPrefSize(dictionaryDefinitions ? VIEW_WIDTH_DEFINE : VIEW_WIDTH, VIEW_HEIGHT);

		TableColumn<CustomWord, String> wordColumn = new TableColumn<>("Word");
		wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
		tableView.getColumns().add(wordColumn);

		if (isScrabble) {
			TableColumn<CustomWord, Integer> valueColumn = new TableColumn<>("Score");
			valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
			tableView.getColumns().add(valueColumn);
		}

		if (dictionaryDefinitions) {
			TableColumn<CustomWord, String> definitionColumn = new TableColumn<>("Definition");
			definitionColumn.setCellValueFactory(new PropertyValueFactory<>("definition"));
			tableView.getColumns().add(definitionColumn);
		}

		VBox root = new VBox(10);
		root.setPadding(new Insets(10));

		ButtonBar buttonBar = new ButtonBar();
		Button exit = new Button("OK");
		exit.setOnAction(event -> dialog.close());
		buttonBar.getButtons().add(exit);

		root.getChildren().addAll(tableView, buttonBar);
		Scene dialogScene = new Scene(root);
		dialogScene.setOnKeyPressed((KeyEvent keyEvent) -> {
			if (keyEvent.getCode() == KeyCode.ESCAPE) {
				dialog.close();
			}
		});
		dialogScene.getStylesheets().add("/css/found-words.css");

		dialog.setScene(dialogScene);
		dialog.show();
	}
}
