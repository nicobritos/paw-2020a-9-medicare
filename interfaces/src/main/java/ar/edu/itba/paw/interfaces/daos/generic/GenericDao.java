package ar.edu.itba.paw.interfaces.daos.generic;

import ar.edu.itba.paw.models.GenericModel;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * This provides a generic DAO interface
 * @param <M> the DAO model type
 * @param <I> the Model's id type
 */
public interface GenericDao<M extends GenericModel<M, I>, I> {
    Optional<M> findById(I id);

    Set<M> findByIds(Collection<I> ids);

    M create(M model);

    void update(M model);

    void remove(M model);

    void remove(I id);

    Set<M> list();

    Class<M> getModelClass();
}
