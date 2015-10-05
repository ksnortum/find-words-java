package net.snortum.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides all permutations of a string
 * 
 * @author Knute Snortum
 * @version 0.1
 */
public class Permutator {
	private String str;
	private List<String> perms;

	/**
	 * Create a permutator
	 * 
	 * @param str
	 *            the string to permutate
	 */
	public Permutator( String str ) {
		this.str = str;
	}

	/**
	 * Returns a list of all the permutations of a string. String is entered
	 * during object creation.
	 * 
	 * @return a list of all permutations of a string
	 */
	public List<String> permutate() {
		perms = new ArrayList<>();
		permutate( "", str );
		return perms;
	}

	/*
	 * Does the actual work of permutating
	 */
	private void permutate( String prefix, String str ) {
		int n = str.length();
		if ( n == 0 ) {
			perms.add( prefix );
		}
		else {
			for ( int i = 0; i < n; i++ ) {
				permutate( prefix + str.charAt( i ),
						str.substring( 0, i ) + str.substring( i + 1, n ) );
			}
		}
	}

}
