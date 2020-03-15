package net.snortum.scrabblewords.model;

import static org.junit.Assert.*;

import org.junit.Test;

import net.snortum.scrabblewords.model.DictionaryName;
import net.snortum.scrabblewords.model.InputData;

/**
 * @author Knute Snortum
 * @version 2.1.1
 */
public class InputDataTest {
	private final String letters = "ABC";
	private final String contains = "A";
	private final String startsWith = "A";
	private final String endsWith = "A";

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
		assertTrue( data.getContains().equals( contains ) );
	}

	@Test
	public void testGetDictionaryName() {
		InputData data = new InputData.Builder( letters ).build();
		assertTrue( DictionaryName.OSPD.equals( data.getDictionaryName() ) );
		data = new InputData.Builder( letters )
				.dictionaryName( DictionaryName.SOWPODS )
				.build();
		assertTrue( DictionaryName.SOWPODS.equals( data.getDictionaryName() ) );
	}
	

	@Test
	public void testStartsWith() {
		InputData data = new InputData.Builder( letters )
				.startsWith( startsWith )
				.build();
		assertTrue( data.getStartsWith().equals( startsWith ) );
	}
	
	@Test
	public void testEndsWith() {
		InputData data = new InputData.Builder( letters )
				.endsWith( endsWith )
				.build();
		assertTrue( data.getEndsWith().equals( endsWith ) );
	}
	
	@Test
	public void testInputDataEmpty() {
		InputData data = new InputData.Builder("").build();
		assertTrue( data.isEmpty() );
	}
}
