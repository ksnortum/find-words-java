package net.snortum.scrabblewords.model;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ScrabbleDictionaryTest {
	
	List<String> testWords;
	
	@Before
	public void setup() {
		testWords = new ArrayList<>();
		testWords.add("one");
		testWords.add("two");
		testWords.add("three");
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
		List<String> validWords = dictionary.getValidWords();
		
		for (String word : testWords) {
			assertTrue(validWords.remove(word));
		}
		
		assertTrue(validWords.isEmpty());
	}

}
