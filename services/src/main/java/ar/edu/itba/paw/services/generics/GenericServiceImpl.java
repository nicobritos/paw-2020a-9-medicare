package ar.edu.itba.paw.services.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.interfaces.services.generic.GenericService;
import ar.edu.itba.paw.models.GenericModel;

/**
 * This provides a generic Service abstract class
 * @param <M> the Service model type
 * @param <I> the Model's id type
 */
public abstract class GenericServiceImpl<M extends GenericModel<I>, I> implements GenericService<M, I> {
    protected GenericDao<M, I> repository;

    @Override
    public M findById(I id) {
        return this.repository.getById(id);
    }

    @Override
    public void remove(M model) {
        this.repository.remove(model);
    }
}
