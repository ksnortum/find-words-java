package net.snortum.scrabble_words.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

public class Validator {
	private static final Logger LOG = Logger.getLogger( Validator.class );
	static final String TOO_FEW_LETTERS =
			"You must have at least three letters";
	static final String CONTAINS_TOO_LONG =
			"Contains cannot have more that five letters";

	private InputData data;

	/**
	 * Create a Validator object
	 * 
	 * @param data
	 *            the {@link InputData} to validate
	 */
	public Validator( InputData data ) {
		this.data = data;
	}

	/**
	 * Validate the {@link InputData}
	 * 
	 * @return a String array of error messages. Will contain one empty message
	 *         if no errors
	 */
	public List<String> validate() {
		if ( LOG.isDebugEnabled() ) {
			LOG.debug( "Validating..." );
		}
		StringBuilder errors = new StringBuilder();
		if ( data.getLetters().length() < 3 ) {
			addErrorMessage( errors, TOO_FEW_LETTERS );
		}
		if ( data.getContains().length() > 5 ) {
			addErrorMessage( errors, CONTAINS_TOO_LONG );
		}
		
		List<String> errorList = new ArrayList<>();
		if ( !errors.toString().isEmpty() ) {
			errorList = Arrays.asList( errors.toString().split( "\n" ) );
		}

		return errorList;
	}

	/*
	 * Append an error message with a new line
	 */
	private void addErrorMessage( StringBuilder errors, String message ) {
		if ( !errors.toString().isEmpty() ) {
			errors.append( "\n" );
		}
		errors.append( message );
	}

}
