package net.snortum.scrabblewords.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import net.snortum.scrabblewords.controller.Validator;
import net.snortum.scrabblewords.controller.WordSearcher;
import net.snortum.scrabblewords.event.ProgressEvent;
import net.snortum.scrabblewords.model.DictionaryName;
import net.snortum.scrabblewords.model.InputData;
import net.snortum.scrabblewords.model.ScrabbleWord;

import net.snortum.scrabblewords.model.TypeOfGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Display possible Scrabble words to play based on your "tile" letters, a
 * Scrabble dictionary, and certain restrictions.
 * 
 * @author Knute Snortum
 * @version 2.8.0
 */
public class ScrabbleWords {
	private static final Logger LOG = LogManager.getLogger(ScrabbleWords.class);
	private static final String AVAILABLE_LETTERS_TEXT = "Available Letters:";
	private static final String CANT_HAVE_LETTERS_TEXT = "Can't Have Letters:";

	private final TextField letters = new TextField();
	private final TextField contains = new TextField();
	private final TextField startsWith = new TextField();
	private final TextField endsWith = new TextField();
	private final ProgressBar progress = new ProgressBar(0.0);
	private final ChoiceBox<DictionaryName> dictionary = new ChoiceBox<>(
			FXCollections.observableArrayList(DictionaryName.values()));
	private final TextField numOfLetters = new TextField();
	private final ToggleGroup typeOfGameTG = new ToggleGroup();

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

		MenuBar menuBar = buildMenu(stage);
		GridPane grid = buildGrid(stage);
		ButtonBar buttonBar = buildButtons(stage);
		VBox box = new VBox(10);
		int top = 5, right = 25, bottom = 15, left = 25;
		box.setPadding(new Insets(top, right, bottom, left));
		box.getChildren().addAll(grid, buttonBar);

		VBox root = new VBox();
		root.getChildren().addAll(menuBar, box);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("ScrabbleWords");
		InputStream imageStream = getClass().getResourceAsStream("/image/letter-S.png");

