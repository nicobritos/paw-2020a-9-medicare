package ar.edu.itba.paw.models;

import java.util.Collection;

/**
 * This class provides a wrapper so that a DAO can keep track of the models added or removed
 * This is done by keeping a copy of the collections in privateModels.
 * Albeit this solution prevents the usage of normal Collections inside models to represent a OneToMany or ManyToMany
 * relation field, this is the easiest and fastest to implement method.
 * @param <M> the model type
 */
public class JoinedCollection<M> {
    /**
     * This provides the field name to be able to access "privateModels" via reflections
     * {@link #privateModels}
     */
    public static final String _PRIVATE_COLLECTION_NAME = "privateModels";

    private Collection<M> privateModels;
    private Collection<M> models;

    public Collection<M> getModels() {
        return this.models;
    }

    public void setModels(Collection<M> models) {
        this.models = models;
    }
}
