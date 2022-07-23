package net.snortum.scrabblewords.model;

/**
 * This class immutable holds to data inputed by the user from the form. It uses
 * an internal {@link Builder} class to create the object. For instance:
 * 
 * <pre>
 * InputData data = new InputData.Builder("abc")
 * 		.contains("d")
 * 		.dictionaryName(DictionaryName.TWL)
 * 		.build();
 * </pre>
 * 
 * @author Knute Snortum
 * @version 2.8.0
 */
public class InputData {

	/**
	 * Build an {@link InputData} object
	 */
	public static class Builder {

		// Mandatory
		private final String letters;

		// Optional, set default values
		private String contains = "";
		private String startsWith = "";
		private String endsWith = "";
		private DictionaryName dictName = DictionaryName.OSPD;
		private String numOfLetters = "";
		private TypeOfGame gameType = TypeOfGame.SCRABBLE;

		/**
		 * Create a Builder object to build an {@link InputData} object.
		 * 
		 * @param letters
		 *            the letters of the "tiles" you have
		 */
		public Builder(String letters) {
			this.letters = letters.toLowerCase();
		}

		/**
		 * @param contains
		 *            the letters the suggested words must contain
		 * @return this object
		 */
		public Builder contains(String contains) {
			this.contains = contains;
			return this;
		}

		/**
		 * @param startsWith
		 *            the letters the suggested word must start with
		 * @return this object
		 */
		public Builder startsWith(String startsWith) {
			this.startsWith = startsWith;
			return this;
		}

		/**
		 * @param endsWith
		 *            the letters the suggested word must end with
		 * @return this object
		 */
		public Builder endsWith(String endsWith) {
			this.endsWith = endsWith;
			return this;
		}

		/**
		 * @param dictName
		 *            the {@link DictionaryName} to search for valid words;
		 *            defaults to "ospd".
		 * @return this object
		 */
		public Builder dictionaryName(DictionaryName dictName) {
			this.dictName = dictName;
			return this;
		}
		
		/**
		 * In crossword mode, this contains the number of letters long
		 * the word must be
		 * 
		 * @param numOfLetters
		 * 				the number of letters
		 * @return this object
		 */
		public Builder numOfLetters(String numOfLetters) {
			this.numOfLetters = numOfLetters;
			return this;
		}

		/**
		 * @param gameType the type of game this program is finding words for
		 * @return this object
		 */
		public Builder gameType(TypeOfGame gameType) {
			this.gameType = gameType;
			return this;
		}

		/** @return an {@link InputData} object built from {@link Builder} options. */
		public InputData build() {
			return new InputData(this);
		}
	}
	
	private final Builder builder;

	// Create InputData object from builder
	private InputData(Builder builder) {
		this.builder = builder;
	}

	// Getters. For Setters, use Builder
	
	private Builder getBuilder() {
		return builder;
	}

	/** @return the letters of your "tiles" */
	public String getLetters() {
		return getBuilder().letters;
	}

	/** @return the letters the suggested words must contain */
	public String getContains() {
		return getBuilder().contains;
	}

	/** @return the letters the suggested word must start with */
	public String getStartsWith() {
		return getBuilder().startsWith;
	}

	/** @return the letters the suggested word must end with */
	public String getEndsWith() {
		return getBuilder().endsWith;
	}

	/** @return the {@link DictionaryName} used to check valid words */
	public DictionaryName getDictionaryName() {
		return getBuilder().dictName;
	}
	
	/** @return the number of letters the word is supposed to have */
	public String getNumOfLetters() {
		return getBuilder().numOfLetters;
	}

	/** @return the type of game this program is finding words for */
	public TypeOfGame getGameType() {
		return getBuilder().gameType;
	}

	/** @return {@code true} if this game is Scrabble */
	public boolean isScrabble() {
		return getBuilder().gameType == TypeOfGame.SCRABBLE;
	}

	/** @return {@code true} if this game is a crossword */
	public boolean isCrossword() {
		return getBuilder().gameType == TypeOfGame.CROSSWORD;
	}

	/** @return {@code true} if this game is Wordle */
	public boolean isWordle() {
		return getBuilder().gameType == TypeOfGame.WORDLE;
	}

	/** @return {@code true} if all text fields are empty, otherwise return {@code false} */
	public boolean isEmpty() {
		return getBuilder().letters.isEmpty() &&
				getBuilder().contains.isEmpty() &&
				getBuilder().startsWith.isEmpty() &&
				getBuilder().endsWith.isEmpty() &&
				getBuilder().numOfLetters.isEmpty();
	}
}
