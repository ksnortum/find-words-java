package net.snortum.scrabblewords.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.snortum.scrabblewords.model.InputData;

/**
 * This immutable class will validate {@link InputData}, returning a list of
 * errors, if any.
 * 
 * @author Knute Snortum
 * @version 2.4.0
 */
public class Validator {
	private static final Logger LOG = LogManager.getLogger(Validator.class);
	private static final String INVALID_REGEX = "The regex is invalid";
	static final String TOO_FEW_LETTERS = "You must have at least one letter";
	static final String CONTAINS_TOO_LONG = "Contains cannot have more that twenty letters";
	static final String LETTERS_OR_DOTS = "Letters can only be \"a\" thru \"z\" and one or two dots";
	static final String NO_MORE_THAN_TWO_DOTS = "Letters can have no more than two dots";
	static final String STARTSWITH_NONLETTERS = "StartsWith must only be letters";
	static final String ENDSWITH_NONLETTERS = "EndsWith must only be letters";
	static final String INVALID_NUMBER = "You must enter only non-negative digits in the number of letters field";
	static final String TOO_MANY_LETTERS = "You cannot have over 20 number of letters";
	static final String TOO_MANY_NUM_OF_LETTERS = "The Number of Letters field can't be more then 20";
	private static final String LETTERS_DOT_RE = "[a-z.]*";
	private static final String LETTERS_RE = "[a-zA-Z]*";

	private final InputData data;
	private String reError = "";

	/**
	 * Create a Validator object
	 * 
	 * @param data
	 *            the {@link InputData} to validate
	 * @throws IllegalArgumentException
	 *             if data is null
	 */
	public Validator(InputData data) {
		if (data == null) {
			throw new IllegalArgumentException("Data cannot be null");
		}
		this.data = data;
	}

	/**
	 * Validate the {@link InputData}
	 * 
	 * @return a List of error messages
	 */
	public List<String> validate() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Validating");
		}
		
		List<String> errors = new ArrayList<>();
		
		if (data.getLetters().length() < 1) {
			errors.add(TOO_FEW_LETTERS);
		} else if (data.getLetters().length() > 20) {
			errors.add(TOO_MANY_LETTERS);
		}
		
		if (!data.getLetters().matches(LETTERS_DOT_RE)) {
			errors.add(LETTERS_OR_DOTS);
		}
		
		if (!data.isCrosswordMode() && !noMoreThanTwoDots(data.getLetters())) {
			errors.add(NO_MORE_THAN_TWO_DOTS);
		}
		
		if (data.getContains().length() > 20) {
			errors.add(CONTAINS_TOO_LONG);
		}
		
		if (!data.getStartsWith().matches(LETTERS_RE)) {
			errors.add(STARTSWITH_NONLETTERS);
		}
		
		if (!data.getEndsWith().matches(LETTERS_RE)) {
			errors.add(ENDSWITH_NONLETTERS);
		}
		
		if (!regexIsValid(data.getContains())) {
			errors.add(INVALID_REGEX);
			errors.add(reError);
		}
		
		if (data.isCrosswordMode() && !data.getNumOfLetters().isBlank()) {
			if (!data.getNumOfLetters().matches("\\d*")) {
				errors.add(INVALID_NUMBER);
			} else if (Integer.parseInt(data.getNumOfLetters()) > 20) {
				errors.add(TOO_MANY_NUM_OF_LETTERS);
			}
		}
		
		return errors;
	}

	/*
	 * False if letters has no more than two dots
	 */
	private boolean noMoreThanTwoDots(String letters) {
		String dots = Arrays.stream(letters.split(""))
				.filter(s -> s.equals("."))
				.collect(Collectors.joining());
		
		return dots.length() <= 2;
	}

	/*
	 * Validate a regex: will it compile?
	 */
	private boolean regexIsValid(String regex) {
		try {
			Pattern.compile(regex);
		} catch (PatternSyntaxException e) {
			reError = e.toString();
			return false;
		}

		return true;
	}

}
