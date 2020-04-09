package ar.edu.itba.paw.services.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericListableDao;
import ar.edu.itba.paw.interfaces.services.generic.GenericListableService;
import ar.edu.itba.paw.models.GenericModel;

import java.util.Collection;

/**
 * This provides a generic Service abstract class
 * This class should be extended by all Services that
 * support listing all of their data
 * @param <M> the Service model type
 * @param <I> the Model's id type
 */
public abstract class GenericListableServiceImpl<M extends GenericModel<I>, I> implements GenericListableService<M, I> {
    protected GenericListableDao<M, I> repository;

    @Override
    public M findById(I id) {
        return this.repository.findById(id);
    }

    @Override
    public void remove(M model) {
        this.repository.remove(model);
    }

    @Override
    public Collection<M> list() {
        return this.repository.list();
    }
}
