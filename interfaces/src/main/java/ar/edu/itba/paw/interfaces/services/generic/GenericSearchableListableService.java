package ar.edu.itba.paw.interfaces.services.generic;

import ar.edu.itba.paw.models.GenericModel;

/**
 * This provides a generic Service interface
 * This interface should be implemented by all Services that
 * support searching data by name and listing all of their data
 * @param <M> the Service model type
 * @param <I> the Model's id type
 */
public interface GenericSearchableListableService<M extends GenericModel<I>, I> extends GenericListableService<M, I>, GenericSearchableService<M, I> {
}
