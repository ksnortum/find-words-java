package net.snortum.scrabblewords.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds a word and its Scrabble value. Immutable. Comparable.
 * 
 * @author Knute Snortum
 * @version 2.6.0
 */
public class ScrabbleWord implements Comparable<ScrabbleWord> {
	private static final Map<String, Integer> LETTER_VALUE;
	private static final String WORD_NULL = "Word cannot be null";
	private static final String WORD_EMPTY = "Word cannot be empty";
	private static final String VALUE_WORD_NULL = "Value word cannot be null";

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

	private final String word;
	private final String valueWord;
	private final int value;
	private final String definition;

	/**
	 * Create a Scrabble word and definition, optionally adding 50 points for a bingo
	 * 
	 * @param word       the word formed by the Scrabble tiles for display
	 * @param valueWord  the word that carries the value of the ScrabbleWord, that
	 *                   is, it has no wildcards (which have zero value)
	 * @param isBingo    true if this word created a bingo
	 * @param definition the definition of the word
	 * @throws IllegalArgumentException if word is null or empty
	 */
	public ScrabbleWord(String word, String valueWord, boolean isBingo, String definition) {
		if (word == null) {
			throw new IllegalArgumentException(WORD_NULL);
		}

		if (word.isEmpty()) {
			throw new IllegalArgumentException(WORD_EMPTY);
		}

		if (valueWord == null) {
			throw new IllegalArgumentException(VALUE_WORD_NULL);
		}

		this.word = word.toLowerCase();
		this.valueWord = valueWord.toLowerCase();
		this.value = calculateValue() + (isBingo ? 50 : 0);
		this.definition = definition;
	}

	/**
	 * Create a Scrabble word without a definition
	 */
	public ScrabbleWord(String word, String valueWord, boolean isBingo) {
		this(word, valueWord, isBingo, null);
	}

	/*
	 * Calculate value as total letter values from tiles, without wildcards
	 */
	private int calculateValue() {
		int total = 0;

		if (!valueWord.isEmpty()) {
			total = Arrays.stream(valueWord.split(""))
					.mapToInt(LETTER_VALUE::get)
					.sum();
		}

		return total;
	}

	// Values can only be set in constructors

	public String getWord() {
		return word;
	}

	public String getValueWord() {
		return valueWord;
	}

	public int getValue() {
		return value;
	}

	public String getDefinition() {
		return definition;
	}

	/**
	 * Descending value order, ascending word order
	 * 
	 * @param that the other Scrabble word
	 * @return -1, 0, or 1
	 */
	@Override
	public int compareTo(ScrabbleWord that) {
		if (this.value > that.getValue()) {
			return -1;
		} else if (this.value < that.getValue()) {
			return 1;
		} else {
			return this.word.compareTo(that.getWord());
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
		if (value != other.getValue())
			return false;
		if (!word.equals(other.getWord()))
			return false;
		if (!valueWord.equals(other.getValueWord()))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		result = prime * result + word.hashCode();
		result = prime * result + valueWord.hashCode();

		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.word);
		sb.append(": ");
		sb.append(this.value);

		if (definition != null && !definition.isEmpty()) {
			sb.append(", ");
			sb.append(definition);
		}

		return sb.toString();
	}

}
