package net.snortum.scrabblewords.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Knute Snortum
 * @version 2.6.0
 */
public class CustomDictionaryTest {
	
	private final List<DictionaryElement> testWords = new ArrayList<>();
	private final List<DictionaryElement> testWordsWithDefinitions = new ArrayList<>();
	
	@Before
	public void setup() {
		testWords.add(new DictionaryElement("one", null));
		testWords.add(new DictionaryElement("two", null));
		testWords.add(new DictionaryElement("three", null));

		testWordsWithDefinitions.add(new DictionaryElement("one", "the number one"));
		testWordsWithDefinitions.add(new DictionaryElement("two", "the number two"));
		testWordsWithDefinitions.add(new DictionaryElement("three", "the number three"));
	}

	@Test
	public void testDictionary() {
		InputData data = new InputData.Builder("")
				.dictionaryName(DictionaryName.TWL)
				.build();
		CustomDictionary dictionary = new CustomDictionary(data.getDictionaryName());
		List<DictionaryElement> validWords = dictionary.getValidWords();
		assertEquals(testWords, validWords);
	}

	@Test
	public void testDictionaryWithDefinitions() {
		InputData data = new InputData.Builder("")
				.dictionaryName(DictionaryName.COLLINS_DEFINE)
				.build();
		CustomDictionary dictionary = new CustomDictionary(data.getDictionaryName());
		List<DictionaryElement> validWords = dictionary.getValidWords();
		assertEquals(testWordsWithDefinitions, validWords);
	}
}
