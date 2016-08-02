package net.snortum.scrabble_words.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * @author Knute
 * @version 0.2
 */
public class ScrabbleWordTest {
	private final String testWord = "punjabi";
	private final int testValue = 18;
	private final int bingoValue = 68;

	@Test
	public void testScrabbleWord() {
		ScrabbleWord thisWord = new ScrabbleWord(testWord);
		assertEquals(testWord, thisWord.getWord());
		assertEquals(testValue, thisWord.getValue());
	}

	@Test
	public void testBingo() {
		boolean isBingo = true;
		ScrabbleWord thisWord = new ScrabbleWord(testWord, isBingo);
		assertEquals(testWord, thisWord.getWord());
		assertEquals(bingoValue, thisWord.getValue());
		isBingo = false;
		thisWord = new ScrabbleWord(testWord, isBingo);
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
	
	@Test (expected=IllegalArgumentException.class)
	public void testBingoWordNull() {
		new ScrabbleWord(null, true);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testBingoWordEmpty() {
		new ScrabbleWord("", true);
	}
	
	@Test
	public void testEqualsObject() {
		ScrabbleWord thisWord = new ScrabbleWord(testWord);
		ScrabbleWord thatWord = new ScrabbleWord(testWord);
		assertTrue(thisWord.equals(thatWord));
	}

	@Test
	public void testScrabbleWordSort() {
		ScrabbleWord word1 = new ScrabbleWord("trick");
		ScrabbleWord word2 = new ScrabbleWord("pat");
		ScrabbleWord word3 = new ScrabbleWord("tap");
		List<ScrabbleWord> list = Arrays.asList(word2, word1, word3);
		Collections.sort(list);
		assertEquals(word1, list.get(0));
		assertEquals(word2, list.get(1));
		assertEquals(word3, list.get(2));
	}
}
