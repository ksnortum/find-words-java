package net.snortum.scrabblewords.model;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Knute Snortum
 * @version 2.4.0
 */
public class InputDataTest {
	private final String letters = "ABC";

	@Test
	public void testGetLetters() {
		InputData data = new InputData.Builder( letters ).build();
		assertEquals(data.getLetters(), letters.toLowerCase());
	}

	@Test
	public void testGetContains() {
		String contains = "A";
		InputData data = new InputData.Builder( letters )
				.contains(contains)
				.build();
		assertEquals(data.getContains(), contains);
	}

	@Test
	public void testGetDictionaryName() {
		InputData data = new InputData.Builder( letters ).build();
		assertEquals(DictionaryName.OSPD, data.getDictionaryName());
		data = new InputData.Builder( letters )
				.dictionaryName( DictionaryName.TWL )
				.build();
		assertEquals(DictionaryName.TWL, data.getDictionaryName());
	}
	

	@Test
	public void testStartsWith() {
		String startsWith = "A";
		InputData data = new InputData.Builder( letters )
				.startsWith(startsWith)
				.build();
		assertEquals(data.getStartsWith(), startsWith);
	}
	
	@Test
	public void testEndsWith() {
		String endsWith = "A";
		InputData data = new InputData.Builder( letters )
				.endsWith(endsWith)
				.build();
		assertEquals(data.getEndsWith(), endsWith);
	}
	
	@Test
	public void testInputDataEmpty() {
		InputData data = new InputData.Builder("").build();
		assertTrue( data.isEmpty() );
	}
	
	@Test
	public void testNumOfLetters() {
		String numOfLetters = "5";
		InputData data = new InputData.Builder( letters )
				.numOfLetters(numOfLetters)
				.build();
		assertEquals(data.getNumOfLetters(), numOfLetters);
	}

	@Test
	public void testGameType() {
		InputData data = new InputData.Builder(letters)
				.gameType(TypeOfGame.CROSSWORD)
				.build();
		assertEquals(data.getGameType(), TypeOfGame.CROSSWORD);
	}
}
