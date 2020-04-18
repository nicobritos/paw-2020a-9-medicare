package ar.edu.itba.paw.persistence.utils.cache;

import java.util.Collection;
import java.util.LinkedList;

public class FilteredCachedCollection<T> extends CachedCollection<T> {
    private Collection<T> completeCollection;

    protected FilteredCachedCollection(Collection<T> completeCollection, Collection<T> collection, CachedCollectionStatus status) {
        super(collection, status);
        this.completeCollection = completeCollection;
    }

    protected FilteredCachedCollection(CachedCollectionStatus status) {
        super(status);
        this.completeCollection = new LinkedList<>();
    }

    protected FilteredCachedCollection() {
        super();
        this.completeCollection = new LinkedList<>();
    }

    public Collection<T> getCompleteCollection() {
        return this.completeCollection;
    }
}
