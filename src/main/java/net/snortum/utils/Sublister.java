package net.snortum.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Create an immutable object that can sublist the given list. (Uses sets so
 * there are no duplicates).
 * 
 * @author Knute Snortum
 * @version 0.2
 * 
 * @param <T>
 *            the type of this list
 */
public class Sublister<T> {
	private final List<T> toSublist;

	/**
	 * Create a sublister object
	 * 
	 * @param toSublist
	 *            the list to sublist
	 */
	public Sublister(List<T> toSublist) {
		this.toSublist = toSublist;
	}

	/**
	 * Return a set of all sublists. Note that because this is a set, no
	 * duplicate lists are present.
	 * 
	 * @return a set of all sublists, duplicates excluded
	 */
	public Set<List<T>> sublist() {
		return sublist(toSublist);
	}

	/*
	 * Does the actual work of sublisting
	 */
	private Set<List<T>> sublist(List<T> thisList) {
		Set<List<T>> lists = new HashSet<>();

		if (thisList.isEmpty()) {
			lists.add(new ArrayList<>());
			return lists;
		}

		List<T> listCopy = new ArrayList<>(thisList);
		T head = listCopy.get(0);
		List<T> rest = new ArrayList<>(listCopy.subList(1, listCopy.size()));

		for (List<T> list : sublist(rest)) {
			List<T> newList = new ArrayList<>();
			newList.add(head);
			newList.addAll(list);
			lists.add(newList);
			lists.add(list);
		}

		return lists;
	}
}