		if (imageStream == null) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("Image stream for icon is null");
			}
		} else {
			stage.getIcons().add(new Image(imageStream));
		}

		stage.show();
	}

	/**
	 * Create the form to get the {@link InputData}.
	 * 
	 * @param stage
	 *            the primary stage from the main application
	 */
	private GridPane buildGrid(final Stage stage) {
		
		// Set up the grid pane
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);

		// Title
		Text title = new Text("Find Scrabble Words");
		title.setId("title");
		title.setFont(Font.font(null, FontWeight.BOLD, 30));
		int col = 0, row = 0, colSpan = 2, rowSpan = 1;
		grid.add(title, col, row, colSpan, rowSpan);

		// Type of Game
		RadioButton scrabbleRB = new RadioButton("Scrabble");
		scrabbleRB.setSelected(true);
		RadioButton crosswordRB = new RadioButton("Crossword");
		RadioButton wordleRB = new RadioButton("Wordle");

		scrabbleRB.setToggleGroup(typeOfGameTG);
		crosswordRB.setToggleGroup(typeOfGameTG);
		wordleRB.setToggleGroup(typeOfGameTG);
		HBox radioButtonBox = new HBox(10);
		radioButtonBox.getChildren().addAll(scrabbleRB, crosswordRB, wordleRB);

		col = 0;
		row++;
		colSpan = 3;
		rowSpan = 1;
		grid.add(radioButtonBox, col, row, colSpan, rowSpan);

		// Available, Can't Have
		col = 0;
		row++;
		Label availableLabel = new Label(AVAILABLE_LETTERS_TEXT);
		grid.add(availableLabel, col, row);
		Tooltip availableTooltip = new Tooltip(
				"Enter the tile letters you have available, or a dot for a blank tile");
		Tooltip cantHaveTooltip = new Tooltip(
				"Enter the letters that these words can't have in them");
		letters.setTooltip(availableTooltip);
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
		Button clearNumOfLetters = new Button("Clear");
		clearNumOfLetters.setDisable(true);
		clearNumOfLetters.setOnAction(event -> {
			numOfLetters.clear();
			numOfLetters.requestFocus();
		});
		grid.add(clearNumOfLetters, col, row);

		// ScrabbleDictionary
		col = 0;
		row++;
		grid.add(new Label("Dictionary: "), col, row);
		dictionary.setValue(DictionaryName.COLLINS);
		col = 1;
		grid.add(dictionary, col, row);

		// Add radio button event listener
		typeOfGameTG.selectedToggleProperty().addListener((ob, o, n) -> {
			switch (getTypeOfGame()) {
				case SCRABBLE:
					numOfLetters.clear();
					numOfLetters.setDisable(true);
					clearNumOfLetters.setDisable(true);
					availableLabel.setText(AVAILABLE_LETTERS_TEXT);
					letters.setTooltip(availableTooltip);
					break;
				case CROSSWORD:
					numOfLetters.clear();
					numOfLetters.setDisable(false);
					clearNumOfLetters.setDisable(false);
					availableLabel.setText(AVAILABLE_LETTERS_TEXT);
					letters.setTooltip(availableTooltip);
					break;
				case WORDLE:
					numOfLetters.setText("5");
					numOfLetters.setDisable(false);
					clearNumOfLetters.setDisable(false);
					availableLabel.setText(CANT_HAVE_LETTERS_TEXT);
					letters.setTooltip(cantHaveTooltip);
					break;
			}
		});

		// Progress bar
		col = 1;
		row++;
		grid.add(progress, col, row);
		progress.setVisible(false);
		progress.addEventFilter(ProgressEvent.PROGRESS, progressEvent ->
				progress.setProgress(progressEvent.getThusFar()));

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

		return grid;
	}
	
	private ButtonBar buildButtons(final Stage stage) {
		
		// Submit button and action
		ButtonBar buttonBar = new ButtonBar();
		Button submit = new Button("Submit");
		buttonBar.getButtons().add(submit);
		submit.setOnAction(event -> searchForWords(stage));

		// Clear button
		Button clear = new Button("Clear");
		clear.setOnAction((ActionEvent event) -> clearText());
		buttonBar.getButtons().add(clear);

		// Exit app
		Button exit = new Button("Exit");
		exit.setOnAction(event -> Platform.exit());
		buttonBar.getButtons().add(exit);

		return buttonBar;
	}

	private MenuBar buildMenu(final Stage mainStage) {
		
		// Menu File
		Menu menuFile = new Menu("File");
		MenuItem clearItem = new MenuItem("Clear");
		clearItem.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
		clearItem.setOnAction(event -> clearText());

		MenuItem exitItem = new MenuItem("Quit");
		exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
		exitItem.setOnAction(event -> Platform.exit());
		menuFile.getItems().addAll(clearItem, exitItem);

		// Menu Help
		Menu menuHelp = new Menu("Help");
		MenuItem helpItem = new MenuItem("Help");
		helpItem.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
		helpItem.setOnAction(event -> new HelpPage().display(mainStage));
		MenuItem aboutItem = new MenuItem("About");
		aboutItem.setOnAction(event -> new AboutPage().display(mainStage));
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
				progress.setVisible(true);
				return ws.getWords();
			}
		};
		boolean dictionaryDefinitions = data.getDictionaryName().toString().contains("DEFINE");
		searchWords.setOnSucceeded(event -> {
			progress.setVisible(false);
			new FoundWords(searchWords.getValue(), stage, dictionaryDefinitions).display();
		});
		searchWords.setOnFailed(wse -> {
			LOG.error("Error in WordSearcher.getWords():");
			Throwable error = wse.getSource().getException();
			LOG.error(error);
			StackTraceElement[] errs = error.getStackTrace();
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

		// Build data and validate
		InputData data = new InputData.Builder(letters.getText())
				.gameType(getTypeOfGame())
				.contains(contains.getText())
				.startsWith(startsWith.getText())
				.endsWith(endsWith.getText())
				.dictionaryName(dictionary.getValue())
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
	 * Clear all text fields
	 */
	private void clearText() {
		letters.clear();
		contains.clear();
		startsWith.clear();
		endsWith.clear();

		if (getTypeOfGame() != TypeOfGame.WORDLE) {
			numOfLetters.clear();
		}

		letters.requestFocus();
	}

	/**
	 * @return the {@link TypeOfGame}
	 */
	private TypeOfGame getTypeOfGame() {
		TypeOfGame typeOfGame = TypeOfGame.SCRABBLE;
		RadioButton selectedRB = (RadioButton)typeOfGameTG.getSelectedToggle();

		if (selectedRB != null) {
			String text = selectedRB.getText().toUpperCase();
			typeOfGame = TypeOfGame.valueOf(text);
		}

		return typeOfGame;
	}
}
