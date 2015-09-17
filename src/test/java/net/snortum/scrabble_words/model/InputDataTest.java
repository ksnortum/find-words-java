package net.snortum.scrabble_words.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class InputDataTest {
	private final String letters = "ABC";
	private final String contains = "A";

	@Test
	public void testGetLetters() {
		InputData data = new InputData.Builder( letters ).build();
		assertTrue( data.getLetters().equals( letters.toLowerCase() ) );
	}

	@Test
	public void testGetContains() {
		InputData data = new InputData.Builder( letters )
				.contains( contains )
				.build();
		assertTrue( data.getContains().equals( contains.toLowerCase() ) );
	}

	@Test
	public void testGetDictionaryName() {
		InputData data = new InputData.Builder( letters ).build();
		assertTrue( DictionaryName.twl.equals( data.getDictionaryName() ) );
		data = new InputData.Builder( letters )
				.dictionaryName( DictionaryName.sowpods )
				.build();
		assertTrue( DictionaryName.sowpods.equals( data.getDictionaryName() ) );
	}

	@Test
	public void testInputDataEmpty() {
		InputData data = new InputData.Builder("").build();
		assertTrue( data.isEmpty() );
	}
}
