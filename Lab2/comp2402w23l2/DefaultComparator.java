/**
 * 
 */
package comp2402w23l2;

// YOU SHOULD NOT MODIFY THIS FILE AT ALL.
// (This version will be used by the autograder; not yours!)


import java.util.Comparator;

class DefaultComparator<T> implements Comparator<T> {
	@SuppressWarnings("unchecked")
	public int compare(T a, T b) {
		return ((Comparable<T>)a).compareTo(b);
	}
}