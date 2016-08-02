package net.snortum.scrabblewords.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import net.snortum.scrabblewords.model.DictionaryName;
import net.snortum.scrabblewords.model.InputData;
import net.snortum.scrabblewords.model.ScrabbleDictionary;

import org.junit.Before;

public class ScrabbleDictionaryTest {
	
	Map<String, String> testWords;
	
	@Before
	public void setup() {
		testWords = new HashMap<>();
		testWords.put("one", "");
		testWords.put("two", "");
		testWords.put("three", "");
	}

	/*
	 * DictionaryName should read from the test resource 
	 */
	@Test
	public void testDictionary() {
		InputData data = new InputData.Builder("abd")
				.dictionaryName(DictionaryName.twl)
				.build();
		ScrabbleDictionary dictionary = new ScrabbleDictionary(data);
		Map<String, String> validWords = dictionary.getValidWords();
		
		for (String key : testWords.keySet()) {
			assertTrue(validWords.remove(key) != null);
		}
		
		assertTrue(validWords.isEmpty());
	}

}
