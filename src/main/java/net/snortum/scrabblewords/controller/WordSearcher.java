package net.snortum.scrabblewords.controller;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.ProgressBar;

import net.snortum.scrabblewords.model.InputData;
import net.snortum.scrabblewords.model.ScrabbleDictionary;
import net.snortum.scrabblewords.model.ScrabbleWord;

/**
 * This immutable class will roll through the selected word dictionary, checking
 * if the available "tiles" (letters) and patterns match the dictionary word
 * 
 * @author Knute Snortum
 * @version 2.2.1
 */
public class WordSearcher {
	private static final Logger LOG = LogManager.getLogger(WordSearcher.class);

	private final InputData data;
	private final ProgressBar progress;

	/**
	 * Create a WordSearcher object, passing in a progress bar to update as work is
	 * done
	 * 
	 * @param data     the {@link InputData} to use
	 * @param progress the {@link ProgressBar} to update, or null
	 */
	public WordSearcher(InputData data, ProgressBar progress) {
		if (data == null) {
			throw new IllegalArgumentException("InputData cannot be null");
		}
		
		this.data = data;
		
		// ProgressBar can be null
		this.progress = progress;
		
		if (progress != null) {
			progress.setVisible(false);
		}
	}

	/**
	 * Get a list of words from the selected dictionary that match the requirements
	 * of the {@link InputData}.
	 * 
	 * @return a sorted Set containing the words that are found
	 */
	public Set<ScrabbleWord> getWords() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("In getWords()");
		}

		// Get valid words from dictionary
		ScrabbleDictionary dict = new ScrabbleDictionary(data.getDictionaryName());
		List<String> validWords = dict.getValidWords();

		// Compile regex once for speed
		Pattern pattern = buildPattern();

		String containsLetters = getLettersFromContains();
		String dataLetters = getValidDataLetters(containsLetters);
		String searchLetters = dataLetters + containsLetters + data.getStartsWith() + data.getEndsWith();
		searchLetters = searchLetters.toLowerCase();
		String wildcards = getWildcards();
		Set<ScrabbleWord> words = new TreeSet<>();

		// ProgressBar variables
		double inc = 1.0 / validWords.size();
		double thusFar = 0.0;
		
		if (progress != null) {
			progress.setVisible(true);
		}

		for (String word : validWords) {
			if (progress != null) {
				progress.setProgress(thusFar);
			}
			
			thusFar += inc;
			StringBuilder valueLetters = new StringBuilder();

			// Skip if dictionary word is longer than all possible letters
			if (word.length() > searchLetters.length() + wildcards.length()) {
				continue;
			}

			// Skip if any part of the word doesn't match the pattern
			if (pattern != null && !pattern.matcher(word).find()) {
				continue;
			}

			String dictWord = word; // make copy of word

			// Loop through letters to see if they can make up the dict word
			for (String letter : searchLetters.split("")) {
				if (dictWord.contains(letter)) {
					dictWord = dictWord.replaceFirst(letter, "");
					valueLetters.append(letter);
				}

				if (dictWord.isEmpty()) {
					break;
				}
			}

			// Loop through the wildcards, if any, and match any dict letter
			int i = 0;
			while (!dictWord.isEmpty() && i < wildcards.length()) {
				// remove any character
				dictWord = dictWord.substring(1);
				i++;
			}

			// All the letters in the dict word have been accounted for, so make a ScrabbleWord
			if (dictWord.isEmpty()) {
				boolean isBingo = false;

				if (word.length() - containsLetters.length() - data.getStartsWith().length()
						- data.getEndsWith().length() >= 7) {
					isBingo = true;
				}

				words.add(new ScrabbleWord(word, valueLetters.toString(), isBingo));

			}

		}

		if (progress != null) {
			progress.setVisible(false);
		}

		return words;
	}

	/**
	 * Return only letters. Since a regex can contain escaped characters, return
	 * only non-escaped
	 */
	private String getLettersFromContains() {
		StringBuilder letters = new StringBuilder();
		boolean isEscapeCharacter = false;

		for (String letter : data.getContains().split("")) {
			if (letter.matches("[a-zA-Z]") && !isEscapeCharacter) {
				letters.append(letter);
			}

			isEscapeCharacter = "\\".equals(letter);
		}

		return letters.toString();
	}

	/**
	 * Remove any capital letters and from dataLetters that are in Contains,
	 * StartsWith, EndsWith or wildcards (.)
	 */
	private String getValidDataLetters(String containsLetters) {
		String dataLetters = data.getLetters();
		dataLetters = removeCapitals(containsLetters, dataLetters);
		dataLetters = removeCapitals(data.getStartsWith(), dataLetters);
		dataLetters = removeCapitals(data.getEndsWith(), dataLetters);
		dataLetters = dataLetters.replaceAll("\\.", "");

		if (LOG.isDebugEnabled()) {
			LOG.debug("Valid data letters = " + dataLetters);
		}

		return dataLetters;
	}

	/**
	 * Remove any capital letters in word from dataLetters
	 */
	private String removeCapitals(String word, String dataLetters) {
		for (int i = 0; i < word.length(); i++) {
			if (Character.isUpperCase(word.charAt(i))) {
				dataLetters = dataLetters.replace(word.substring(i, i + 1).toLowerCase(), "");
			}
		}

		return dataLetters;
	}

	/**
	 * Build and compile a pattern out of contains, startsWith, and endsWith
	 * 
	 * @return a compiled pattern or null if there is nothing to compile
	 */
	private Pattern buildPattern() {
		Pattern pattern = null;
		String patternString = lowerCaseNonEscapedLetters(data.getContains());

		if (!data.getStartsWith().isEmpty()) {
			patternString = "^" + data.getStartsWith().toLowerCase() + ".*" + patternString; 
		}

		if (!data.getEndsWith().isEmpty()) {
			if (!patternString.endsWith(".*")) {
				patternString += ".*";
			}

			patternString += data.getEndsWith().toLowerCase() + "$";
		}

		if (!patternString.isEmpty()) {
			try {
				pattern = Pattern.compile(patternString);
			} catch (PatternSyntaxException e) {
				String alertText = String.format(
						"Error compiling regex from contains, starts-with, and ends-with%nPattern: %s", patternString);
				LOG.error(alertText, e);
			}
		}

		return pattern;
	}

	/**
	 * Return the lower case of any non-escaped letters
	 */
	private String lowerCaseNonEscapedLetters(String input) {
		StringBuilder result = new StringBuilder();
		boolean isEscapeCharacter = false;

		for (String letter : input.split("")) {
			if (letter.matches("[a-zA-Z]") && !isEscapeCharacter) {
				result.append(letter.toLowerCase());
			} else {
				result.append(letter);
			}

			isEscapeCharacter = "\\".equals(letter);
		}

		return result.toString();
	}

	/**
	 * Return the wildcards (.) in letters, if any
	 */
	private String getWildcards() {
		StringBuilder wildcards = new StringBuilder();
		String letters = data.getLetters();

		while (letters.contains(".")) {
			wildcards.append(".");
			letters = letters.replaceFirst("\\.", "");
		}

		return wildcards.toString();
	}
}
