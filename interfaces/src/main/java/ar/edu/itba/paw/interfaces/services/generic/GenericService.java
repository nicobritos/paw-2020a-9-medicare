package ar.edu.itba.paw.interfaces.services.generic;

import ar.edu.itba.paw.models.GenericModel;

/**
 * This provides a generic Service interface
 * @param <M> the Service model type
 * @param <I> the Model's id type
 */
public interface GenericService<M extends GenericModel<I>, I> {
    M findById(I id);

    void remove(M model);
}
