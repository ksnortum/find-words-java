package net.snortum.scrabble_words.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class Dictionary {
	private static final Logger LOG = Logger.getLogger(Dictionary.class);
	static final String INPUTDATA_NULL = "Input Data cannot be null";
	static final String DICTIONARY_NULL = "Dictionary name cannot be null";
	private static Map<DictionaryName, List<String>> words = new HashMap<>();

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
		this.data = new InputData(data);

		if (data == null) {
			throw new IllegalArgumentException(INPUTDATA_NULL);
		}

		if (data.getDictionaryName() == null) {
			throw new IllegalArgumentException(DICTIONARY_NULL);
		}
	}

	/**
	 * Get a list of words from a text dictionary file. Cache words for multiple
	 * calls. Dictionary file name comes from the @{link InputDate} field
	 * entered at object creation. Dictionary name must be in the class path.
	 * 
	 * @return list of words from a text dictionary, or an empty list if any
	 *         errors are encountered
	 */
	public List<String> getValidWords() {
		if (words.containsKey(data.getDictionaryName())) {
			return words.get(data.getDictionaryName());
		}

		List<String> validWords = new ArrayList<>();
		String dictionaryFile = "/" + data.getDictionaryName() + ".txt";
		InputStream in = getClass().getResourceAsStream(dictionaryFile);

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(in))) {
			String thisWord = null;

			while ((thisWord = br.readLine()) != null) {
				validWords.add(thisWord.toLowerCase());
			}
		} catch (FileNotFoundException e) {
			LOG.error(e.getStackTrace());
		} catch (IOException e) {
			LOG.error(e.getStackTrace());
		}

		words.put(data.getDictionaryName(), validWords);
		return validWords;
	}
}
