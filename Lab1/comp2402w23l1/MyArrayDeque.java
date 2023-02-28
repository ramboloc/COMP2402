package comp2402w23l1;
// Thank you Pat Morin for the basic skeleton of this file.

// You may not import any classes; if you do, the autograder will fail.


/**
 * An implementation of the List interface that allows for fast modifications
 * at both the head and tail.
 * 
 * The implementation is as a circular array.  The List item of rank i is stored
 * at a[(j+i)%a.length].  Insertions and removals at position i take 
 * O(1+min{i, size()-i}) amortized time.
 * @author morin
 *
 * @param <T> the type of objects stored in this list
 * TODO: Implement addAll() and removeAll() efficiently
 */
public class MyArrayDeque<T> implements MyList<T> {

	/**
	 * Array used to store elements. Do not remove this.
	 */
	protected T[] a;
	
	/**
	 * Index of next element to de-queue. Do not remove this.
	 */
	protected int j;
	
	/**
	 * Number of elements in the queue. Do not remove this.
	 */
	protected int n;
	
	/**
	 * Grow the internal array
	 */
	protected void resize() {
		@SuppressWarnings("unchecked")
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
	public MyArrayDeque() {
		a = (T[]) new Object[1];
		j = 0;
		n = 0;
	}
	
	public int size() {
		return n;
	}
	
	public T get(int i) {
		if (i < 0 || i > n-1) throw new IndexOutOfBoundsException();
		return a[(j+i)%a.length];
	}
	
	public T set(int i, T x) {
		if (i < 0 || i > n-1) throw new IndexOutOfBoundsException();
		T y = a[(j+i)%a.length];
		a[(j+i)%a.length] = x;
		return y;
	}
	
	public void add(int i, T x) {
		if (i < 0 || i > n) throw new IndexOutOfBoundsException();
		if (n+1 > a.length) resize();
		if (i < n/2) { // shift a[0],..,a[i-1] left one position
			j = (j == 0) ? a.length - 1 : j - 1; //(j-1)mod a.length
			for (int k = 0; k <= i-1; k++)
				a[(j+k)%a.length] = a[(j+k+1)%a.length];
		} else { // shift a[i],..,a[n-1] right one position
			for (int k = n; k > i; k--)
				a[(j+k)%a.length] = a[(j+k-1)%a.length];
		}
		a[(j+i)%a.length] = x;
		n++;
	}
	
	public T remove(int i) {
		if (i < 0 || i > n - 1)	throw new IndexOutOfBoundsException();
		T x = a[(j+i)%a.length];
		if (i < n/2) {  // shift a[0],..,[i-1] right one position
			for (int k = i; k > 0; k--)
				a[(j+k)%a.length] = a[(j+k-1)%a.length];
			j = (j + 1) % a.length;
		} else { // shift a[i+1],..,a[n-1] left one position
			for (int k = i; k < n-1; k++)
				a[(j+k)%a.length] = a[(j+k+1)%a.length];
		}
		n--;
		if (3*n < a.length) resize();
		return x;
	}
	
	public void clear() {
		n = 0;
		resize();
	}



	// You should not need to modify anything above this line.

	// This is the default behaviour of toString.
	// Override this with more useful behaviour for debugging, if you wish
	public String toString() {
		String s = "[";
		for (int i = 0; i < n - 1; i++) {
			s += a[(j+i)%a.length];
			s += ", ";
		}
		if (n > 0) {
			s += a[(j + n - 1)% a.length];
		}
		s += "]";
		return s;
	}



	public void copy(int k) {
		// TODO(student): Your code goes here.
		if (k < 0) {
			throw new ArrayStoreException("Invalid k value. k should not be negative number");
		}
		if (k == 0) {
			a = (T[]) new Object[1];
			n = 0;
			j = 0;
			return;
		}

		T[] oldArr = deepCopy(a);
		T[] newCopyedArr = (T[]) new Object[a.length * k];
		int newCopyedArrIdx = 0;
		for (int i = 0; i < n; i++) {
			for (int counter = 0; counter < k; counter++) {
				newCopyedArr[newCopyedArrIdx++] = oldArr[(j + i) % oldArr.length];
			}
		}

		a = newCopyedArr;
		n *= k;
		j = 0;
	}

	private T[] deepCopy(T[] inputArr) {
		T[] outputArr = (T[]) new Object[inputArr.length];
		for (int i = 0; i < inputArr.length; i++) {
			outputArr[i] = inputArr[i];
		}

		return outputArr;
	}


	// This main method is provided for you for testing purposes.
	// You will want to add to this for local testing.
	public static void main(String[] args) {
		// These tests are not at all sufficient. They are just examples.
		testCopy(5, 2);
		testCopy( 0, 2);
		testCopy(10, 0);
		testCopy(5, 1);
		testCopy(4, 4);
	}


	// Creates a MyArrayDeque of n elements, then calls
	// copy on that list.
	public static void testCopy(int n, int k) {
		System.out.println("Test copy("+k+") ------");
		MyList<Integer> mad = new MyArrayDeque<Integer>();

		// Create a MyDeque with n elements 0, 1, 2, ..., n-1.
		for (int i = 0; i < n; i++) {
			mad.add(i, mad.size());
		}
		System.out.println(mad);

		// Call copy on it. Print out the results.
		mad.copy(k);
		System.out.println(mad);
		System.out.println("Done Test copy------");
	}

}
