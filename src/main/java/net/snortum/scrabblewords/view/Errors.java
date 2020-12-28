package net.snortum.scrabblewords.view;

import static java.util.stream.Collectors.joining;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.snortum.scrabblewords.controller.Validator;

/**
 * Display errors from validation.
 * 
 * @author Knute Snortum
 * @version 2.7.0
 * @see {@link Validator}
 */
public class Errors {
	private final List<String> errors;
	private final Stage stage;
	
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
		box.setPadding(new Insets(10));
		box.setSpacing(10);

		Text title = new Text("Please correct these errors");
		title.setFont(Font.font(null, FontWeight.BOLD, -1));
		box.getChildren().add(title);

		Label errorLabel = new Label(message);
		errorLabel.setWrapText(true);
		box.getChildren().add(errorLabel);

		Button exitButton = new Button("OK");
		exitButton.setOnAction(event -> dialog.close());
		ButtonBar buttonBar = new ButtonBar();
		buttonBar.getButtons().add(exitButton);
		box.getChildren().add(buttonBar);

		Scene dialogScene = new Scene(box);
		dialog.setScene(dialogScene);
		dialog.initStyle(StageStyle.UNDECORATED);
		dialog.show();
	}

}
