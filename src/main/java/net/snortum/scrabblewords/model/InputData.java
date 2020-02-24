package net.snortum.scrabblewords.model;

/**
 * This class immutable holds to data inputed by the user from the form. It uses
 * an internal {@link Builder} class to create the object. For instance:
 * 
 * <pre>
 * InputData data = new InputData.Builder("abc")
 * 		.contains("d")
 * 		.dictionaryName(DictionaryName.twl)
 * 		.build();
 * </pre>
 * 
 * @author Knute Snortum
 * @version 2020.02.24
 */
public class InputData {

	/**
	 * Build an {@link InputData} object
	 */
	public static class Builder {

		// Mandatory
		private String letters;

		// Optional, set default values
		private String contains = "";
		private String startsWith = "";
		private String endsWith = "";
		private DictionaryName dictName = DictionaryName.twl;

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
		 *            defaults to "twl".
		 * @return this object
		 */
		public Builder dictionaryName(DictionaryName dictName) {
			this.dictName = dictName;
			return this;
		}

		/**
		 * @return an {@link InputData} object built from {@link Builder}
		 *         options.
		 */
		public InputData build() {
			return new InputData(this);
		}
	}
	
	private Builder builder;

	// Create InputData object from builder
	private InputData(Builder builder) {
		this.builder = builder;
	}

	/**
	 * Create a copy of the passed in object
	 * 
	 * @param data
	 *            the object to be copied
	 * @throws IllegalArgumentException
	 *             if data is null
	 */
	public InputData(InputData data) {
		if (data == null) {
			throw new IllegalArgumentException("Data cannot be null");
		}

		builder = data.getBuilder();
	}

	// Getters. For Setters, use Builder
	
	private Builder getBuilder() {
		return builder;
	}

	/**
	 * @return the letters of your "tiles"
	 */
	public String getLetters() {
		return getBuilder().letters;
	}

	/**
	 * @return the letters the suggested words must contain
	 */
	public String getContains() {
		return getBuilder().contains;
	}

	/**
	 * @return the letters the suggested word must start with
	 */
	public String getStartsWith() {
		return getBuilder().startsWith;
	}

	/**
	 * @return the letters the suggested word must end with
	 */
	public String getEndsWith() {
		return getBuilder().endsWith;
	}

	/**
	 * @return the {@link DictionaryName} used to check valid words
	 */
	public DictionaryName getDictionaryName() {
		return getBuilder().dictName;
	}

	/**
	 * @return true if all text fields are empty, otherwise return false
	 */
	public boolean isEmpty() {
		return getBuilder().letters.isEmpty() &&
				getBuilder().contains.isEmpty() &&
				getBuilder().startsWith.isEmpty() &&
				getBuilder().endsWith.isEmpty();
	}
}
