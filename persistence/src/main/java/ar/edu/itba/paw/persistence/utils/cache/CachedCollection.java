package ar.edu.itba.paw.persistence.utils.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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

    public Set<T> getCollectionAsSet() {
        if (this.collection instanceof Set) {
            return (Set<T>) this.collection;
        }
        return new HashSet<>(this.collection);
    }
}
