package net.snortum.scrabblewords.view;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.snortum.scrabblewords.controller.Validator;
import net.snortum.scrabblewords.controller.WordSearcher;
import net.snortum.scrabblewords.model.DictionaryName;
import net.snortum.scrabblewords.model.InputData;
import net.snortum.scrabblewords.model.ScrabbleWord;

/**
 * Display possible Scrabble words to play based on your "tile" letters, a
 * Scrabble dictionary, and certain restrictions.
 * 
 * @author Knute Snortum
 * @version 2.5.3
 */
public class ScrabbleWords {
	private static final Logger LOG = LogManager.getLogger(ScrabbleWords.class);

	private final TextField letters = new TextField();
	private final TextField contains = new TextField();
	private final TextField startsWith = new TextField();
	private final TextField endsWith = new TextField();
	private final ProgressBar progress = new ProgressBar(0.0);
	private final ChoiceBox<DictionaryName> dictionary = new ChoiceBox<>(
			FXCollections.observableArrayList(DictionaryName.values()));
	private final CheckBox crossword = new CheckBox();
	private final TextField numOfLetters = new TextField();

	/**
	 * Build the main GUI form and display
	 * 
	 * @param stage
	 *            the primary stage from the main application
	 */
	public void buildAndShowGui(final Stage stage) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Starting application");
		}

		GridPane grid = buildGrid(stage);
		MenuBar menuBar = buildMenu();
		VBox box = new VBox();
		box.getChildren().addAll(menuBar, grid);
		Scene scene = new Scene(box);
		stage.setScene(scene);
		stage.setTitle("ScrabbleWords");
		stage.show();
	}

	/**
	 * Create the form to get the {@link InputData}.
	 * 
	 * @param stage
	 *            the primary stage from the main application
	 */
	private GridPane buildGrid(final Stage stage) {
		
		// Setup the grid pane
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		int top = 5, right = 25, bottom = 15, left = 25;
		grid.setPadding(new Insets(top, right, bottom, left));

		// Title
		Text title = new Text("Find Scrabble Words");
		title.setId("title");
		title.setFont(Font.font(null, FontWeight.BOLD, 30));
		int col = 0, row = 0, colSpan = 2, rowSpan = 1;
		grid.add(title, col, row, colSpan, rowSpan);

		// Available
		col = 0;
		row++;
		grid.add(new Label("Available Letters:"), col, row);
		letters.setTooltip(new Tooltip(
				"Enter the tile letters you have available, a dot for a blank tile"));
		col = 1;
		grid.add(letters, col, row);
		Button clearLetters = new Button("Clear");
		clearLetters.setOnAction(event -> {
			letters.clear();
			letters.requestFocus();
		});
		col = 2;
		grid.add(clearLetters, col, row);

		// Contains letters
		col = 0;
		row++;
		Label lblContains = new Label("Contains Letters:");
		grid.add(lblContains, col, row);
		contains.setTooltip(new Tooltip(
				"Letter(s)/regex on the board that words must contain"));
		col = 1;
		grid.add(contains, col, row);
		Button clearContains = new Button("Clear");
		clearContains.setOnAction(event -> {
			contains.clear();
			contains.requestFocus();
		});
		col = 2;
		grid.add(clearContains, col, row);

		// Starts with
		col = 0;
		row++;
		grid.add(new Label("Starts with:"), col, row);
		startsWith.setTooltip(
				new Tooltip("Words must start with these letter(s)"));
		col = 1;
		grid.add(startsWith, col, row);
		Button clearStartsWith = new Button("Clear");
		clearStartsWith.setOnAction(event -> {
			startsWith.clear();
			startsWith.requestFocus();
		});
		col = 2;
		grid.add(clearStartsWith, col, row);

		// Ends with
		col = 0;
		row++;
		grid.add(new Label("Ends with:"), col, row);
		endsWith.setTooltip(new Tooltip("Words must end with these letter(s)"));
		col = 1;
		grid.add(endsWith, col, row);
		Button clearEndsWith = new Button("Clear");
		clearEndsWith.setOnAction(event -> {
			endsWith.clear();
			endsWith.requestFocus();
		});
		col = 2;
		grid.add(clearEndsWith, col, row);

		// ScrabbleDictionary
		col = 0;
		row++;
		grid.add(new Label("Dictionary: "), col, row);
		dictionary.setValue(DictionaryName.COLLINS);
		col = 1;
		grid.add(dictionary, col, row);
		
		// Crossword Mode
		col = 0;
		row++;
		Label checkBoxLabel = new Label("Crossword Mode:");
		checkBoxLabel.setGraphic(crossword);
		checkBoxLabel.setContentDisplay(ContentDisplay.RIGHT);
		grid.add(checkBoxLabel, col, row);
		crossword.setTooltip(new Tooltip("unchecked = Scrabble mode, checked = crossword mode"));
		Button clearNumOfLetters = new Button("Clear");
		clearNumOfLetters.setDisable(true);
		crossword.setOnAction(event -> {
			numOfLetters.setDisable(!crossword.isSelected());
			clearNumOfLetters.setDisable(!crossword.isSelected());
		});
		
		// Progress bar
		col = 1;
		grid.add(progress, col, row);
		progress.setVisible(false);
		
		// Number of Letters to Match
		col = 0;
		row++;
		Label numOfLettersLabel = new Label("Number of Letters:");
		grid.add(numOfLettersLabel, col, row);
		numOfLetters.setDisable(true);
		col = 1;
		grid.add(numOfLetters, col, row);
		numOfLetters.setTooltip(new Tooltip(
				"The exact number of letters in a word.  Zero or blank means unlimited"));
		col = 2;
		clearNumOfLetters.setOnAction(event -> {
			numOfLetters.clear();
			numOfLetters.requestFocus();
		});
		grid.add(clearNumOfLetters, col, row);
		
		// <Enter> = Submit, <Esc> = Quit
		grid.setOnKeyPressed(keyEvent -> {
			switch (keyEvent.getCode()) {
			case ENTER:
				searchForWords(stage);
				break;
			case ESCAPE:
				Platform.exit();
				break;
			default:
				break;
			}
		});

		// Add buttons to box
		HBox hbox = buildButtons(stage);
		colSpan = 2;
		rowSpan = 1;
		col = 1;
		row += rowSpan;
		grid.add(hbox, col, row, colSpan, rowSpan);

		return grid;
	}
	
	private HBox buildButtons(final Stage stage) {
		
		// Submit button and action
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.BASELINE_RIGHT);
		Button submit = new Button("Submit");
		hbox.getChildren().add(submit);
		int top = 0, right = 5, bottom = 0, left = 5;
		HBox.setMargin(submit, new Insets(top, right, bottom, left));
		submit.setOnAction(event -> searchForWords(stage));

		// Clear button
		Button clear = new Button("Clear All");
		clear.setOnAction((ActionEvent event) -> clearText());
		hbox.getChildren().add(clear);
		top = 0; right = 5; bottom = 0; left = 0;
		HBox.setMargin(clear, new Insets(top, right, bottom, left));

		// Clear except Letters button
		Button clearExcept = new Button("Clear Except Avail");
		clearExcept.setOnAction(event -> clearTextExceptLetters());
		hbox.getChildren().add(clearExcept);

		return hbox;
	}

	private MenuBar buildMenu() {
		
		// Menu File
		Menu menuFile = new Menu("File");
		MenuItem clearItem = new MenuItem("Clear");
		clearItem.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
		clearItem.setOnAction(event -> clearText());

		MenuItem clearExceptItem = new MenuItem("Clear Except Avail");
		clearExceptItem.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
		clearExceptItem.setOnAction(event -> clearTextExceptLetters());

		MenuItem exitItem = new MenuItem("Quit");
		exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
		exitItem.setOnAction(event -> Platform.exit());
		menuFile.getItems().addAll(clearItem, clearExceptItem, exitItem);

		// Menu Help
		Menu menuHelp = new Menu("Help");
		MenuItem helpItem = new MenuItem("Help");
		helpItem.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
		helpItem.setOnAction(event -> new HelpPage().display());
		MenuItem aboutItem = new MenuItem("About");
		aboutItem.setOnAction(event -> new AboutPage().display());
		menuHelp.getItems().addAll(helpItem, aboutItem);

		// Create menus
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(menuFile, menuHelp);

		return menuBar;
	}

	/**
	 * Search for words in the background
	 * 
	 * @param stage
	 *            The stage where the input data is and to create an Errors
	 *            Display dialogue on
	 */
	private void searchForWords(final Stage stage) {

		// Validate input data
		InputData data = validateInputData(stage);
		if (data.isEmpty()) {
			return;
		}

		// Create task for word searching
		WordSearcher ws = new WordSearcher(data, progress);
		Task<Set<ScrabbleWord>> searchWords = new Task<>() {
			@Override
			protected Set<ScrabbleWord> call() {
				return ws.getWords();
			}
		};
		searchWords.setOnSucceeded(event -> new FoundWords(searchWords.getValue(), stage).display());
		searchWords.setOnFailed(wse -> {
			LOG.error("Word Search Task got an error:");
			StackTraceElement[] errs = wse.getSource().getException().getStackTrace();
			Arrays.asList(errs).forEach(err -> LOG.error(err.toString()));
		});

		// Run the word searching task
		Thread th = new Thread(searchWords);
		th.setDaemon(true);
		th.start();
	}

	/**
	 * Validate the entered data, returning an {@link InputData} object, or
	 * displaying errors, if any
	 * 
	 * @param stage
	 *            the stage to display the errors on
	 * @return the validated {@link InputData}
	 */
	private InputData validateInputData(final Stage stage) {

		// Get data a validate
		InputData data = new InputData.Builder(letters.getText())
				.contains(contains.getText())
				.startsWith(startsWith.getText())
				.endsWith(endsWith.getText())
				.dictionaryName(dictionary.getValue())
				.crosswordMode(crossword.isSelected())
				.numOfLetters(numOfLetters.getText())
				.build();
		Validator validator = new Validator(data);
		List<String> errors = validator.validate();

		if (!errors.isEmpty()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Errors returned from validation");
			}
			Errors errorsView = new Errors(errors, stage);
			errorsView.display();
			return new InputData.Builder("").build();
		}
		
		return data;
	}

	/**
	 * Clear all text fields, except letters
	 */
	private void clearTextExceptLetters() {
		contains.clear();
		startsWith.clear();
		endsWith.clear();
		numOfLetters.clear();
		contains.requestFocus();
	}

	/**
	 * Clear all text fields
	 */
	private void clearText() {
		letters.clear();
		contains.clear();
		startsWith.clear();
		endsWith.clear();
		numOfLetters.clear();
		letters.requestFocus();
	}

}
