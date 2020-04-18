package ar.edu.itba.paw.persistence.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericSearchableDao;
import ar.edu.itba.paw.models.GenericModel;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;

import javax.sql.DataSource;
import java.util.Set;

/**
 * This provides a generic listable DAO abstract class
 * This class should be extended by all DAOs that
 * support searching data by name
 * @param <M> the DAO model type
 * @param <I> the Model's id type
 */
public abstract class GenericSearchableDaoImpl<M extends GenericModel<M, I>, I> extends GenericDaoImpl<M, I> implements GenericSearchableDao<M, I> {
    public GenericSearchableDaoImpl(DataSource dataSource, Class<M> mClass, Class<I> iClass) {
        super(dataSource, mClass, iClass);
    }

    /**
     * Returns a collection of <M> that have a name similar to the one provided.
     * The search is not case-sensitive nor exact.
     * By default, runs a query against a column name = "name". If it doesn't exist, it will throw an SQLException
     * @param name the model's name
     * @return a collection of matched models
     */
    @Override
    public Set<M> findByName(String name) {
        return this.findByFieldIgnoreCase("name", Operation.LIKE, name);
    }
}
