package net.snortum.scrabblewords.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * @author Knute
 * @version 2.0.0
 */
public class ScrabbleWordTest {
	private final String testWord = "punjabi";
	private final String testValueWord = "punjbi";

	@Test
	public void testScrabbleWord() {
		ScrabbleWord thisWord = new ScrabbleWord(testWord, testValueWord, false);
		assertEquals(testWord, thisWord.getWord());
		assertEquals(testValueWord, thisWord.getValueWord());
		assertEquals(17, thisWord.getValue());
	}

	@Test
	public void testBingo() {
		ScrabbleWord thisWord = new ScrabbleWord(testWord, testValueWord, true);
		assertEquals(testWord, thisWord.getWord());
		assertEquals(testValueWord, thisWord.getValueWord());
		assertEquals(67, thisWord.getValue());
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testScrabbleWordNull() {
		new ScrabbleWord(null, testValueWord, false);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testScrabbleValueWordNull() {
		new ScrabbleWord(testWord, null, false);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testScrabbleWordEmpty() {
		new ScrabbleWord("", testValueWord, false);
	}
	
	@Test
	public void testEqualsObject() {
		ScrabbleWord thisWord = new ScrabbleWord(testWord, testValueWord, false);
		ScrabbleWord thatWord = new ScrabbleWord(testWord, testValueWord, false);
		assertEquals(thisWord, thatWord);
	}
	
	@Test
	public void testEqualsObjectNotIsBingo() {
		ScrabbleWord thisWord = new ScrabbleWord(testWord, testValueWord, false);
		ScrabbleWord thatWord = new ScrabbleWord(testWord, testValueWord, true);
		assertNotEquals(thisWord, thatWord);
	}
	
	@Test
	public void testEqualsObjectNotValueWord() {
		ScrabbleWord thisWord = new ScrabbleWord(testWord, testValueWord, false);
		ScrabbleWord thatWord = new ScrabbleWord(testWord, testWord, false);
		assertNotEquals(thisWord, thatWord);
	}

	@Test
	public void testEqualsObjectNotWord() {
		ScrabbleWord thisWord = new ScrabbleWord(testWord, testValueWord, false);
		ScrabbleWord thatWord = new ScrabbleWord(testValueWord, testValueWord, false);
		assertNotEquals(thisWord, thatWord);
	}
	
	@Test
	public void testScrabbleWordSort() {
		ScrabbleWord word1 = new ScrabbleWord("trick", "trick", false);
		ScrabbleWord word2 = new ScrabbleWord("pat", "pat", false);
		ScrabbleWord word3 = new ScrabbleWord("tap", "tap", false);
		List<ScrabbleWord> list = Arrays.asList(word2, word1, word3);
		Collections.sort(list);
		assertEquals(word1, list.get(0));
		assertEquals(word2, list.get(1));
		assertEquals(word3, list.get(2));
	}
}
