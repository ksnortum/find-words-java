package net.snortum.scrabblewords.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * This immutable class retrieves a list of words from a Custom Dictionary
 * text file. The text file must be in the ClassPath with one word per line.
 * Words will be down-cased.
 *
 * @author Knute Snortum
 * @version 2.1.1
 */
public class CustomDictionary {
	private static final Logger LOG = LogManager.getLogger(CustomDictionary.class);
	static final String DICTIONARY_NULL = "Dictionary name cannot be null";
	private static final Map<DictionaryName, List<DictionaryElement>> words = new HashMap<>();

	private final DictionaryName dictionaryName;

	/**
	 * Create a new CustomDictionary object. Uses data to get the
	 * {@link DictionaryName}.
	 *
	 * @param dictionaryName
	 *				the dictionary name to use
	 * @throws NullPointerException
	 *              if dictionaryName is null
	 */
	public CustomDictionary(DictionaryName dictionaryName) {
		Objects.requireNonNull(dictionaryName, DICTIONARY_NULL);
		this.dictionaryName = dictionaryName;
	}

	/**
	 * Get a list of words from a text dictionary file. Cache words for multiple
	 * calls. CustomDictionary file name comes from the {@link InputData}
	 * field entered at object creation. CustomDictionary name must be in the
	 * class path.
	 *
	 * @return list of words from a text dictionary (uses Map for faster access)
	 */
	public List<DictionaryElement> getValidWords() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Getting dictionary words");
		}

		// Was this dictionary loaded already?
		if (words.containsKey(dictionaryName)) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Using pre-loaded dictionary");
			}
			return words.get(dictionaryName);
		}

		String dictionaryFile = "/dicts/" + dictionaryName.toString().toLowerCase() + ".txt";
		List<DictionaryElement> validWords = new ArrayList<>();
		InputStream in = getClass().getResourceAsStream(dictionaryFile);

		if (in == null) {
			LOG.error(String.format("Could not find resource \"%s\"", dictionaryName));
			return validWords;
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String line;
			String definition;
			String word;

			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\t");
				word = parts[0].toLowerCase();

				if (parts.length > 1) {
					definition = parts[1];
				} else {
					definition = null;
				}

				validWords.add(new DictionaryElement(word, definition));
			}
		} catch (IOException e) {
			LOG.error(e.toString());
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Size of valid word list: " + validWords.size());
		}

		words.put(dictionaryName, validWords);

		return validWords;
	}
}
