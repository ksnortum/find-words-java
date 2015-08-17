package net.snortum.utils;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PermutatorTest {
	private List<String> correctPerms;
	
	@Before
	public void beforePermutate() {
		correctPerms = Arrays.asList( "abc", "acb", "bac", "bca", "cab", "cba" );
	}

	@Test
	public void testPermutate() {
		Permutator p = new Permutator( "abc" );
		List<String> perms = p.permutate();
		
		for (String perm : correctPerms) {
			assertTrue( perms.remove( perm ) );
		}
		
		assertTrue( perms.size() == 0 );
	}

}
