package ar.edu.itba.paw.persistence.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericListableDao;
import ar.edu.itba.paw.models.GenericModel;
import ar.edu.itba.paw.persistence.database.JDBCQueryBuilder;
import ar.edu.itba.paw.persistence.database.JDBCSelectQueryBuilder;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * This provides a generic listable DAO abstract class
 * This class should be extended by all DAOs that
 * support listing all of their data
 * @param <M> the DAO model type
 * @param <I> the Model's id type
 */
public abstract class GenericListableDaoImpl<M extends GenericModel<I>, I> extends GenericDaoImpl<M, I> implements GenericListableDao<M, I> {
    public GenericListableDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Collection<M> list() {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .select("*")
                .from(this.getTableName(), this.getTableAlias());

        return this.query(queryBuilder.getQueryAsString(), queryBuilder.getArguments());
    }
}
