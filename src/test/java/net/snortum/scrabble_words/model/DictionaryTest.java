package net.snortum.scrabble_words.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.Before;

public class DictionaryTest {
	
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
		Dictionary dictionary = new Dictionary(data);
		Map<String, String> validWords = dictionary.getValidWords();
		
		for (String key : testWords.keySet()) {
			assertTrue(validWords.remove(key) != null);
		}
		
		assertTrue(validWords.isEmpty());
	}

}
