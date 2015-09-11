package net.snortum.scrabble_words.view;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.snortum.scrabble_words.model.DictionaryName;
import net.snortum.scrabble_words.model.InputData;
import net.snortum.scrabble_words.model.ScrabbleWord;
import net.snortum.scrabble_words.model.Validator;
import net.snortum.scrabble_words.model.WordSearcher;

/**
 * Display possible Scrabble words to play based on your "tile" letters, a
 * Scrabble dictionary, and certain restrictions.
 * 
 * @author Knute
 * @version 1.0
 */
public class ScrabbleWords extends Application {
	private static final Logger LOG = Logger.getLogger(ScrabbleWords.class);
	private static final String CONTAINS_LETTERS = "Contains Letters:";
	private static final String CONTAINS_RE = "Contains Regex:";
	private static final String USE_LETTERS = "use letters";
	private static final String USE_REGEX = "use regex";

	public static void main(String[] args) {
		launch(ScrabbleWords.class, args);
	}

	@Override
	public void start(final Stage stage) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Starting application");
		}

		// Setup the GUI
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Text title = new Text("Find Scrabble Words");
		title.setId("title");
		title.setStyle("-fx-font-size:30; -fx-font-weight:bold");
		int col = 0, row = 0, colSpan = 2, rowSpan = 1;
		grid.add(title, col, row, colSpan, rowSpan);

		col = 0;
		row++;
		grid.add(new Label("Available Letters:"), col, row);
		TextField letters = new TextField();
		letters.setTooltip(
				new Tooltip("Enter the tile letters you have available"));
		col = 1;
		grid.add(letters, col, row);

		final ToggleGroup group = new ToggleGroup();
		RadioButton btnContains = new RadioButton("Contains these letters");
		btnContains.setToggleGroup(group);
		btnContains.setSelected(true);
		btnContains.setUserData(USE_LETTERS);
		btnContains.setTooltip(new Tooltip("Use letter(s) in contains field"));
		col = 0;
		row++;
		grid.add(btnContains, col, row);

		RadioButton btnContainsRe = new RadioButton("Contains (with regex)");
		btnContainsRe.setToggleGroup(group);
		btnContainsRe.setUserData(USE_REGEX);
		btnContainsRe
				.setTooltip(new Tooltip("Use a regex in the contains field"));
		col = 0;
		row++;
		grid.add(btnContainsRe, col, row);

		col = 0;
		row++;
		Label lblContains = new Label(CONTAINS_LETTERS);
		grid.add(lblContains, col, row);
		TextField contains = new TextField();
		contains.setTooltip(new Tooltip(
				"Letter(s)/regex on the board that words must contain"));
		col = 1;
		grid.add(contains, col, row);

		// Set Contains label text based on radio button selection
		group.selectedToggleProperty()
				.addListener((ObservableValue<? extends Toggle> ov,
						Toggle old_toggle, Toggle new_toggle) -> {
					if (group.getSelectedToggle() != null) {
						if (USE_LETTERS.equals(
								group.getSelectedToggle().getUserData())) {
							lblContains.setText(CONTAINS_LETTERS);
						} else if (USE_REGEX.equals(
								group.getSelectedToggle().getUserData())) {
							lblContains.setText(CONTAINS_RE);
						} else {
							lblContains.setText("Unknown");
						}
					}
				});

		col = 0;
		row++;
		grid.add(new Label("Starts with:"), col, row);
		TextField startsWith = new TextField();
		startsWith.setTooltip(
				new Tooltip("Words must start with these letter(s)"));
		col = 1;
		grid.add(startsWith, col, row);

		col = 0;
		row++;
		grid.add(new Label("Ends with:"), col, row);
		TextField endsWith = new TextField();
		endsWith.setTooltip(new Tooltip("Words must end with these letter(s)"));
		col = 1;
		grid.add(endsWith, col, row);

		col = 0;
		row++;
		grid.add(new Label("Dictionary: "), col, row);
		ChoiceBox<DictionaryName> dictionary = new ChoiceBox<>(
				FXCollections.observableArrayList(DictionaryName.values()));
		dictionary.setValue(DictionaryName.twl);
		col = 1;
		grid.add(dictionary, col, row);

		col = 0;
		row++;
		grid.add(new Label("Calculating: "), col, row);
		col = 1;
		final ProgressBar progress = new ProgressBar(0.0);
		grid.add(progress, col, row);

		// Submit button and action
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.BASELINE_RIGHT);
		Button submit = new Button("Submit");
		hbox.getChildren().add(submit);
		col = 1;
		row += rowSpan;
		grid.add(hbox, col, row);
		submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				// Get data a validate
				String containsData = contains.getText();
				String containsReData = "";
				if (containsIsRe(group)) {
					containsReData = containsData;
					containsData = "";
				}
				InputData data = new InputData.Builder(letters.getText())
						.contains(containsData)
						.containsRe(containsReData)
						.startsWith(startsWith.getText())
						.endsWith(endsWith.getText())
						.dictionaryName(dictionary.getValue())
						.build();
				Validator validator = new Validator(data);
				List<String> errors = validator.validate();

				if (!errors.isEmpty()) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Errors returned from validation");
					}
					displayErrors(errors, stage);
					return;
				}

				// Create task for word searching
				WordSearcher ws = new WordSearcher(data, progress);
				Task<Set<ScrabbleWord>> searchWords = new Task<Set<ScrabbleWord>>() {
					@Override
					protected Set<ScrabbleWord> call() throws Exception {
						return ws.getWords();
					}
				};
				searchWords
						.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent event) {
						displayWords(searchWords.getValue(), stage);
					}
				});
				searchWords.setOnFailed(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent event) {
						LOG.error("Word Search Task got an error:");
						LOG.error(event.getSource().getException()
								.getStackTrace());
					}
				});

				// Run the word searching task
				Thread th = new Thread(searchWords);
				th.setDaemon(true);
				th.start();
			}
		});

		// Display scene
		Scene scene = new Scene(grid);
		stage.setScene(scene);
		stage.setTitle("Find Scrabble Words");
		stage.show();
	}

	/**
	 * Display the set of Scrabble words and values
	 * 
	 * @param words
	 *            a set of {@link ScrabbleWord}s
	 * @param stage
	 *            JavaFx {@link Stage}
	 */
	private void displayWords(Set<ScrabbleWord> words, Stage stage) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("in displayWords()");
		}
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		ObservableList<String> data = FXCollections.observableArrayList();

		if (words.size() == 0) {
			data.add("Nothing found");
		} else {
			for (ScrabbleWord sw : words) {
				data.add(sw.toString());
			}
		}

		ListView<String> listView = new ListView<>(data);
		listView.setPrefSize(200, 300);
		Scene dialogScene = new Scene(listView);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	/**
	 * Display validation errors
	 * 
	 * @param errors
	 *            a list of error strings
	 * @param stage
	 *            the {@link Stage} to create the dialog in
	 */
	private void displayErrors(List<String> errors, Stage stage) {
		StringBuilder message = new StringBuilder();

		for (String error : errors) {
			if (!message.toString().isEmpty()) {
				message.append("\n");
			}
			message.append(error);
		}

		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		dialog.setTitle("Errors");

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));

		Text title = new Text("Please correct these errors");
		title.setStyle("-fx-font-weight: bold;");
		grid.add(title, 0, 0);

		Label errorLabel = new Label(message.toString());
		errorLabel.setWrapText(true);
		grid.add(errorLabel, 0, 2);

		Scene dialogScene = new Scene(grid);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	private boolean containsIsRe(ToggleGroup group) {
		return group.getSelectedToggle() != null &&
				USE_REGEX.equals(group.getSelectedToggle().getUserData());
	}
}
