package comp2402w23l2;
// Thank you Pat Morin for the basic skeleton of this file.

import java.util.ListIterator;

/**
 * An implementation of the List interface as a doubly-linked circular list. A
 * dummy node is used to simplify the code.
 *
 * @param <T> the type of elements stored in the list
 * @author morin
 */
public class MyDLList<T> implements MyList<T> {
    class Node {
        T x;
        Node prev, next;
    }

    /**
     * Number of nodes in the list
     */
    int n;

    /**
     * The dummy node. We use the convention that dummy.next = first and
     * dummy.prev = last
     */
    protected Node dummy;

    public MyDLList() {
        dummy = new Node();
        dummy.next = dummy;
        dummy.prev = dummy;
        n = 0;
    }

    /**
     * Add a new node containing x before the node p
     *
     * @param w the node to insert the new node before
     * @param x the value to store in the new node
     * @return the newly created and inserted node
     */
    protected Node addBefore(Node w, T x) {
        Node u = new Node();
        u.x = x;
        u.prev = w.prev;
        u.next = w;
        u.next.prev = u;
        u.prev.next = u;
        n++;
        return u;
    }

    /**
     * Remove the node p from the list
     *
     * @param w the node to remove
     */
    protected void remove(Node w) {
        w.prev.next = w.next;
        w.next.prev = w.prev;
        n--;
    }

    /**
     * Get the i'th node in the list
     *
     * @param i the index of the node to get
     * @return the node with index i
     */
    protected Node getNode(int i) {
        Node p = null;
        if (i < n / 2) {
            p = dummy.next;
            for (int j = 0; j < i; j++)
                p = p.next;
        } else {
            p = dummy;
            for (int j = n; j > i; j--)
                p = p.prev;
        }
        return p;
    }

    public int size() {
        return n;
    }

    public boolean add(T x) {
        addBefore(dummy, x);
        return true;
    }

    public T remove(int i) {
        if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
        Node w = getNode(i);
        remove(w);
        return w.x;
    }

    /**
     * Replace each element x of MyList with k copies of x.
     *
     * @param k
     */
    public void copy(int k) {
        // TODO(student): Your code goes here.
        // Get the first node
        Node p=dummy.next;
        Node q;
        while(p!=dummy){
            if(k==0){
                dummy.prev=dummy;
                dummy.next=dummy;
                n=0;
            }
            for(int i=1;i<k;i++){
                q=new Node();
                q.x=p.x;
                p.prev.next=q;
                q.prev=p.prev;
                p.prev=q;
                q.next=p;
                n+=1;

            }

            p=p.next;
        }
    }

    public void add(int i, T x) {
        if (i < 0 || i > n) throw new IndexOutOfBoundsException();
        addBefore(getNode(i), x);
    }

    public T get(int i) {
        if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
        return getNode(i).x;
    }

    public T set(int i, T x) {
        if (i < 0 || i > n - 1) throw new IndexOutOfBoundsException();
        Node u = getNode(i);
        T y = u.x;
        u.x = x;
        return y;
    }

    public void clear() {
        dummy.next = dummy;
        dummy.prev = dummy;
        n = 0;
    }


	// Note: DO NOT CHANGE THIS FUNCTION.
	// otherwise the server tests might fail.
    public MyDLList<T> removeFirst() {
		if( this.size() == 0 ) return new MyDLList<T>();

        MyDLList<T> other = new MyDLList<T>();
        T first = remove(0);
        other.n = this.n;
        other.dummy = this.dummy;

        this.dummy = new Node();
        this.dummy.next = dummy;
        this.dummy.prev = dummy;
        this.n = 0;
        this.add(0, first);
        return other;
    }


    public String toString() {
        StringBuilder retStr = new StringBuilder();
        retStr.append("[");
        Node u = dummy.next;
        while (u != dummy) {
            retStr.append(u.x);
            u = u.next;
            if (u != dummy) {
                retStr.append(", ");
            }
        }
        retStr.append("]");
        return retStr.toString();
    }

    public ListIterator<T> iterator() {
        return new Iterator(this, 0);
    }

    class Iterator implements ListIterator<T> {
        /**
         * The list we are iterating over
         */
        MyDLList<T> l;

        /**
         * The node whose value is returned by next()
         */
        Node p;

        /**
         * The last node whose value was returned by next() or previous()
         */
        Node last;

        /**
         * The index of p
         */
        int i;

        Iterator(MyDLList<T> il, int ii) {
            l = il;
            i = ii;
            p = l.getNode(i);
        }

        public boolean hasNext() {
            return p != l.dummy;
        }

        public T next() {
            T x = p.x;
            last = p;
            p = p.next;
            i++;
            return x;
        }

        public int nextIndex() {
            return i;
        }

        public boolean hasPrevious() {
            return p.prev != dummy;
        }

        public T previous() {
            p = p.prev;
            last = p;
            i--;
            return p.x;
        }

        public int previousIndex() {
            return i - 1;
        }

        public void add(T x) {
            MyDLList.this.addBefore(p, x);
        }

        public void set(T x) {
            last.x = x;
        }

        public void remove() {
            if (p == last) {
                p = p.next;
            }
            MyDLList.this.remove(last);
        }

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
        MyList<Integer> ml = new MyDLList<Integer>();

        // Create a MyDLList with n elements 0, 1, 2, ..., n-1.
        for (int i = 0; i < n; i++) {
            ml.add(i, ml.size());
        }
        System.out.println(ml);

        // Call copy on it. Print out the results.
        ml.copy(k);
        System.out.println(ml);
        System.out.println("Done Test copy------");
    }
    
}
