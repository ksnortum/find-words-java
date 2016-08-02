package net.snortum.scrabblewords.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import javafx.scene.control.ProgressBar;
import net.snortum.scrabblewords.view.Progressor;
import net.snortum.utils.Permuter;
import net.snortum.utils.Sublister;

/**
 * This immutable class will create all possible permutations of the "tiles"
 * (letters) and validate them against the entered dictionary
 * 
 * @author Knute Snortum
 * @version 2016.05.12
 */
public class WordSearcher {
	private static final Logger LOG = Logger.getLogger(WordSearcher.class);
	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

	private final InputData data;
	private final Progressor progress;

	/**
	 * Create a WordSearcher object
	 * 
	 * @param data
	 *            the {@link InputData} to use
	 */
	public WordSearcher(InputData data) {
		this.data = data;
		this.progress = new Progressor();
	}

	/**
	 * Create a WordSearcher object, passing in a progress bar to update as work
	 * is done
	 * 
	 * @param data
	 *            the {@link InputData} to use
	 * @param bar
	 *            the {@link ProgressBar} to update
	 */
	public WordSearcher(InputData data, ProgressBar bar) {
		this.data = data;
		this.progress = new Progressor(bar);
	}

	/**
	 * Get a list of words from the selected dictionary that match the
	 * requirements of the {@link InputData}.
	 * 
	 * @return a sorted Set containing the words that are found
	 */
	public Set<ScrabbleWord> getWords() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("In getWords()");
		}

		// Get valid words from dictionary
		ScrabbleDictionary dict = new ScrabbleDictionary(data);
		Map<String, String> validWords = dict.getValidWords();

		// Compile regex once for speed
		Pattern pattern = null;
		if (!data.getContains().isEmpty()) {
			pattern = Pattern.compile(data.getContains());
		}

		Set<ScrabbleWord> words = new TreeSet<>();

		// Do letters have a wild card?
		if (data.getLetters().indexOf(".") == -1) {
			String letters = data.getLetters() + getLettersFromContains(); 
			List<String> lettersAsList = Arrays.asList(letters.split(""));
			Sublister<String> sublister = new Sublister<String>(lettersAsList);
			Set<List<String>> sublists = sublister.sublist();
			if (LOG.isDebugEnabled()) {
				LOG.debug("Starting permutations");
			}
			words = permutateSublists(validWords, pattern, sublists, words);
		} else {
			String dataLetters = data.getLetters().replace(".", "");

			// Wild card processing
			for (String letter : ALPHABET.split("")) {
				String allLetters = letter + dataLetters + getLettersFromContains(); 
				List<String> lettersAsList = Arrays
						.asList(allLetters.split(""));
				Sublister<String> sublister = new Sublister<String>(
						lettersAsList);
				Set<List<String>> sublists = sublister.sublist();
				if (LOG.isDebugEnabled()) {
					LOG.debug("Starting permutations for letter (" + letter
							+ ")");
				}
				words = permutateSublists(validWords, pattern, sublists, words);
			}
		}

		return words;
	}

	/**
	 * Permutate a sublist of letters, checking for valid words.
	 * 
	 * @param validWords
	 *            a list of dictionary words
	 * @param pattern
	 *            an optional pattern to find in the permutations
	 * @param sublists
	 *            a set of sublists of possible letters to permutate over
	 * @param words
	 *            a set of valid {@link ScrabbleWord}s to add to
	 * @return a set of valid {@link ScrabbleWord}s
	 */
	private Set<ScrabbleWord> permutateSublists(Map<String, String> validWords,
			Pattern pattern, Set<List<String>> sublists,
			Set<ScrabbleWord> words) {

		// Setup for progress bar
		double inc = 1 / (double) sublists.size();
		double thusFar = 0.0;

		// Loop thru sublists
		for (List<String> thisList : sublists) {
			progress.showProgress(thusFar);
			thusFar += inc;

			// No one-letter words
			if (thisList.size() + data.getStartsWith().length() 
					+ data.getEndsWith().length() >= 2) {

				// Ready to permutate
				String str = String.join("", thisList);
				Permuter permuter = new Permuter(str);

				// Loop thru permutations
				for (String word : permuter.permuteUnique()) {
					word = data.getStartsWith() + word + data.getEndsWith();

					if (validWords.containsKey(word) && (pattern == null
							|| pattern.matcher(word).find())) {

						// Is this a bingo?
						boolean isBingo = word.length()
								- getLettersFromContains().length() >= 7; 

						words.add(new ScrabbleWord(word, isBingo));
					}
				}
			}
		}
		progress.showProgress(Progressor.FINISHED);

		return words;
	}

	/*
	 * Return only letters. Since a regex can contain escaped characters, return
	 * only non-escaped
	 */
	private String getLettersFromContains() {
		StringBuilder letters = new StringBuilder();
		boolean isEscapeCharacter = false;
		
		for (String letter : data.getContains().split("")) {
			if ( letter.matches("[a-z]") && !isEscapeCharacter ) {
				letters.append(letter);
			}
			if ( "\\".equals(letter) ) {
				isEscapeCharacter = true;
			} else {
				isEscapeCharacter = false;
			}
		}
		
		return letters.toString();
	}
}
