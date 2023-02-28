package comp2402w23l1;
// Thank you Pat Morin for the basic skeleton of this file.

// You may not import any classes; if you do, the autograder will fail.

/**
 * This class implements the MyList interface as a single array a.
 * Elements are stored at positions a[0],...,a[size()-1].
 * Doubling/halving is used to resize the array a when necessary.
 *
 * @param <T> the type of objects stored in the List
 * @author morin
 * @author sharp
 */
public class MyArrayStack<T> implements MyList<T> {

    /**
     * The array used to store elements. Do not remove this.
     */
    T[] a;

    /**
     * The number of elements stored. Do not remove this.
     */
    int n;

    /**
     * Resize the internal array
     */
    protected void resize() {
        @SuppressWarnings("unchecked")
        T[] b = (T[]) new Object[Math.max(n * 2, 1)];
        for (int i = 0; i < n; i++) {
            b[i] = a[i];
        }
        //System.out.printf( "resize from %d to %d\n", a.length, b.length);
        a = b;
    }

    /**
     * Constructor
     */
    @SuppressWarnings("unchecked")
    public MyArrayStack() {
        a = (T[]) new Object[1];
        n = 0;
    }

    /**
     * Constructor
     *
     * @param cap
     */
    @SuppressWarnings("unchecked")
    public MyArrayStack(int cap) {
        a = (T[]) new Object[cap];
        n = 0;
    }

    public int size() {
        return n;
    }

    public T get(int i) {
        if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
        return a[i];
    }

    public T set(int i, T x) {
        if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
        T y = a[i];
        a[i] = x;
        return y;
    }

    public void add(int i, T x) {
        if (i < 0 || i > n) throw new IndexOutOfBoundsException();
        if (n + 1 > a.length) resize();
        for (int j = n; j > i; j--)
            a[j] = a[j - 1];
        a[i] = x;
        n++;
    }

    public T remove(int i) {
        if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
        T x = a[i];
        for (int j = i; j < n - 1; j++)
            a[j] = a[j + 1];
        n--;
        if (a.length >= 3 * n) resize();
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
            s += a[i];
            s += ", ";
        }
        if (n > 0) {
            s += a[n - 1];
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
            a = (T[]) new Object[0];
            n = 0;
            return;
        }

        T[] oldArr = deepCopy(a);
        T[] newCopyedArr = (T[]) new Object[a.length * k];
        int newCopyedArrIdx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                newCopyedArr[newCopyedArrIdx++] = oldArr[i];
            }
        }

        a = newCopyedArr;
        n *= k;
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


    // Creates a MyArrayStack of n elements, then calls
    // copy on that list.
    public static void testCopy(int n, int k) {
        System.out.println("Test copy("+k+") ------");
        MyList<Integer> mal = new MyArrayStack<Integer>();

        // Create a MyArrayStack with n elements 0, 1, 2, ..., n-1.
        for (int i = 0; i < n; i++) {
            mal.add(i, mal.size());
        }
        System.out.println(mal);

        // Call copy on it. Print out the results.
        mal.copy(k);
        System.out.println(mal);
        System.out.println("Done Test copy------");
    }

}
