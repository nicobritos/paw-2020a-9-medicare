package ar.edu.itba.paw.services.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericSearchableListableDao;
import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableListableService;
import ar.edu.itba.paw.models.GenericModel;

import java.util.Collection;

/**
 * This provides a generic Service abstract class
 * This class should be extended by all Services that
 * support searching data by name and listing all of their data
 * @param <M> the Service model type
 * @param <I> the Model's id type
 */
public abstract class GenericSearchableListableServiceImpl<M extends GenericModel<I>, I> implements GenericSearchableListableService<M, I> {
    protected GenericSearchableListableDao<M, I> repository;

    @Override
    public M findById(I id) {
        return this.repository.findById(id);
    }

    @Override
    public void remove(M model) {
        this.repository.remove(model);
    }

    @Override
    public Collection<M> findByName(String name) {
        return this.repository.findByName(name);
    }

    @Override
    public Collection<M> list() {
        return this.repository.list();
    }
}
