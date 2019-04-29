package tree;

import java.util.Iterator;

/**
 * <p>This class is an implementation of a list that can store any type of elements.</p>
 * <p>The implementation is based on an implicit treap.</p>
 * <p>This class offers an almost O(log(n)) time performance
 * on add/put/set/get/remove operations.<br>
 * Thus, this class is somewhere between ArrayList and LinkedList,
 * it is inferior to each of them in the performance of some operations
 * while being superior in others.</p>
 * <p><strong>Note that this implementation is not synchronized.</strong></p>
 * @param <T> type of elements to be stored in this implicit treap.
 */
public class ImplicitTreap<T> implements Tree<T> {
    /**
     * The root of this implicit treap.
     */
    private Node root;

    /**
     * Creates a new implicit treap.
     */
    public ImplicitTreap() {
        root = null;
    }

    /**
     * Appends the specified element to the end of this list.
     * @param value element to be appended to this list.
     * @throws NullPointerException if the element is null.
     */
    public void add(T value) {
        if (value == null)
            throw new NullPointerException();

        root = merge(root, new Node(value));
    }

    /**
     * Returns the element at the specified position in this list.
     * @param index position of the element to be returned.
     * @return element at the specified position in this list.
     * @throws IndexOutOfBoundsException if there is no such position in this list.
     */
    public T get(int index) {
        checkBounds(index);

        Node M, R;

        PairOfNodes pair = split(root, index);
        R = pair.getR();

        pair = split(R, 1);
        M = pair.getL();

        return M == null ? null : M.value;
    }

    /**
     * Inserts the specified element at the specified position in this list expanding it.
     * @param index index at which the specified element is to be inserted.
     * @param value element to be inserted.
     * @throws IndexOutOfBoundsException if there is no such position in this list.
     * @throws NullPointerException if the element is null.
     */
    public void put(int index, T value) {
        if (value == null)
            throw new NullPointerException();

        checkBounds(index);

        PairOfNodes pair = split(root, index);
        Node L = pair.getL();
        Node R = pair.getR();

        Node M = new Node(value);

        root = merge(merge(L, M), R);
    }

    /**
     * Replaces the value of the element at the specified position in this list with
     * new value.
     * @param index index of the element to be replaced.
     * @param value value to be stored at the specified position.
     * @throws IndexOutOfBoundsException if there is no such position in this list.
     * @throws NullPointerException if the element is null.
     */
    public void set(int index, T value) {
        if (value == null)
            throw new NullPointerException();

        checkBounds(index);

        Node L, R;
        Node N = new Node(value);

        PairOfNodes pair = split(root, index);
        L = pair.getL();
        R = pair.getR();

        pair = split(R, 1);
        R = pair.getR();

        root = merge(merge(L, N), R);
    }

    /**
     * Removes the element at the specified position in this list
     * narrowing it down.
     * @param index the index of the element to be removed.
     * @return element that was removed from this list.
     * @throws IndexOutOfBoundsException if there is no such position in this list.
     */
    public T remove(int index) {
        checkBounds(index);

        Node L, M, R;

        PairOfNodes pair = split(root, index);
        L = pair.getL();
        R = pair.getR();

        pair = split(R, 1);
        M = pair.getL();
        R = pair.getR();

       root = merge(L, R);

       return M.value;
    }

    /**
     * Returns the size of this list.
     * @return the size of this list.
     */
    @Override
    public int getSize() {
        return sizeOf(root);
    }

    /**
     * Returns true if this list contains no elements.
     * @return true if this list contains no elements, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return sizeOf(root) == 0;
    }

    /**
     * Removes all the elements from this list.
     * This list will be empty after this call returns.
     */
    @Override
    public void clear() {
        root = null;
    }

    /**
     * Returns an array containing all the elements from this list.
     * @return array containing all the elements from this list.
     */
    @Override
    public Object[] toArray() {
        return Trees.<T>toArray(root, sizeOf(root));
    }

    /**
     * Returns an array containing all the elements from this list.
     * @param a the array into which the elements from this list are to
     * be stored, if it is big enough; otherwise, a new array of the
     * same type is allocated.
     * @return array containing all of the elements from this list.
     */
    @Override
    public <T2> T2[] toArray(T2[] a) {
        return Trees.<T2>toArray(a, root, sizeOf(root));
    }

    /**
     * Returns a string representation of this list.
     * @return string representation of this list.
     */
    @Override
    public String toString() {
        return Trees.<T>toString(root, sizeOf(root));
    }

    /**
     * Returns an iterator over the elements from this list.
     * @return iterator over the elements from this list.
     */
    @Override
    public Iterator<T> iterator() {
        return Trees.<T>getIterator(root, sizeOf(root));
    }

