package net.snortum.scrabble_words.model;

public class InputData {
	
	private String letters;
	private String contains;
	private DictionaryName dictName;
	
	/**
	 * Create a InputData object 
	 * 
	 * @author Knute
	 */
	public static class Builder {
		
		// Mandatory
		private String letters;
		
		// Optional, set default values
		private String contains = "";
		private DictionaryName dictName = DictionaryName.twl;
		
		/**
		 * Create a Builder object to build an @{link InputData} object. 
		 * @param letters 
		 * 		the letters of the "tiles" you have
		 * @see build
		 */
		public Builder( String letters ) {
			this.letters = letters.toLowerCase();
		}
		
		/**
		 * @param contains
		 * 		the letters the suggested words must contain
		 * @return this object
		 */
		public Builder contains( String contains ) {
			this.contains = contains.toLowerCase();
			return this;
		}
		
		/**
		 * @param dictName 
		 * 		the {@link DictionaryName} to search for valid words; defaults
		 * 		to "twl".
		 * @return this object
		 */
		public Builder dictionaryName( DictionaryName dictName ) {
			this.dictName = dictName;
			return this;
		}
		
		/**
		 * @return an {@link InputData} object built from {@link Builder}
		 * 		options.  For instance:
		 * 
		 * <pre>
		 * InputData data = new InputData.Builder( "abc" )
		 * 	.contains( "d" )
		 * 	.dictionaryName( DictionaryName.twl )
		 * 	.build(); 
		 * </pre>
		 */
		public InputData build() {
			return new InputData( this );
		}
	}

	// Create InputData object from builder
	private InputData( Builder builder ) {
		this.letters = builder.letters;
		this.contains = builder.contains;
		this.dictName = builder.dictName;
	}
	
	/**
	 * Create a copy of the passed in object
	 * 
	 * @param data
	 * 		the object to be copied
	 * @throws IllegalArgumentException if data is null
	 */
	public  InputData( InputData data ) {
		if ( data == null ) {
			throw new IllegalArgumentException( "Data cannot be null" );
		}
		
		this.letters = data.letters;
		this.contains = data.contains;
		this.dictName = data.dictName;
	}

	// Getters.  For Setters, use Builder
	
	/**
	 * @return the letters of your "tiles"
	 */
	public String getLetters() {
		return letters;
	}
	
	/**
	 * @return the letters the suggested words must contain
	 */
	public String getContains() {
		return contains;
	}
	
	/**
	 * @return the {@link DictionaryName} used to check valid words
	 */
	public DictionaryName getDictionaryName() {
		return dictName;
	}
}
