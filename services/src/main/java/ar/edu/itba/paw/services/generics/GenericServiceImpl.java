package ar.edu.itba.paw.services.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.interfaces.services.generic.GenericService;
import ar.edu.itba.paw.models.GenericModel;

import java.util.Collection;
import java.util.Optional;

/**
 * This provides a generic Service abstract class
 * @param <M> the Service model type
 * @param <I> the Model's id type
 */
public abstract class GenericServiceImpl<DAO extends GenericDao<M, I>, M extends GenericModel<M, I>, I> implements GenericService<M, I> {
    @Override
    public Optional<M> findById(I id) {
        return this.getRepository().findById(id);
    }

    @Override
    public Collection<M> findByIds(Collection<I> ids) {
        return this.getRepository().findByIds(ids);
    }

    @Override
    public M create(M model) {
        return this.getRepository().create(model);
    }

    @Override
    public void update(M model) {
        this.getRepository().update(model);
    }

    @Override
    public void remove(M model) {
        this.getRepository().remove(model);
    }

    @Override
    public void remove(I id) {
        this.getRepository().remove(id);
    }

    @Override
    public Collection<M> list() {
        return this.getRepository().list();
    }
    
    protected abstract DAO getRepository();
}
