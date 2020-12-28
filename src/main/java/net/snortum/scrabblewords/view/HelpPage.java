package net.snortum.scrabblewords.view;

/**
 * Display the help text in a web browser view.
 * 
 * @author Knute Snortum
 * @version 2017.07.08
 */
public class HelpPage extends WebPage {
	private static final String TITLE = "Help";
	private static final String RESOURCE = "/html/help.html";
	private static final String CSS = "/css/web.css";
	private static final double WIDTH = 750.0;
	private static final double HEIGHT = 500.0;
	
	public HelpPage() {
		super(TITLE, RESOURCE, CSS, WIDTH, HEIGHT);
	}
	
}