    /**
     * Merges two specified implicit treaps into one.
     * @param L first implicit treap to be merged.
     * @param R second implicit treap to be merged.
     * @return new implicit treap which contains all the elements from two that were merged.
     */
    private Node merge(Node L, Node R) {
        if (L == null) return R;
        if (R == null) return L;

        Node N;

        if (L.y > R.y)
            N = new Node(L.value, L.y, L.L, merge(L.R, R));
        else
            N = new Node(R.value, R.y, merge(L, R.L), R.R);

        N.updateSize();
        return N;
    }

    /**
     * Splits a specified implicit treap into two new implicit treaps using the key.
     * @param N implicit treap to be splitted.
     * @param x split key.
     * @return pair of implicit treaps that contain all the elements from the one that got splitted.
     */
    private PairOfNodes split(Node N, int x) {
        if (N == null)
            return new PairOfNodes(null, null);

        Node L;
        Node R;
        Node newNode = null;
        int curIndex = sizeOf(N.L) + 1;

        if (curIndex <= x) {
            if (N.R == null) {
                R = null;
            } else {
                PairOfNodes pair = split(N.R, x - curIndex);
                newNode = pair.getL();
                R = pair.getR();
            }
            L = new Node(N.value, N.y, N.L, newNode);
            L.updateSize();
        } else {
            if (N.L == null) {
                L = null;
            } else {
                PairOfNodes pair = split(N.L, x);
                L = pair.getL();
                newNode = pair.getR();
            }
            R = new Node(N.value, N.y, newNode, N.R);
            R.updateSize();
        }

        return new PairOfNodes(L, R);
    }

    /**
     * Checks if the specified index is within the size of this list.
     * @param index index whose presence will be checked.
     * @throws IndexOutOfBoundsException if there is no such index int this list.
     */
    private void checkBounds(int index) {
        if (root == null || index >= root.size || index < 0)
            throw new IndexOutOfBoundsException("Index: " + index + " size: " + sizeOf(root));
    }

    /**
     * Returns the size of the specified node.
     * @param N node whose size will be returned.
     * @return size of the node or 0 if the node is null.
     */
    private int sizeOf(Node N) {
        return N == null ? 0 : N.size;
    }

    /**
     * A node of this implicit treap.
     * Actually each node can be considered as an implicit treap.
     */
    private class Node extends Trees.Node<T> {
        /**
         * The value that is stored in this node.
         */
        T value;
        /**
         * Second key.
         */
        double y;
        /**
         * The size of this node.
         */
        int size;

        /**
         * Left child.
         */
        Node L;
        /**
         * Right child.
         */
        Node R;

        /**
         * Creates a new node with the specified value.
         * @param value value to be stored.
         */
        Node(T value) {
            this(value, Math.random(), null, null);
        }

        /**
         * Creates a new node with the specified value and both children.
         * @param value value to be stored.
         * @param L left child.
         * @param R right child.
         */
        Node(T value, Node L, Node R) {
            this(value, Math.random(), L, R);
        }

        /**
         * Creates a new node with the specified value, second key and both children.
         * @param value value to be stored.
         * @param y second key.
         * @param L left child.
         * @param R right child.
         */
        Node(T value, double y, Node L, Node R) {
            this.value = value;
            this.y = y;
            this.L = L;
            this.R = R;
            this.size = 1;
        }

        /**
         * Updates the size of the node, considering that both children
         * store valid size.
         */
        void updateSize() {
            size = sizeOf(L) + sizeOf(R) + 1;
        }

        /**
         * Returns the value stored.
         * @return value stored.
         */
        @Override
        T getValue() {
            return value;
        }

        /**
         * Returns left child.
         * @return left child.
         */
        @Override
        Node getL() {
            return L;
        }

        /**
         * Returns right child.
         * @return right child.
         */
        @Override
        Node getR() {
            return R;
        }
    }

    /**
     * Helper class.
     * Allows methods to return two implicit treaps at ones.
     * Is used in slitting methods.
     * An instance contains two implicit treaps, which are
     * are referred as left and right.
     */
    private class PairOfNodes {
        /**
         * Left implicit treap.
         */
        private Node L;
        /**
         * Right implicit treap.
         */
        private Node R;

        /**
         * Returns left implicit treap.
         * @return left implicit treap.
         */
        Node getL() {
            return L;
        }

        /**
         * Returns right implicit treap.
         * @return right implicit treap.
         */
        Node getR() {
            return R;
        }

        /**
         * Creates new pair of implicit treaps..
         * @param L first implicit treap (will be referred as left).
         * @param R second implicit treap (will be referred as right).
         */
        PairOfNodes(Node L, Node R) {
            this.L = L;
            this.R = R;
        }
    }
}
