package ar.edu.itba.paw.persistence.generics;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.GenericModel;
import ar.edu.itba.paw.persistence.database.JDBCQueryBuilder;
import ar.edu.itba.paw.persistence.database.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.database.JDBCWhereClauseBuilder;
import ar.edu.itba.paw.persistence.database.JDBCWhereClauseBuilder.Operation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

/**
 * This provides a generic DAO abstract class
 * @param <M> the DAO model type
 * @param <I> the Model's id type
 */
public abstract class GenericDaoImpl<M extends GenericModel<I>, I> implements GenericDao<M, I> {
    protected JdbcTemplate jdbcTemplate;

    public GenericDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public M findById(I id) {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .select("*")
                .from(this.getTableName(), this.getTableAlias())
                .where(
                        new JDBCWhereClauseBuilder()
                        .where(this.getTableAlias() + "." + this.getIdColumnName(), Operation.EQ, id.toString())
                )
                .addArgument(id);

        List<M> list = this.query(queryBuilder.getQueryAsString(), queryBuilder.getArguments());
        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public void save(M model) {

    }

    @Override
    public void remove(M model) {

    }

    protected List<M> query(String query, Object... args) {
        return this.jdbcTemplate.query(query, this.getRowMapper(), args);
    }

    protected String getTableAlias() {
        return this.getTableName();
    }

    protected abstract RowMapper<M> getRowMapper();

    protected abstract String getTableName();

    protected abstract String getIdColumnName();
}
