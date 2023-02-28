package comp2402w23l1;
// Thank you Pat Morin for the basic skeleton of this file.

// YOU SHOULD NOT MODIFY THIS FILE AT ALL.
// (This version will be used by the autograder; not yours!)

/**
 * The MyList<T> interface is a simple interface that allows a class to implement
 * all the functionality of the (more complicated) List<T> interface. 
 *
 * @author sharp
 *
 * @param <T>
 * @see List<T>
 */
public interface MyList<T> {

	/**
	 * @return the number of elements in this MyList
	 */
	public int size();

	/**
	 * Get the element at index i of the MyList
	 *
	 * @param i
	 * @return the element at index i in this MyList
	 */
	public T get(int i);

	/**
	 * Set the element at index i to x to the MyList
	 *
	 * @param i
	 * @param x
	 * @return the value at index i prior to the call
	 */
	public T set(int i, T x);

	/**
	 * Add the element x at index i of the MyList
	 *
	 * @param i
	 * @param x
	 */
	public void add(int i, T x);

	/**
	 * Remove the element x from the MyList
	 *
	 * @param i
	 * @return the element x removed from position i
	 */
	public T remove(int i);

	/**
	 * Replace each element x of MyList with k copies of x.
	 *
	 * @param k
	 */
	public void copy(int k);


	/**
	 * Clear the MyList, removing all elements from the set
	 */
	public void clear();

}
