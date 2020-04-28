package ar.edu.itba.paw.persistence.utils.cache;

import java.util.*;

public class CachedCollection<T> {
    private CachedCollectionStatus status;
    private Collection<T> collection;

    protected CachedCollection(Collection<T> collection, CachedCollectionStatus status) {
        this.collection = collection;
        this.status = status;
    }

    protected CachedCollection(CachedCollectionStatus status) {
        this(new LinkedList<>(), status);
    }

    protected CachedCollection() {
        this(new LinkedList<>(), CachedCollectionStatus.INCOMPLETE);
    }

    public CachedCollectionStatus getStatus() {
        return this.status;
    }

    public Collection<T> getCollection() {
        return this.collection;
    }

    public List<T> getCollectionAsList() {
        if (this.collection instanceof List) {
            return (List<T>) this.collection;
        }
        return new LinkedList<>(this.collection);
    }

    public List<T> getCollectionAsList() {
        if (this.collection instanceof List) {
            return (List<T>) this.collection;
        }
        return new LinkedList<>(this.collection);
    }
}
