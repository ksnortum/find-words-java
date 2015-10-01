package net.snortum.scrabble_words.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds a word and its Scrabble value. Immutable. Comparable.
 * 
 * @author Knute
 * @version 1.0
 */
public class ScrabbleWord implements Comparable<ScrabbleWord> {
	private static final Map<String, Integer> LETTER_VALUE;
	private static final String WORD_NULL = "Word cannot be null";
	private static final String WORD_EMPTY = "Word cannot be empty";

	private final String word;
	private final int value;

	static {
		LETTER_VALUE = new HashMap<>();
		LETTER_VALUE.put("a", 1);
		LETTER_VALUE.put("b", 3);
		LETTER_VALUE.put("c", 3);
		LETTER_VALUE.put("d", 2);
		LETTER_VALUE.put("e", 1);
		LETTER_VALUE.put("f", 4);
		LETTER_VALUE.put("g", 2);
		LETTER_VALUE.put("h", 4);
		LETTER_VALUE.put("i", 1);
		LETTER_VALUE.put("j", 8);
		LETTER_VALUE.put("k", 5);
		LETTER_VALUE.put("l", 1);
		LETTER_VALUE.put("m", 3);
		LETTER_VALUE.put("n", 1);
		LETTER_VALUE.put("o", 1);
		LETTER_VALUE.put("p", 3);
		LETTER_VALUE.put("q", 10);
		LETTER_VALUE.put("r", 1);
		LETTER_VALUE.put("s", 1);
		LETTER_VALUE.put("t", 1);
		LETTER_VALUE.put("u", 1);
		LETTER_VALUE.put("v", 4);
		LETTER_VALUE.put("w", 4);
		LETTER_VALUE.put("x", 8);
		LETTER_VALUE.put("y", 4);
		LETTER_VALUE.put("z", 10);
	}

	/**
	 * Create a Scrabble word and calculate its value
	 * 
	 * @param word
	 *            the word formed by the Scrabble tiles
	 * @throws IllegalArgumentException
	 *             if word is null or empty
	 */
	public ScrabbleWord(String word) {
		if (word == null) {
			throw new IllegalArgumentException(WORD_NULL);
		}
		if (word.isEmpty()) {
			throw new IllegalArgumentException(WORD_EMPTY);
		}

		this.word = word.toLowerCase();
		this.value = calculateValue();
	}

	/**
	 * Create a Scrabble word from an existing word, optionally adding 50 points
	 * for a bingo
	 * 
	 * @param word
	 *            the word formed by the Scrabble tiles
	 * @param isBingo
	 *            true if this word created a bingo
	 * @throws IllegalArgumentException
	 *             if word is null or empty
	 */
	public ScrabbleWord(String word, boolean isBingo) {
		if (word == null) {
			throw new IllegalArgumentException(WORD_NULL);
		}
		if (word.isEmpty()) {
			throw new IllegalArgumentException(WORD_EMPTY);
		}

		this.word = word.toLowerCase();
		this.value = calculateValue() + (isBingo ? 50 : 0);
	}

	/*
	 * Calculate value as total letter values from tiles
	 */
	private int calculateValue() {
		return Arrays.stream(word.split(""))
				.mapToInt(letter -> LETTER_VALUE.get(letter))
				.sum();
	}

	// Values can only be set in constructors

	public String getWord() {
		return word;
	}

	public int getValue() {
		return value;
	}

	/**
	 * Descending value order, ascending word order
	 * 
	 * @param that
	 *            the other Scrabble word
	 * @return -1, 0, or 1
	 */
	@Override
	public int compareTo(ScrabbleWord that) {
		if (this.value > that.value) {
			return -1;
		} else if (this.value < that.value) {
			return 1;
		} else {
			return this.word.compareTo(that.word);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScrabbleWord other = (ScrabbleWord) obj;
		if (value != other.value)
			return false;
		if (!word.equals(other.word))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		result = prime * result + word.hashCode();

		return result;
	}

	@Override
	public String toString() {
		return this.word + ": " + this.value;
	}

}
