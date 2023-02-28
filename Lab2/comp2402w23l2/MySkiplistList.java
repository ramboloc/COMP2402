package comp2402w23l2;
// Thank you Pat Morin for the basic skeleton of this file.

import java.lang.reflect.Array;
import java.lang.IllegalStateException;
import java.util.ListIterator;
import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList; 


/**
 * Implements the List interface as a skiplist so that all the
 * standard operations take O(log n) time
 *
 * @param <T>
 * @author morin
 * @author sharp
 */
public class MySkiplistList<T> implements MyList<T> {
    class Node {
        T x;
        Node[] next;
        int[] length;

        @SuppressWarnings("unchecked")
        public Node(T ix, int h) {
            x = ix;
            next = (Node[]) Array.newInstance(Node.class, h + 1);
            length = new int[h + 1];
        }

        public int height() {
            return next.length - 1;
        }
    }

    /**
     * This node sits on the left side of the skiplist
     */
    protected Node sentinel;

    /**
     * The maximum height of any element
     */
    int h;

    /**
     * The number of elements stored in the skiplist
     */
    int n;

    /**
     * A source of random numbers
     */
    Random rand;

    public MySkiplistList() {
        n = 0;
        sentinel = new Node(null, 32);
        h = 0;
        rand = new Random(0);
    }

    /**
     * Find the node that precedes list index i in the skiplist.
     *
     *
     * @return the predecessor of the node at index i or the final
     * node if i exceeds size() - 1.
     */
    protected Node findPred(int i) {
        Node u = sentinel;
        int r = h;
        int j = -1;   // index of the current node in list 0
        while (r >= 0) {
            while (u.next[r] != null && j + u.length[r] < i) {
                j += u.length[r];
                u = u.next[r];
            }
            r--;
        }
        return u;
    }

    public T get(int i) {
        if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
        return findPred(i).next[0].x;
    }

    public T set(int i, T x) {
        if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
        Node u = findPred(i).next[0];
        T y = u.x;
        u.x = x;
        return y;
    }



    // Prof Alexa wrote this to help debug when her lengths were turning
    // negative for some reason. If you want to use it, feel free. It has
    // not been rigorously tested, but it should return true if there is
    // any length in the skiplist that is negative (which shouldn't happen.
    // If it's happening, you're doing something wrong.)
    public boolean checkNegLengths() {
        Node u = sentinel;
        int k = u.height(); // probably don't need this
        int r = u.height();
        int j = -1; // index of u
        while (r >= 0) {
            while (u.next[r] != null) {
                if (u.length[r] < 0) {
                    return true;
                }
                u = u.next[r];
            }
            if (u.length[r] < 0) {
                System.out.println("r: " + r + ", u.x: " + u.x + ", u.length[r]: " + u.length[r]);
                return true;
            }
            r--;
        }
        return false;
    }

    // DO NOT CHANGE THIS FUNCTION
    // otherwise the server tests might fail.
    public MySkiplistList<T> removeFirst() {
        if( this.size() == 0 )      return new MySkiplistList<T>();

        MySkiplistList<T> other = new MySkiplistList<T>();
        T first = remove(0);
        other.n = this.n;
        other.sentinel = this.sentinel;
        other.h = this.h;

        this.sentinel = new Node(null, 32);
        this.n = 0;
        this.h = 0;
        this.add(0, first);
        return other;
    }


    /**
     * Insert a new node into the skiplist
     *
     * @param i the index of the new node
     * @param w the node to insert
     * @return the node u that precedes v in the skiplist
     */
    protected Node add(int i, Node w) {
        Node u = sentinel;
        int k = w.height();
        int r = h;
        int j = -1; // index of u
        while (r >= 0) {
            while (u.next[r] != null && j + u.length[r] < i) {
                j += u.length[r];
                u = u.next[r];
            }
            u.length[r]++; // accounts for new node in list 0
            if (r <= k) {
                w.next[r] = u.next[r];
                u.next[r] = w;
                w.length[r] = u.length[r] - (i - j);
                if (w.next[r] == null) w.length[r] = 0;
                u.length[r] = i - j;
            }
            r--;
        }
        n++;
        return u;
    }


    /**
     * Simulate repeatedly tossing a coin until it comes up tails.
     * Note, this code will never generate a height greater than 32
     *
     * @return the number of coin tosses - 1
     */
    protected int pickHeight() {
        int z = rand.nextInt();
        int k = 0;
        int m = 1;
        while ((z & m) != 0) {
            k++;
            m <<= 1;
        }
        return k;
    }

