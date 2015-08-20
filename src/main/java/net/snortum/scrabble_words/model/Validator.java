package net.snortum.scrabble_words.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

public class Validator {
	private static final String INVALID_REGEX = "The regex is invalid";
	private static final String BOTH_CONTAINS_LETTERS_AND_REGEX = "Cannot have both Contains Letters and Contains Regex";
	static final String TOO_FEW_LETTERS = "You must have at least three letters";
	static final String CONTAINS_TOO_LONG = "Contains cannot have more that five letters";
	private static final Logger LOG = Logger.getLogger(Validator.class);

	private InputData data;
	private String reError = "";

	/**
	 * Create a Validator object
	 * 
	 * @param data
	 *            the {@link InputData} to validate
	 */
	public Validator(InputData data) {
		this.data = data;
	}

	/**
	 * Validate the {@link InputData}
	 * 
	 * @return a String array of error messages. Will contain one empty message
	 *         if no errors
	 */
	public List<String> validate() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Validating...");
		}
		StringBuilder errors = new StringBuilder();
		if (data.getLetters().length() < 3) {
			addErrorMessage(errors, TOO_FEW_LETTERS);
		}
		if (!data.getContains().isEmpty() && !data.getContainsRe().isEmpty()) {
			addErrorMessage(errors, BOTH_CONTAINS_LETTERS_AND_REGEX);
		}
		if (data.getContains().length() > 5) {
			addErrorMessage(errors, CONTAINS_TOO_LONG);
		}
		if (!data.getContainsRe().isEmpty()
				&& !regexIsValid(data.getContainsRe())) {
			addErrorMessage(errors, INVALID_REGEX);
			addErrorMessage(errors, reError);
		}

		List<String> errorList = new ArrayList<>();
		if (!errors.toString().isEmpty()) {
			errorList = Arrays.asList(errors.toString().split("\n"));
		}

		return errorList;
	}

	/*
	 * Append an error message with a new line
	 */
	private void addErrorMessage(StringBuilder errors, String message) {
		if (!errors.toString().isEmpty()) {
			errors.append("\n");
		}
		errors.append(message);
	}
	
	/*
	 * Validate a regex: will it compile?
	 */
	private boolean regexIsValid(String regex) {
		try {
			Pattern.compile(regex);
		} catch (PatternSyntaxException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(e.getDescription());
			}
			reError = e.getDescription();
			return false;
		}
		
		return true;
	}

}
