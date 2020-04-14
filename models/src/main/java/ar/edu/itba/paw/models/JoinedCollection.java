package ar.edu.itba.paw.models;

import java.util.Collection;

public class JoinedCollection<M> {
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
