package net.snortum.scrabble_words.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import javafx.scene.control.ProgressBar;
import net.snortum.scrabble_words.view.Progressor;
import net.snortum.utils.Permutator;
import net.snortum.utils.Sublister;

/**
 * Create all possible permutations of the "tiles" (letters) and validate them
 * against the entered dictionary. Do other tests.
 * 
 * @author Knute
 * @version 1.1
 */
public class WordSearcher {
	private static final Logger LOG = Logger.getLogger(WordSearcher.class);
	private static final int FINISHED = 1;
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

	public WordSearcher(InputData data, ProgressBar bar) {
		this.data = data;
		this.progress = new Progressor(bar);
	}

	/**
	 * Get a list of words from the selected dictionary that match the
	 * requirements of the {@link InputData}.
	 * 
	 * @return a sorted Set where the words that are found can be added to it
	 */
	public Set<ScrabbleWord> getWords() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("In getWords()");
		}

		// Get valid words from dictionary
		Dictionary dict = new Dictionary(data);
		Map<String, String> validWords = dict.getValidWords();

		// Compile regex once for speed
		Pattern pattern = null;
		if (!data.getContainsRe().isEmpty()) {
			pattern = Pattern.compile(data.getContainsRe());
		}

		Set<ScrabbleWord> words = new TreeSet<>();

		// Do letters have a wild card?
		if (data.getLetters().indexOf(".") == -1) {
			String letters = data.getLetters() + data.getContains();
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
				String allLetters = letter + dataLetters + data.getContains();
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
			if (thisList.size() >= 2) {

				// Ready to permutate
				String str = String.join("", thisList);
				Permutator permutator = new Permutator(str);

				// Loop thru permutations
				for (String word : permutator.permutate()) {

					// Must be in the dictionary
					if (!validWords.containsKey(word)) {
						continue;
					}

					// Must contain these letters
					if (!checkContains(word)) {
						continue;
					}

					// Must match this pattern
					if (pattern != null && !pattern.matcher(word).find()) {
						continue;
					}

					words.add(new ScrabbleWord(word));
				}
			}
		}
		progress.showProgress(FINISHED);

		return words;
	}

	/**
	 * Does this word contain all the letters from the {@link InputData}?
	 * 
	 * @param word
	 *            the word to check
	 * @return true: there are no "contains" letters, or word contains all
	 *         letters false: word does not contain all letters
	 */
	private boolean checkContains(String word) {
		boolean addIt = true;

		// No containing letters always passes
		if (!data.getContains().isEmpty()) {
			addIt = false;

			// Check all containing letters
			for (String containsLetter : data.getContains().split("")) {
				if (word.indexOf(containsLetter) >= 0) {
					addIt = true;
					break;
				}
			}
		}

		return addIt;
	}

}
