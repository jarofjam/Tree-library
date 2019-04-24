package tree;

import java.util.ArrayList;

public class LeftistHeap {

    private Node root;
    private int size;

    public LeftistHeap() {
        this.root = null;
    }

    public void add(int value) {
        if (root == null) {
            root = new Node(value);
            size = 1;
            return;
        }

        root = merge(root, new Node(value));
        size++;
    }

    public int peek() {
        if (size == 0)
            return 0;

        return root.value;
    }

    public int poll() {
        if (size == 0)
            return 0;

        int result = root.value;

        root = merge(root.L, root.R);

        size--;
        return result;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public Object[] toArray() {
        if (size == 0)
            return new Object[0];

        ArrayList<Integer> ar = new ArrayList<Integer>();
        walkInOrder(root, ar);

        return ar.toArray();
    }

    public void merge(LeftistHeap anotherHeap) {
        if (anotherHeap == null)
            throw new IllegalArgumentException();

        root = merge(root, anotherHeap.root);
        size += anotherHeap.size;
    }

    private Node merge(Node L, Node R) {
        if (L == null) return R;
        if (R == null) return L;

        Node N = null;

        if (L.value <= R.value) {
            N = L;
            N.R = merge(L.R, R);
        } else {
            N = R;
            N.L = merge(L, R.L);
        }

        N.checkChildren();

        N.update();

        return N;
    }

    private void walkInOrder(Node N, ArrayList<Integer> ar) {
        if (N.L != null)
            walkInOrder(N.L, ar);

        ar.add(N.value);

        if (N.R != null)
            walkInOrder(N.R, ar);
    }

    private final class Node {

        private final int value;
        private Node L;
        private Node R;

        private int d = 0;

        Node(int value) {
            this.value = value;
            this.L = null;
            this.R = null;
            this.d = 1;
        }

        Node(int value, Node L, Node R) {
            this.value = value;

            if (L.d < R.d) {
                this.R = L;
                this.L = R;
            } else {
                this.L = L;
                this.R = R;
            }

            this.d = L.d + R.d + 1;
        }

        void checkChildren() {
            if (R == null) return;

            if (L == null || R.d > L.d) {
                Node temp = L;
                L = R;
                R = temp;
            }
        }

        void update() {
            d = 1;
            if (L != null) d += L.d;

            if (R != null) d += R.d;
        }
    }
}
