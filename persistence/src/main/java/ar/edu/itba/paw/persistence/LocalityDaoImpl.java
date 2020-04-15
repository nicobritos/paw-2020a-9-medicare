package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.LocalityDao;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.builder.JDBCQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.ColumnTransformer;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;

@Repository
public class LocalityDaoImpl extends GenericSearchableDaoImpl<Locality, Integer> implements LocalityDao {
    private final RowMapper<Locality> rowMapper = (resultSet, rowNum) -> this.hydrate(resultSet);
    public static final String TABLE_NAME = getTableNameFromModel(Locality.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Locality.class);

    @Autowired
    public LocalityDaoImpl(DataSource dataSource) {
        super(dataSource, Locality.class, Integer.class);
    }

    @Override
    public Collection<Locality> findByProvince(Province province) {
        return this.findByField("province_id", Operation.EQ, province);
    }

    @Override
    public Collection<Locality> findByProvinceAndName(Province province, String name) {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableName())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromName("name"), Operation.LIKE, ":name", ColumnTransformer.LOWER)
                        .and()
                        .where(this.formatColumnFromName("province_id"), Operation.EQ, ":province")
                );

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("province", province.getId());
        parameterSource.addValue("name", name);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    protected RowMapper<Locality> getRowMapper() {
        return this.rowMapper;
    }
}
