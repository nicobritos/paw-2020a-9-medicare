package ar.edu.itba.paw.interfaces.daos.generic;

import ar.edu.itba.paw.models.GenericModel;

import java.util.Collection;

/**
 * This provides a generic searchable and listable DAO interface
 * This interface should be implemented by all DAOs that
 * support searching data by name and listing all of their data
 * @param <M> the DAO model type
 * @param <I> the Model's id type
 */
public interface GenericSearchableListableDao<M extends GenericModel<I>, I> extends GenericSearchableDao<M, I>, GenericListableDao<M, I> {
}
