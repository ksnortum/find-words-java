package net.snortum.scrabblewords.view;

import static java.util.stream.Collectors.joining;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.snortum.scrabblewords.controller.Validator;

/**
 * Display errors from validation.
 * 
 * @author Knute Snortum
 * @version 2017.07.07
 * @see Validator
 */
public class Errors {
	private List<String> errors;
	private Stage stage;
	
	/**
	 * @param errors
	 *            a list of error strings
	 * @param stage
	 *            the {@link Stage} to create the dialog on
	 */
	public Errors (List<String> errors, Stage stage) {
		this.errors = errors;
		this.stage = stage;
	}

	/**
	 * Display validation errors
	 */
	public void display() {
		String message = errors.stream().collect(joining(System.lineSeparator()));
		Stage dialog = new Stage();
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

}
