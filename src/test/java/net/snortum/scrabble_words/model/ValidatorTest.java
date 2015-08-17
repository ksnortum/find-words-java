package net.snortum.scrabble_words.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ValidatorTest {

	@Test
	public void testValidate() {
		InputData data = new InputData.Builder( "abc" ).build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 0 );
	}

	@Test
	public void testValidatorLetterError() {
		InputData data = new InputData.Builder( "ab" ).build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.TOO_FEW_LETTERS );
	}
	
	@Test
	public void testValidatorContainsError() {
		InputData data = new InputData.Builder( "abc" )
				.contains( "123456" )
				.build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.CONTAINS_TOO_LONG );
	}

}
