package ar.edu.itba.paw.interfaces.services.generic;

import ar.edu.itba.paw.models.GenericModel;

import java.util.Collection;
import java.util.Optional;

/**
 * This provides a generic Service interface
 * @param <M> the Service model type
 * @param <I> the Model's id type
 */
public interface GenericService<M extends GenericModel<M, I>, I> {
    Optional<M> findById(I id);

    Collection<M> findByIds(Collection<I> ids);

    M create(M model);

    void update(M model);

    void remove(M model);

    void remove(I id);

    Collection<M> list();
}
