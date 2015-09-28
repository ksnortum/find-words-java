package net.snortum.scrabble_words.view;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Browser extends Region {

	private final WebView browser = new WebView();
	private final WebEngine webEngine = browser.getEngine();

	public Browser(String Url) {
		webEngine.load(Url);
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
	
	public WebEngine getWebEngine() {
		return webEngine;
	}
}
