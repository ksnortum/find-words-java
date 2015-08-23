package net.snortum.scrabble_words.model;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Knute
 * @version 0.1
 */
public class ScrabbleWordTest {
	private final String testWord = "punjabi";
	private final int testValue = 18;

	/**
	 * Test method for {@link net.snortum.scrabble_words.model.ScrabbleWord#ScrabbleWord(java.lang.String)}.
	 * Test method for {@link net.snortum.scrabble_words.model.ScrabbleWord#getWord()}.
	 * Test method for {@link net.snortum.scrabble_words.model.ScrabbleWord#getValue()}.
	 */
	@Test
	public void testScrabbleWord() {
		ScrabbleWord thisWord = new ScrabbleWord(testWord);
		assertEquals(testWord, thisWord.getWord());
		assertEquals(testValue, thisWord.getValue());
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testScrabbleWordNull() {
		new ScrabbleWord(null);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testScrabbleWordEmpty() {
		new ScrabbleWord("");
	}

	/**
	 * Test method for {@link net.snortum.scrabble_words.model.ScrabbleWord#equals(java.lang.Object)}.
	 */
	@Test
	public void testEqualsObject() {
		ScrabbleWord thisWord = new ScrabbleWord(testWord);
		ScrabbleWord thatWord = new ScrabbleWord(testWord);
		assertTrue(thisWord.equals(thatWord));
	}

}
