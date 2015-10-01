package net.snortum.scrabble_words.model;

import static java.util.stream.Collectors.toMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.log4j.Logger;

public class Dictionary {
	private static final Logger LOG = Logger.getLogger(Dictionary.class);
	static final String INPUTDATA_NULL = "Input Data cannot be null";
	static final String DICTIONARY_NULL = "Dictionary name cannot be null";

	private static Map<DictionaryName, Map<String, String>> words = new HashMap<>();

	private InputData data;

	/**
	 * Create a new Dictionary object. Uses data to get the
	 * {@link DictionaryName}.
	 * 
	 * @param data
	 *            the input data to use
	 * @throw IllegalArgumentException if data or dictionary name are null
	 */
	public Dictionary(InputData data) {
		if (data == null) {
			throw new IllegalArgumentException(INPUTDATA_NULL);
		}

		this.data = new InputData(data);

		if (this.data.getDictionaryName() == null) {
			throw new IllegalArgumentException(DICTIONARY_NULL);
		}
	}

	/**
	 * Get a list of words from a text dictionary file. Cache words for multiple
	 * calls. Dictionary file name comes from the @{link InputDate} field
	 * entered at object creation. Dictionary name must be in the class path.
	 * 
	 * @return list of words from a text dictionary (use Map for faster access)
	 */
	public Map<String, String> getValidWords() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Getting dictionary words");
		}

		// Was this dictionary loaded already?
		if (words.containsKey(data.getDictionaryName())) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Using pre-loaded dictionary");
			}
			return words.get(data.getDictionaryName());
		}

		String dictionaryFile = "/" + data.getDictionaryName() + ".txt";
		InputStream in = getClass().getResourceAsStream(dictionaryFile);
		Map<String, String> validWords = new HashMap<>();

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(in))) {
			validWords = br.lines()
					.map(word -> word.toLowerCase())
					.collect(toMap(Function.identity(), value -> ""));
		} catch (IOException e) {
			LOG.error(e.toString());
		}

		words.put(data.getDictionaryName(), validWords);

		return validWords;
	}
}
