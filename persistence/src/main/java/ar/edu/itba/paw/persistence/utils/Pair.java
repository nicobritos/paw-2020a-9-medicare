package ar.edu.itba.paw.persistence.utils;

public class Pair<T, R> {
    private final R right;
    private final T left;

    public Pair(T left, R right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return this.left;
    }

    public R getRight() {
        return this.right;
    }
}
