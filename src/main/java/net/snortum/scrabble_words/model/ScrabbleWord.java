package net.snortum.scrabble_words.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds a word and its Scrabble value. Immutable. Comparable.
 * 
 * @author Knute
 *
 */
public class ScrabbleWord implements Comparable<ScrabbleWord> {
	private static final Map<Character, Integer> LETTER_VALUE;
	private final String word;
	private final int value;

	static {
		LETTER_VALUE = new HashMap<>();
		LETTER_VALUE.put('a', 1);
		LETTER_VALUE.put('b', 3);
		LETTER_VALUE.put('c', 3);
		LETTER_VALUE.put('d', 2);
		LETTER_VALUE.put('e', 1);
		LETTER_VALUE.put('f', 4);
		LETTER_VALUE.put('g', 2);
		LETTER_VALUE.put('h', 4);
		LETTER_VALUE.put('i', 1);
		LETTER_VALUE.put('j', 8);
		LETTER_VALUE.put('k', 5);
		LETTER_VALUE.put('l', 1);
		LETTER_VALUE.put('m', 3);
		LETTER_VALUE.put('n', 1);
		LETTER_VALUE.put('o', 1);
		LETTER_VALUE.put('p', 3);
		LETTER_VALUE.put('q', 10);
		LETTER_VALUE.put('r', 1);
		LETTER_VALUE.put('s', 1);
		LETTER_VALUE.put('t', 1);
		LETTER_VALUE.put('u', 1);
		LETTER_VALUE.put('v', 4);
		LETTER_VALUE.put('w', 4);
		LETTER_VALUE.put('x', 8);
		LETTER_VALUE.put('y', 10);
		LETTER_VALUE.put('z', 1);
	}

	/**
	 * Create a Scrabble word and calculate its value
	 * 
	 * @param word
	 *            cannot be null; will be lower cased
	 */
	public ScrabbleWord(String word) {
		if (word == null) {
			throw new NullPointerException();
		}

		this.word = word.toLowerCase();
		this.value = calculateValue(word);
	}

	// Values can only be set in the constructor
	
	public String getWord() {
		return word;
	}

	public int getValue() {
		return value;
	}

	/*
	 * Calculate the word value based on Scrabble tile values
	 */
	private int calculateValue(String word) {
		int total = 0;

		for (char letter : word.toCharArray()) {
			total += LETTER_VALUE.get(letter);
		}

		return total;
	}

	/**
	 * Descending order
	 */
	@Override
	public int compareTo(ScrabbleWord that) {
		return this.value > that.value ? -1 : this.value == that.value ? 0 : 1;
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