    // This is just for testing purposes, for if you want a
    // non-randomized SkiplistList. Don't change this function
    // as it might be needed for the server tests.
    // But feel free to use it in your tests. Just be sure to
    // test on random-height Skiplists too!
    public void createFixedSkiplistList(ArrayList<T> elts) {
        int[] heights = new int[elts.size()];
        int m = elts.size();
        for (int i = 0; i < elts.size(); i++) {
            if (i % 2 == 0) heights[i] = 0;
            if (i % 4 == 0) heights[i] = 1;
            if (i % 8 == 0) heights[i] = 2;
            if (i % 16 == 0) heights[i] = 3;
            if (i % 32 == 0) heights[i] = 4;
            if (i % 64 == 0) heights[i] = 5;
            if (i % 128 == 0) heights[i] = 6;
            if (i % 256 == 0) heights[i] = 7;
            if (i % 512 == 0) heights[i] = 8;

            if (i == 0) heights[i] = 0;
        }

        for (int i = 0; i < elts.size(); i++) {
            Node w = new Node(elts.get(i), heights[i]);
            if (w.height() > h) {
                h = w.height();
            }
            add(i, w);
        }
    }

    public void add(int i, T x) {
        if (i < 0 || i > n) throw new IndexOutOfBoundsException();
        Node w = new Node(x, pickHeight());
        if (w.height() > h)
            h = w.height();
        add(i, w);
    }

    public T remove(int i) {
        if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
        T x = null;
        Node u = sentinel;
        int r = h;
        int j = -1; // index of node u
        while (r >= 0) {
            while (u.next[r] != null && j + u.length[r] < i) {
                j += u.length[r];
                u = u.next[r];
            }
            u.length[r]--;  // for the node we are removing
            if (j + u.length[r] + 1 == i && u.next[r] != null) {
                x = u.next[r].x;
                u.length[r] += u.next[r].length[r];
                u.next[r] = u.next[r].next[r];
                if (u == sentinel && u.next[r] == null)
                    h--;
            }
            r--;
        }
        n--;
        return x;
    }

    /**
     * Replace each element x of MyList with k copies of x.
     *
     * @param k
     */
    public void copy(int k) {
        // TODO(student): Your code goes here.

        T x = null;
       // Node u = sentinel;
        int r = h;
        int j = -1; // index of node u
        ArrayList<T> arrayList=new ArrayList<>();
        Node u = sentinel.next[0];
        if(k>0){


            while( u != null ) {
                arrayList.add(u.x);
                u = u.next[0];

            }
            /*while (r >= 0) {
                while (u.next[r] != null ) {

                    j += u.length[r];
                    System.out.println(j);
                    if(u.x!=null){
                        arrayList.add(u.x);
                    }
                    u = u.next[r];
                }

                r--;
            }
            arrayList.add(u.x);*/
            //MySkiplistList<T> tMySkiplistList=this;
            for (int i=0;i<arrayList.size();i++) {
                T t =arrayList.get(i);
                if(t!=null){
                    for(j=0;j<k-1;j++){
                        this.add(i*k,t);
                    }
                }
            }

        }else {
            n = 0;
            sentinel = new Node(null, 32);
            h = 0;
            rand = new Random(0);
        }




    }


    public ListIterator<T> iterator() {
        class SkiplistIterator implements ListIterator<T> {
            Node u;
            int i;
            boolean removable;

            public SkiplistIterator() {
                u = sentinel;
                i = -1;
                removable = false;
            }

            public boolean hasNext() {
                return u.next[0] != null;
            }

            public T next() {
                u = u.next[0];
                i++;
                removable = true;
                return u.x;
            }

            public T previous() {
                u = findPred(i);   // wish we could do u.prev -- this op is NOT O(1)
                i--;
                removable = true;
                return u.x;
            }

            public boolean hasPrevious() {
                return u != sentinel;
            }

            public void remove() { // Not fast
                if (!removable)
                    throw new IllegalStateException();
                MySkiplistList.this.remove(i);
                i--;
                removable = false;
            }

            public void add(T x) { // Not fast
                MySkiplistList.this.add(i + 1, x);
                i++;
                removable = false;
            }

            public void set(T x) {
                if (!removable)
                    throw new IllegalStateException();
                u.x = x;
            }

            public int previousIndex() {
                return i - 1;
            }

            public int nextIndex() {
                return i + 1;
            }
        }
        return new SkiplistIterator();
    }


    public void clear() {
        n = 0;
        h = 0;
        Arrays.fill(sentinel.length, 0);
        Arrays.fill(sentinel.next, null);
    }

    public int size() {
        return n;
    }


  public String toString() {
    StringBuilder retStr = new StringBuilder();
    retStr.append("[");
    Node u = sentinel.next[0];
    while( u != null ) {
      retStr.append(u.x);
      u = u.next[0];
      if( u != null ) {
        retStr.append(", ");
      }
    }
    retStr.append("]");
    return retStr.toString();
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




    // Creates a MyList of n elements, then calls
    // copy on that list.
    public static void testCopy(int n, int k) {
        System.out.println("Test copy("+k+") ------");
        MyList<Integer> ml = new MySkiplistList<Integer>();

        // Create a MySkiplistList with n elements 0, 1, 2, ..., n-1.
        for (int i = 0; i < n; i++) {
            ml.add(i, ml.size());
        }
        System.out.println(ml);

        // Call copy on it. Print out the results.
        ml.copy(k);
        System.out.println(ml);
        System.out.println("size: " + ml.size() );
        for( int i=0; i < ml.size(); i++ ) {
            System.out.println("get("+i+") = " + ml.get(i));
        }
        System.out.println("Done Test copy------");
    }

}
