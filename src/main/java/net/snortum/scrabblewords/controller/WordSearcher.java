package net.snortum.scrabblewords.controller;

import javafx.event.Event;
import javafx.event.EventTarget;

import net.snortum.scrabblewords.event.ProgressEvent;
import net.snortum.scrabblewords.model.DictionaryElement;
import net.snortum.scrabblewords.model.InputData;
import net.snortum.scrabblewords.model.ScrabbleDictionary;
import net.snortum.scrabblewords.model.ScrabbleWord;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * This immutable class will roll through the selected word dictionary, checking
 * if the available "tiles" (letters) and patterns match the dictionary word
 *
 * @author Knute Snortum
 * @version 2.8.0
 */
public class WordSearcher {
	private static final Logger LOG = LogManager.getLogger(WordSearcher.class);
	private static final String ALL_LETTERS = "abcdefghijklmnopqrstuvwxyz";

	private final InputData data;
	private final EventTarget eventTarget;

	/**
	 * Search for words based on the {@link InputData} passed in.  An event
	 * is fired to show the progress of the task.
	 *
	 * @param data the InputData to use
	 * @param eventTarget the {@link EventTarget} for the {@link ProgressEvent}
	 * @throws NullPointerException if either parameter is null
	 */
	public WordSearcher(InputData data, EventTarget eventTarget) {
		Objects.requireNonNull(data, "InputData cannot be null");
		Objects.requireNonNull(eventTarget, "EventTarget cannot be null");

		this.data = data;
		this.eventTarget = eventTarget;
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
		List<DictionaryElement> validWords = dict.getValidWords();

		// Compile regex once for speed
		Pattern pattern = buildPattern();

		String containsLetters = getLettersFromContains();
		String dataLetters = getValidDataLetters(containsLetters);
		String searchLetters = dataLetters + containsLetters + data.getStartsWith() + data.getEndsWith();
		searchLetters = searchLetters.toLowerCase();
		String wildcards = getWildcards();
		Set<ScrabbleWord> words = new TreeSet<>();

		// Progress variables
		double inc = 1.0 / validWords.size();
		double thusFar = 0.0;

		for (DictionaryElement element : validWords) {

			// Fire an event that follows the progress of the task
			Event event = new ProgressEvent(this, eventTarget, ProgressEvent.PROGRESS, thusFar);
			Event.fireEvent(eventTarget, event);

			thusFar += inc;
			String word = element.getWord();
			StringBuilder valueLetters = new StringBuilder();

			// Skip if dictionary word is longer than all possible letters
			if (word.length() > searchLetters.length() + wildcards.length()) {
				continue;
			}

			// Skip if any part of the word doesn't match the pattern
			if (pattern != null && !pattern.matcher(word).find()) {
				continue;
			}
			
			// Skip if this is crossword mode and length the word is not equal to numOfLetters
			if ((data.isCrossword() || data.isWordle())
					&& !data.getNumOfLetters().isBlank()
					&& Integer.parseInt(data.getNumOfLetters()) > 0
					&& word.length() != Integer.parseInt(data.getNumOfLetters())) {
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
				boolean isBingo = word.length() - containsLetters.length() - data.getStartsWith().length()
						- data.getEndsWith().length() >= 7;
				words.add(new ScrabbleWord(word, valueLetters.toString(), isBingo, element.getDefinition()));

			}

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
		String dataLetters;

		// If the game type is Wordle, available letters is treated as "can't have" letters
		if (data.isWordle()) {
			dataLetters = Arrays.stream(ALL_LETTERS.split(""))
					.filter(x -> !data.getLetters().contains(x))
					.collect(Collectors.joining());
		} else {
			dataLetters = data.getLetters();
		}

		dataLetters = removeCapitals(containsLetters, dataLetters);
		dataLetters = removeCapitals(data.getStartsWith(), dataLetters);
		dataLetters = removeCapitals(data.getEndsWith(), dataLetters);
		dataLetters = dataLetters.replaceAll("\\.", "");

		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Valid data letters: \"%s\"", dataLetters));
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
				if (LOG.isDebugEnabled()) {
					LOG.debug(String.format("Built pattern: \"%s\"", patternString));
				}
			} catch (PatternSyntaxException e) {
				String alertText = String.format(
						"Error compiling regex from contains, starts-with, and ends-with%n" +
						"Pattern: \"%s\"", patternString);
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
