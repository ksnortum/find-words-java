package net.snortum.scrabble_words.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

/**
 * Validate {@link InputData}
 * 
 * @author Knute
 * @version 0.2
 */
public class Validator {
	private static final String INVALID_REGEX = "The regex is invalid";
	private static final String BOTH_CONTAINS_LETTERS_AND_REGEX = 
			"Cannot have both Contains Letters and Contains Regex";
	static final String TOO_FEW_LETTERS = "You must have at least three letters";
	static final String CONTAINS_TOO_LONG = "Contains cannot have more that five letters";
	static final String LETTERS_OR_DOT = "Letters can only be \"a\" thru \"z\" and one dot";
	static final String CONTAINS_NONLETTERS = "Contains must only be letters (a-z)";
	static final String STARTSWITH_NONLETTERS = "StartsWith must only be letters (a-z)";
	static final String ENDSWITH_NONLETTERS = "EndsWith must only be letters (a-z)";
	private static final Logger LOG = Logger.getLogger(Validator.class);
	private static final String LETTERS_DOT_RE = "[a-z.]*";
	private static final String LETTERS_RE = "[a-z]*";

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
			LOG.debug("Validating");
		}
		List<String> errors = new ArrayList<>();
		if (data.getLetters().length() < 3) {
			errors.add(TOO_FEW_LETTERS);
		}
		if (!data.getLetters().matches(LETTERS_DOT_RE)) {
			errors.add(LETTERS_OR_DOT);
		}
		if (!data.getContains().isEmpty() && !data.getContainsRe().isEmpty()) {
			errors.add(BOTH_CONTAINS_LETTERS_AND_REGEX);
		}
		if (data.getContains().length() > 5) {
			errors.add(CONTAINS_TOO_LONG);
		}
		if (!data.getContains().matches(LETTERS_RE)) {
			errors.add(CONTAINS_NONLETTERS);
		}
		if (!data.getStartsWith().matches(LETTERS_RE)) {
			errors.add(STARTSWITH_NONLETTERS);
		}
		if (!data.getEndsWith().matches(LETTERS_RE)) {
			errors.add(ENDSWITH_NONLETTERS);
		}
		if (!data.getContainsRe().isEmpty()
				&& !regexIsValid(data.getContainsRe())) {
			errors.add(INVALID_REGEX);
			errors.add(reError);
		}

		return errors;
	}
	
	/*
	 * Validate a regex: will it compile?
	 */
	private boolean regexIsValid(String regex) {
		try {
			Pattern.compile(regex);
		} catch (PatternSyntaxException e) {
			reError = e.getDescription();
			return false;
		}
		
		return true;
	}

}
