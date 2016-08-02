package net.snortum.scrabble_words.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ValidatorTest {

	@Test
	public void testValidateNoDot() {
		InputData data = new InputData.Builder( "abc" ).build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 0 );
	}
	
	@Test
	public void testValidateOneDot() {
		InputData data = new InputData.Builder( "abc." ).build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 0 );
	}
	
	@Test
	public void testValidatorOneDotOnlyError() {
		InputData data = new InputData.Builder( "abc.." ).build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.ONLY_ONE_DOT );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullDataValidate() {
		new Validator( null );
	}

	@Test
	public void testValidatorTooFewLettersError() {
		InputData data = new InputData.Builder( "" ).build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.TOO_FEW_LETTERS );
	}
	
	@Test
	public void testValidatorNotLettersOrDotError() {
		InputData data = new InputData.Builder( "abc5e" ).build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.LETTERS_OR_DOT );
	}
	
	@Test
	public void testValidatorContainsError() {
		InputData data = new InputData.Builder( "abc" )
				.contains( "abcdefghijklmnopqrstuvwxyz" )
				.build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.CONTAINS_TOO_LONG );
	}
	
	@Test
	public void testValidatorStartsWithNonletters() {
		InputData data = new InputData.Builder( "abc" )
				.startsWith( "a5" )
				.build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.STARTSWITH_NONLETTERS );
	}
	
	@Test
	public void testValidatorEndsWithNonletters() {
		InputData data = new InputData.Builder( "abc" )
				.endsWith( "a5" )
				.build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.ENDSWITH_NONLETTERS );
	}
}
