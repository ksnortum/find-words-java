package net.snortum.scrabblewords.view;

/**
 * Display the about text in a web browser view.
 * 
 * @author Knute Snortum
 * @version 2017.07.08
 */
public class AboutPage extends WebPage {
	private static final String TITLE = "About";
	private static final String RESOURCE = "/about.html";
	private static final String CSS = "/main.css";
	private static final double WIDTH = 500.0;
	private static final double HEIGHT = 400.0;
	
	public AboutPage() {
		super(TITLE, RESOURCE, CSS, WIDTH, HEIGHT);
	}
	
}
