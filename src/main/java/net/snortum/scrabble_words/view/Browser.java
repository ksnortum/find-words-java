package net.snortum.scrabble_words.view;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * Create a {@link WebView} in a {@link Region} with the width and height based
 * on the dimensions of the region.
 * 
 * @author Knute Snortum
 * @version 0.1
 */
public class Browser extends Region {

	private final WebView browser = new WebView();
	private final WebEngine webEngine = browser.getEngine();

	/**
	 * Create a browser ({@link WebView}) from the passed in URL.
	 * 
	 * @param url
	 *            the URL to load the browser from
	 */
	public Browser(String url) {
		webEngine.load(url);
		getChildren().add(browser);
	}

	@Override
	protected void layoutChildren() {
		double w = getWidth();
		double h = getHeight();
		double x = 0, y = 0, baseline = 0;
		layoutInArea(browser, x, y, w, h, baseline, HPos.CENTER, VPos.CENTER);
	}

	@Override
	protected double computePrefWidth(double height) {
		return getWidth();
	}

	@Override
	protected double computePrefHeight(double width) {
		return getHeight();
	}

	/**
	 * @return the browser's {@link WebEngine}
	 */
	public WebEngine getWebEngine() {
		return webEngine;
	}
}
