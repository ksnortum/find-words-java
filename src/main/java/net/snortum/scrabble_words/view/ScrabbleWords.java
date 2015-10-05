package net.snortum.scrabble_words.view;

import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
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
 * @author Knute Snortum
 * @version 1.2
 */
public class ScrabbleWords extends Application {
	private static final Logger LOG = Logger.getLogger(ScrabbleWords.class);
	private static final String CONTAINS_LETTERS = "Contains Letters:";
	private static final String CONTAINS_RE = "Contains Regex:";
	private static final String USE_LETTERS = "use letters";
	private static final String USE_REGEX = "use regex";
	private static final int HELP_PAGE_WIDTH = 750;
	private static final int HELP_PAGE_HEIGHT = 500;
	private static final String HELP_PAGE_CSS = "main.css";
	private static final int ABOUT_PAGE_WIDTH = 500;
	private static final int ABOUT_PAGE_HEIGHT = 400;
	private static final String ABOUT_PAGE_CSS = "main.css";

	private final TextField letters = new TextField();
	private final TextField contains = new TextField();
	private final TextField startsWith = new TextField();
	private final TextField endsWith = new TextField();
	private final ProgressBar progress = new ProgressBar(0.0);
	private final ToggleGroup group = new ToggleGroup();
	private final ChoiceBox<DictionaryName> dictionary = new ChoiceBox<>(
			FXCollections.observableArrayList(DictionaryName.values()));

	/**
	 * Launch the ScrabbleWords application
	 * 
	 * @param args
	 *            not used
	 */
	public static void main(String[] args) {
		launch(ScrabbleWords.class, args);
	}

	/**
	 * Create the form to get the {@link InputData} and validate it, displaying
	 * error messages, if any. When "submit" is pressed, display all suggested
	 * words that match the restrictions of the input data. When "clear" is
	 * pressed, clear all entered data from the form. Create help and about
	 * menus.
	 */
	@Override
	public void start(final Stage stage) throws Exception {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Starting application");
		}

		// Setup the GUI
		final GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		int top = 5, right = 25, bottom = 15, left = 25;
		grid.setPadding(new Insets(top, right, bottom, left));

		// Title
		final Text title = new Text("Find Scrabble Words");
		title.setId("title");
		title.setStyle("-fx-font-size:30; -fx-font-weight:bold");
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

		// Contains letters or regex
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

		// Starts with
		col = 0;
		row++;
		grid.add(new Label("Starts with:"), col, row);
		startsWith.setTooltip(
				new Tooltip("Words must start with these letter(s)"));
		col = 1;
		grid.add(startsWith, col, row);

		// Ends with
		col = 0;
		row++;
		grid.add(new Label("Ends with:"), col, row);
		endsWith.setTooltip(new Tooltip("Words must end with these letter(s)"));
		col = 1;
		grid.add(endsWith, col, row);

		// ScrabbleDictionary
		col = 0;
		row++;
		grid.add(new Label("Dictionary: "), col, row);
		dictionary.setValue(DictionaryName.twl);
		col = 1;
		grid.add(dictionary, col, row);

		// Progress bar
		col = 1;
		row++;
		grid.add(progress, col, row);
		progress.setVisible(false);

