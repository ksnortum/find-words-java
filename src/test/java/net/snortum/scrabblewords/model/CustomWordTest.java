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
public class CustomWordTest {
	private final String testWord = "punjabi";
	private final String testValueWord = "punjbi";

	@Test
	public void testScrabbleWord() {
		CustomWord thisWord = new CustomWord(testWord, testValueWord, false);
		assertEquals(testWord, thisWord.getWord());
		assertEquals(testValueWord, thisWord.getValueWord());
		assertEquals(17, thisWord.getValue());
	}

	@Test
	public void testBingo() {
		CustomWord thisWord = new CustomWord(testWord, testValueWord, true);
		assertEquals(testWord, thisWord.getWord());
		assertEquals(testValueWord, thisWord.getValueWord());
		assertEquals(67, thisWord.getValue());
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testScrabbleWordNull() {
		new CustomWord(null, testValueWord, false);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testScrabbleValueWordNull() {
		new CustomWord(testWord, null, false);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testScrabbleWordEmpty() {
		new CustomWord("", testValueWord, false);
	}
	
	@Test
	public void testEqualsObject() {
		CustomWord thisWord = new CustomWord(testWord, testValueWord, false);
		CustomWord thatWord = new CustomWord(testWord, testValueWord, false);
		assertEquals(thisWord, thatWord);
	}
	
	@Test
	public void testEqualsObjectNotIsBingo() {
		CustomWord thisWord = new CustomWord(testWord, testValueWord, false);
		CustomWord thatWord = new CustomWord(testWord, testValueWord, true);
		assertNotEquals(thisWord, thatWord);
	}
	
	@Test
	public void testEqualsObjectNotValueWord() {
		CustomWord thisWord = new CustomWord(testWord, testValueWord, false);
		CustomWord thatWord = new CustomWord(testWord, testWord, false);
		assertNotEquals(thisWord, thatWord);
	}

	@Test
	public void testEqualsObjectNotWord() {
		CustomWord thisWord = new CustomWord(testWord, testValueWord, false);
		CustomWord thatWord = new CustomWord(testValueWord, testValueWord, false);
		assertNotEquals(thisWord, thatWord);
	}
	
	@Test
	public void testScrabbleWordSort() {
		CustomWord word1 = new CustomWord("trick", "trick", false);
		CustomWord word2 = new CustomWord("pat", "pat", false);
		CustomWord word3 = new CustomWord("tap", "tap", false);
		List<CustomWord> list = Arrays.asList(word2, word1, word3);
		Collections.sort(list);
		assertEquals(word1, list.get(0));
		assertEquals(word2, list.get(1));
		assertEquals(word3, list.get(2));
	}
}
