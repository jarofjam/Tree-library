package tree;

public interface Heap<T> extends Iterable<T>, Tree<T> {
    void add(T value);
    T peek();
    T poll();
}
