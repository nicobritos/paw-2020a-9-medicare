package ar.edu.itba.paw.interfaces.daos.generic;

import ar.edu.itba.paw.models.GenericModel;

/**
 * This provides a generic DAO interface
 * @param <M> the DAO model type
 * @param <I> the Model's id type
 */
public interface GenericDao<M extends GenericModel<I>, I> {
    M getById(I id);

    void save(M model);

    void remove(M model);
}
