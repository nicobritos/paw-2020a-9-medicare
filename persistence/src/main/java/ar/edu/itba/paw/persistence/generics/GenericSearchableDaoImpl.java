package ar.edu.itba.paw.persistence.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericSearchableDao;
import ar.edu.itba.paw.models.GenericModel;
import ar.edu.itba.paw.persistence.database.JDBCQueryBuilder;
import ar.edu.itba.paw.persistence.database.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.database.JDBCWhereClauseBuilder;
import ar.edu.itba.paw.persistence.database.JDBCWhereClauseBuilder.Operation;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * This provides a generic listable DAO abstract class
 * This class should be extended by all DAOs that
 * support searching data by name
 * @param <M> the DAO model type
 * @param <I> the Model's id type
 */
public abstract class GenericSearchableDaoImpl<M extends GenericModel<I>, I> extends GenericDaoImpl<M, I> implements GenericSearchableDao<M, I> {
    public GenericSearchableDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Collection<M> findByName(String name) {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .select("*")
                .from(this.getTableName(), this.getTableAlias())
                .where(
                        new JDBCWhereClauseBuilder()
                                .where(this.getTableAlias() + "." + this.getIdColumnName(), Operation.LIKE, name, true)
                )
                .addArgument(name);

        return this.query(queryBuilder.getQueryAsString(), queryBuilder.getArguments());
    }
}
