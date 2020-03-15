package net.snortum.scrabblewords.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Knute Snortum
 * @version 2.1.1
 */
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
		InputData data = new InputData.Builder("")
				.dictionaryName(DictionaryName.TWL)
				.build();
		ScrabbleDictionary dictionary = new ScrabbleDictionary(data.getDictionaryName());
		List<String> validWords = dictionary.getValidWords();
		assertEquals(testWords, validWords);
	}

}
