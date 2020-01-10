package net.snortum.scrabblewords.view;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Base class of all web browser displays.
 * 
 * @author Knute Snortum
 * @version 2017.12.14
 */
public class WebPage {
	private static final Logger LOG = LogManager.getLogger();
	
	private final String title; 
	private final String resource; 
	private final String css; 
	private final double width; 
	private final double height; 
	
	protected WebPage(String title, String resource, String css, double width, 
			double height) {
		this.title = title;
		this.resource = resource;
		this.css = css;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Create web browser to display HTML
	 */
	public void display() {
		Stage stage = new Stage();
		stage.setTitle(title);
		LOG.debug("resource = " + resource);
		URL url = getClass().getResource(resource);

		if (url == null) {
			LOG.error("Could not find \"" + resource + "\"");
			return;
		}

		Browser browser = new Browser(url.toExternalForm());
		URL styleSheetUrl = getClass().getResource(css);

		if (styleSheetUrl == null) {
			LOG.error("Could not find \"" + css + "\"");
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug(title + " page style sheet location: "
						+ styleSheetUrl.toString());
			}

			browser.getWebEngine()
					.setUserStyleSheetLocation(styleSheetUrl.toString());
		}

		Scene scene = new Scene(browser, width, height);
		stage.setScene(scene);
		stage.show();
	}
}
