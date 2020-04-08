package ar.edu.itba.paw.interfaces.services.generic;

import ar.edu.itba.paw.models.GenericModel;

import java.util.Collection;

/**
 * This provides a generic Service interface
 * This interface should be implemented by all Services that
 * support listing all of their data
 * @param <M> the Service model type
 * @param <I> the Model's id type
 */
public interface GenericListableService<M extends GenericModel<I>, I> extends GenericService<M, I> {
    Collection<M> list();
}
