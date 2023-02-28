package comp2402w23l2;

// YOU SHOULD NOT MODIFY THIS FILE AT ALL.
// (This version will be used by the autograder; not yours!)

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * An implementation of the Queue<T> interface using an array
 * 
 * All operations takes constant amortized time.
 * @author morin
 *
 * @param <T>
 */
public class ArrayQueue<T> extends AbstractQueue<T> {
	
	/**
	 * Array used to store elements
	 */
	protected T[] a;
	
	/**
	 * Index of next element to de-queue
	 */
	protected int j;
	
	/**
	 * Number of elements in the queue
	 */
	protected int n;
	
	/**
	 * Grow the internal array
	 */
	@SuppressWarnings("unchecked")
	protected void resize() {
		T[] b = (T[]) new Object[Math.max(n * 2, 1)];
		for (int k = 0; k < n; k++)
			b[k] = a[(j+k) % a.length];
		a = b;
		j = 0;
	}
	
	/**
	 * Constructor
	 */
	@SuppressWarnings("unchecked")
	public ArrayQueue() {
		a = (T[]) new Object[1];
		j = 0;
		n = 0;
	}
	
	/**
	 * Return an iterator for the elements of the queue. 
	 * This iterator does not support the remove operation
	 */
	public Iterator<T> iterator() {
		class QueueIterator implements Iterator<T> {
			int k;
			
			public QueueIterator() {
				k = 0;
			}

			public boolean hasNext() {
				return (k < n);
			}
			
			public T next() {
				if (k > n) throw new NoSuchElementException();
				T x = a[(j+k) % a.length];
				k++;
				return x;
			}
			
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
		return new QueueIterator();
	}

	public int size() {
		return n;
	}

	public boolean offer(T x) {
		return add(x);
	}

	public boolean add(T x) {
		if (n + 1 > a.length) resize();
		a[(j+n) % a.length] = x;
		n++;
		return true;
	}
	
	public T peek() {
		T x = null;
		if (n > 0) {
			x = a[j];
		}
		return x;
	}

	public T remove() { 
		if (n == 0) throw new NoSuchElementException();
		T x = a[j];
		j = (j + 1) % a.length;
		n--;
		if (a.length >= 3*n) resize();
		return x;
	}

	public T poll() {
		return n == 0 ? null : remove();
	}
	
	public static void main(String args[]) {
		int m = 10000, n = 50;
		ArrayQueue<Integer> q = new ArrayQueue<Integer>();
		for (int i = 0; i < m; i++) {
			q.add(i);
			if (q.size() > n) {
				Integer x = q.remove();
				assert(x == i - n);
			}
		}
		Iterator<Integer> i = q.iterator();
		while (i.hasNext()) {
			System.out.println(i.next());
		}
	}
}
