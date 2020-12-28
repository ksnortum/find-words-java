package net.snortum.scrabblewords.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Displayed when no words are found
 * 
 * @author Knute Snortum
 * @version 2.7.0
 */
public class NothingFound {
	private final Stage stage;

	/**
	 * @param stage
	 *            the {@link Stage} to create the dialog on
	 */
	public NothingFound(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Display "nothing found" message
	 */
	public void display() {
		Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(stage);

		VBox box = new VBox();
		box.setPadding(new Insets(10));
		box.setSpacing(10);
		Label messageLabel = new Label("Nothing Found");
		messageLabel.setFont(Font.font(16));
		box.getChildren().add(messageLabel);

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
