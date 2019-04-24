package tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

abstract class TreeStructured<T> implements Iterable<T> {

    AbstractNode root;

    @Override
    public Iterator<T> iterator() {
        return new NodeIterator();
    }

    @Override
    public String toString() {
        if (root == null)
            return "[]";

        List<T> ar = new ArrayList<>(this.getSize());
        walkInOrder(root, ar);

        return ar.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining(", ", "[", "]"));
    }

    void walkInOrder(AbstractNode N, List<T> ar) {
        if (N == null)
            return;

        if (N.getL() != null)
            walkInOrder(N.getL(), ar);

        ar.add(N.getValue());

        if (N.getR() != null)
            walkInOrder(N.getR(), ar);
    }

    public Object[] toArray() {
        if (getSize() == 0)
            return new Object[0];

        List<T> ar = new ArrayList<>(getSize());
        walkInOrder(root, ar);

        return ar.toArray();
    }

    abstract int getSize();

    abstract class AbstractNode {
        AbstractNode L;
        AbstractNode R;

        abstract T getValue();
        abstract AbstractNode getL();
        abstract AbstractNode getR();
    }

    class NodeIterator implements Iterator<T>{
        private List<T> list = new ArrayList<>(getSize());
        int cursor = 0;

        NodeIterator() {
            walkInOrder(root, list);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return cursor != getSize();
        }

        @Override
        public T next() {
            return list.get(cursor++);
        }
    }
}
