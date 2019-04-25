package tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

class Trees {

    static <T> Iterator<T> getIterator(Node root, int size) {
        return new Iterator<T>() {
            private List<T> list;
            private int cursor;

            {
                list = new ArrayList<>(size);
                walkInOrder(root, list);
                cursor = 0;
            }

            @Override
            public boolean hasNext() {
                return cursor != size;
            }

            @Override
            public T next() {
                return list.get(cursor++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    static <T> String toString(Node root, int size) {
        if (root == null) {
            return "[]";
        }
        List<T> ar = new ArrayList<>(size);
        walkInOrder(root, ar);
        return ar.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "[", "]"));
    }

    static <T> Object[] toArray(Node root, int size) {
        if (size == 0)
            return new Object[0];

        List<T> ar = new ArrayList<>(size);
        walkInOrder(root, ar);

        return ar.toArray();
    }

    private static <T> void walkInOrder(Node N, List<T> ar) {
        if (N == null)
            return;

        if (N.getL() != null)
            walkInOrder(N.getL(), ar);

        ar.add((T) N.getValue());

        if (N.getR() != null)
            walkInOrder(N.getR(), ar);

    }

    static abstract class Node<T> {
        abstract T getValue();
        abstract Node getL();
        abstract Node getR();
    }
}
