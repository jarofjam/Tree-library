package tree;

public interface Heap<T> extends Iterable<T> {

    void add(T value);
    T peek();
    T poll();
    int getSize();
    boolean isEmpty();
    void clear();
    Object[] toArray();
}
