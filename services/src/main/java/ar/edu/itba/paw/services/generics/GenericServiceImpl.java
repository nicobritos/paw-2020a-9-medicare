package ar.edu.itba.paw.services.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.interfaces.services.generic.GenericService;
import ar.edu.itba.paw.models.GenericModel;

import java.util.Collection;

/**
 * This provides a generic Service abstract class
 * @param <M> the Service model type
 * @param <I> the Model's id type
 */
public abstract class GenericServiceImpl<D extends GenericDao<M, I>, M extends GenericModel<I>, I> implements GenericService<M, I> {
    protected D repository;

    public GenericServiceImpl(D repository) {
        this.repository = repository;
    }

    @Override
    public M findById(I id) {
        return this.repository.findById(id);
    }

    @Override
    public Collection<M> findByIds(Collection<I> ids) {
        return this.repository.findByIds(ids);
    }

    @Override
    public M create(M model) {
        return this.repository.create(model);
    }

    @Override
    public void update(M model) {
        this.repository.update(model);
    }

    @Override
    public void remove(M model) {
        this.repository.remove(model);
    }

    @Override
    public void remove(I id) {
        this.repository.remove(id);
    }

    @Override
    public Collection<M> list() {
        return this.repository.list();
    }
}