		// Submit button and action
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.BASELINE_RIGHT);
		Button submit = new Button("Submit");
		hbox.getChildren().add(submit);
		top = 0;
		right = 5;
		bottom = 0;
		left = 5;
		HBox.setMargin(submit, new Insets(top, right, bottom, left));
		submit.setOnAction((ActionEvent event) -> searchForWords(stage));
		
		// <Enter> = Submit, <Esc> = Quit
		grid.setOnKeyPressed((KeyEvent keyEvent) -> {
			switch (keyEvent.getCode()){
			case ENTER:
				searchForWords(stage);
				break;
			case ESCAPE:
				System.exit(0);
				break;
			default:
				break;
			}
		});

		// Clear button
		Button clear = new Button("Clear");
		clear.setOnAction((ActionEvent event) -> clearText());
		hbox.getChildren().add(clear);

		// Add buttons to box
		col = 1;
		row += rowSpan;
		grid.add(hbox, col, row);

		// Menu File
		Menu menuFile = new Menu("File");
		MenuItem clearItem = new MenuItem("Clear");
		clearItem.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
		clearItem.setOnAction((ActionEvent event) -> clearText());
		MenuItem exitItem = new MenuItem("Quit");
		exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
		exitItem.setOnAction((ActionEvent event) -> System.exit(0));
		menuFile.getItems().addAll(clearItem, exitItem);
		
		// Menu Help
		Menu menuHelp = new Menu("Help");
		MenuItem helpItem = new MenuItem("Help");
		helpItem.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
		helpItem.setOnAction((ActionEvent event) -> help());
		MenuItem aboutItem = new MenuItem("About");
		aboutItem.setOnAction((ActionEvent event) -> about());
		menuHelp.getItems().addAll(helpItem, aboutItem);

		// Create menus
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(menuFile, menuHelp);

		// Display scene
		VBox box = new VBox();
		Scene scene = new Scene(box);
		((VBox) scene.getRoot()).getChildren().addAll(menuBar);
		box.getChildren().add(grid);
		stage.setScene(scene);
		stage.setTitle("Find Scrabble Words");
		stage.show();
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
		Task<Set<ScrabbleWord>> searchWords = new Task<Set<ScrabbleWord>>() {
			@Override
			protected Set<ScrabbleWord> call() throws Exception {
				return ws.getWords();
			}
		};
		searchWords.setOnSucceeded((WorkerStateEvent wse) -> displayWords(
				searchWords.getValue(), stage));
		searchWords.setOnFailed((WorkerStateEvent wse) -> {
			LOG.error("Word Search Task got an error:");
			StackTraceElement[] errs = wse.getSource().getException()
					.getStackTrace();
			Arrays.asList(errs).forEach((err) -> LOG.error(err.toString()));
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
			return new InputData.Builder("").build();
		}
		return data;
	}

	/**
	 * Display the set of Scrabble words and values
	 * 
	 * @param words
	 *            a set of {@link ScrabbleWord}s
	 * @param stage
	 *            the {@link Stage} to create a modal dialogue on
	 */
	private void displayWords(Set<ScrabbleWord> words, Stage stage) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("In displayWords()");
		}

		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		ObservableList<String> data = FXCollections.observableArrayList();

		if (words.isEmpty()) {
			data.add("Nothing found");
		} else {
			words.stream().forEach(word -> data.add(word.toString()));
		}

		ListView<String> listView = new ListView<>(data);
		listView.setPrefSize(200, 300);
		// FIXME: Doesn't work 
		listView.setOnKeyPressed((KeyEvent keyEvent) -> {
			System.out.println("Key pressed in Display Words");
			if (keyEvent.getCode() == KeyCode.ESCAPE) {
				System.out.println("Escape in Display Words");
				System.exit(0);
			}
		});
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
	 *            the {@link Stage} to create the dialog on
	 */
	private void displayErrors(List<String> errors, Stage stage) {
		String message = errors.stream().collect(joining("\n"));
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);
		dialog.setTitle("Errors");

		VBox box = new VBox();
		GridPane grid = new GridPane();
		box.getChildren().add(grid);
		int top = 10, right = 10, bottom = 10, left = 10;
		grid.setPadding(new Insets(top, right, bottom, left));

		Text title = new Text("Please correct these errors");
		title.setStyle("-fx-font-weight: bold;");
		grid.add(title, 0, 0);

		Label errorLabel = new Label(message.toString());
		errorLabel.setWrapText(true);
		grid.add(errorLabel, 0, 2);

		Scene dialogScene = new Scene(box);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	private boolean containsIsRe(ToggleGroup group) {
		return group.getSelectedToggle() != null &&
				USE_REGEX.equals(group.getSelectedToggle().getUserData());
	}

	/**
	 * Clear all test fields
	 */
	private void clearText() {
		letters.clear();
		contains.clear();
		startsWith.clear();
		endsWith.clear();
		letters.requestFocus();
	}

	/**
	 * Create web browser to display help HTML
	 */
	private void help() {
		final Stage helpStage = new Stage();
		helpStage.setTitle("Help");
		URL url = ScrabbleWords.class.getResource("/help.html");

		if (url == null) {
			LOG.error("Could not find \"help.html\"");
			return;
		}

		String helpUrl = url.toExternalForm();
		Browser browser = new Browser(helpUrl);
		String cssText = getCssText(HELP_PAGE_CSS);
		setCssViaJavaScript(browser.getWebEngine(), cssText);
		double width = HELP_PAGE_WIDTH, height = HELP_PAGE_HEIGHT;
		Scene scene = new Scene(browser, width, height);
		helpStage.setScene(scene);
		helpStage.show();
	}

	/**
	 * Create a browser for the About menu item
	 */
	private void about() {
		final Stage aboutStage = new Stage();
		aboutStage.setTitle("About");
		URL url = ScrabbleWords.class.getResource("/about.html");

		if (url == null) {
			LOG.error("Could not find \"about.html\"");
			return;
		}

		String aboutUrl = url.toExternalForm();
		Browser browser = new Browser(aboutUrl);
		String cssText = getCssText(ABOUT_PAGE_CSS);
		setCssViaJavaScript(browser.getWebEngine(), cssText);
		double width = ABOUT_PAGE_WIDTH, height = ABOUT_PAGE_HEIGHT;
		Scene scene = new Scene(browser, width, height);
		aboutStage.setScene(scene);
		aboutStage.show();
	}

	/**
	 * Read a CSS file
	 * 
	 * @param fileName
	 *            the name and path of the CSS file
	 * @return a sting that is the concatenation of all CSS text lines
	 */
	private String getCssText(String fileName) {
		InputStream is = getClass().getClassLoader()
				.getResourceAsStream(fileName);

		if (is == null) {
			LOG.error(fileName + " not found");
			return "";
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		return br.lines().collect(joining(" "));
	}

	/**
	 * Add CSS to HTML via DOM and JavaScript
	 * 
	 * @param webEngine
	 *            the browser object's web engine to act on
	 * @param cssText
	 *            the CSS styling to add to the HTML
	 * @return the web engine with listener added
	 */
	private WebEngine setCssViaJavaScript(WebEngine webEngine, String cssText) {
		webEngine.getLoadWorker().stateProperty()
				.addListener((obs, oldState, newState) -> {
					if (newState == State.SUCCEEDED) {
						Document doc = webEngine.getDocument();
						Element styleNode = doc.createElement("style");
						org.w3c.dom.Text styleContent = doc
								.createTextNode(cssText);
						styleNode.appendChild(styleContent);
						doc.getDocumentElement().getElementsByTagName("head")
								.item(0).appendChild(styleNode);
						webEngine.executeScript(
								"document.documentElement.innerHTML");
					}
				});
		return webEngine;
	}
}
