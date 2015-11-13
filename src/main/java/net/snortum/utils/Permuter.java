package net.snortum.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Provides all permutations of a string
 * 
 * @author Knute Snortum
 * @version 1.0
 */
public class Permuter {

	/** The string to permute */
	private String str;
	private Map<String, String> permsMap;

	/**
	 * Create a permuter
	 * 
	 * @param str
	 *            the string to permute
	 */
	public Permuter(String str) {
		this.str = str;
	}

	/**
	 * Returns a list of all the permutations of a string. String is entered
	 * during object creation. If a character appears more than once in the
	 * string the permutations will have duplicates. Use
	 * {@link #permuteUnique()} to avoid this.
	 * 
	 * @return a list of all permutations of a string
	 */
	public List<String> permute() {
		List<String> perms = new ArrayList<>();
		permute("", str, perms);
		return perms;
	}

	/**
	 * Returns an ordered set of all the permutations of a string. String is
	 * entered during object creation.
	 * 
	 * @return a set of all permutations of a string
	 */
	public Set<String> permuteUnique() {
		Set<String> perms = new TreeSet<>();
		permute("", str, perms);
		return perms;
	}

	/**
	 * Is lookup a permutation of {@link #str}?
	 * 
	 * @param lookup
	 *            the string to lookup
	 * @return true if lookup is a permutation of {@link #str}
	 */
	public boolean isPermutation(String lookup) {
		if (permsMap == null) {
			permuteLookup();
		}
		return permsMap.containsKey(lookup);
	}

	/**
	 * Creates a map of permutations for use with {@link #isPermutation}
	 */
	public void permuteLookup() {
		permsMap = new HashMap<>();
		permuteMap("", str);
	}

	/*
	 * Does the actual work of permuting to a list. Adds to perms as a side
	 * effect
	 */
	private void permute(String prefix, String str,
			Collection<String> perms) {
		int n = str.length();
		if (n == 0) {
			perms.add(prefix);
		} else {
			for (int i = 0; i < n; i++) {
				permute(prefix + str.charAt(i),
						str.substring(0, i) + str.substring(i + 1, n), perms);
			}
		}
	}

	/*
	 * Does the actual work of permuting to a map. Adds to permsMap as a side
	 * effect
	 */
	private void permuteMap(String prefix, String str) {
		int n = str.length();
		if (n == 0) {
			permsMap.put(prefix, "");
		} else {
			for (int i = 0; i < n; i++) {
				permuteMap(prefix + str.charAt(i),
						str.substring(0, i) + str.substring(i + 1, n));
			}
		}
	}
}
