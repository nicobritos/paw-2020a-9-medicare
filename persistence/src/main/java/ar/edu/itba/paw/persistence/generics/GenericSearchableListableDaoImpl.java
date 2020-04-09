package ar.edu.itba.paw.persistence.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericSearchableListableDao;
import ar.edu.itba.paw.models.GenericModel;
import ar.edu.itba.paw.persistence.database.JDBCQueryBuilder;
import ar.edu.itba.paw.persistence.database.JDBCSelectQueryBuilder;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * This provides a generic listable DAO abstract class
 * This class should be extended by all DAOs that
 * support searching data by name and listing all of their data
 * @param <M> the DAO model type
 * @param <I> the Model's id type
 */
public abstract class GenericSearchableListableDaoImpl<M extends GenericModel<I>, I> extends GenericSearchableDaoImpl<M, I> implements GenericSearchableListableDao<M, I> {
    public GenericSearchableListableDaoImpl(DataSource dataSource) {
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
