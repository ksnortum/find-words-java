package net.snortum.utils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class SublisterTest {
	private List<Character> list;
	private Set<List<Character>> lists;

	@Before
	public void beforeTest() {
		list = Arrays.asList( 'a', 'b', 'c' );
		lists = new HashSet<>();
		lists.add( new ArrayList<>() ); // []
		lists.add( Arrays.asList( 'a' ) );
		lists.add( Arrays.asList( 'b' ) );
		lists.add( Arrays.asList( 'c' ) );
		lists.add( Arrays.asList( 'a', 'b' ) );
		lists.add( Arrays.asList( 'b', 'c' ) );
		lists.add( Arrays.asList( 'a', 'c' ) );
		lists.add( Arrays.asList( 'a', 'b', 'c' ) );
	}

	@Test
	public void testSublister() {
		Sublister<Character> lister = new Sublister<>( list );
		Set<List<Character>> allSublists = lister.sublist();
		assertTrue( allSublists.size() == lists.size() );

		for ( List<Character> thisList : lists ) {
			assertTrue( allSublists.remove( thisList ) );
		}

		assertTrue( allSublists.isEmpty() );
	}

}
