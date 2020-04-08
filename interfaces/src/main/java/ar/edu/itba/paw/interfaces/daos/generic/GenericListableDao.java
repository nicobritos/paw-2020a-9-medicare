package ar.edu.itba.paw.interfaces.daos.generic;

import ar.edu.itba.paw.models.GenericModel;

import java.util.Collection;

/**
 * This provides a generic listable DAO interface
 * This interface should be implemented by all DAOs that
 * support listing all of their data
 * @param <M> the DAO model type
 * @param <I> the Model's id type
 */
public interface GenericListableDao<M extends GenericModel<I>, I> extends GenericDao<M, I> {
    Collection<M> list();
}
