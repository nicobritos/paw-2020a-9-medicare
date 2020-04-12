package ar.edu.itba.paw.interfaces.daos.generic;

import ar.edu.itba.paw.models.GenericModel;

import java.util.Collection;

/**
 * This provides a generic DAO interface
 * @param <M> the DAO model type
 * @param <I> the Model's id type
 */
public interface GenericDao<M extends GenericModel<I>, I> {
    M findById(I id);

    Collection<M> findByIds(Collection<I> ids);

    M create(M model);

    void update(M model);

    void remove(M model);

    void remove(I id);

    Collection<M> list();

    Collection<M> findByField(String columnName, Object value);

    Class<M> getModelClass();
}
