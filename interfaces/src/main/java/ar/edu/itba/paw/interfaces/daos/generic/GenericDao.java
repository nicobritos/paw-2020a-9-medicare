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

    /**
     * Searches for a collection of models that have a columnName equals to the provided object's value
     * @param columnName the db column name
     * @param value the column's value. If it extends GenericModel then its ID will be used
     * @return a collection of models found
     */
    Set<M> findByField(String columnName, Object value);

    /**
     * Searches for a collection of models that have a columnName equals to the provided string (case insensitive)
     * @param columnName the db column name
     * @param value the column's value
     * @return a collection of models found
     */
    Set<M> findByFieldIgnoreCase(String columnName, String value);

    Class<M> getModelClass();
}
