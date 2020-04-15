package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.OfficeDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.builder.JDBCQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;

@Repository
public class OfficeDaoImpl extends GenericSearchableDaoImpl<Office, Integer> implements OfficeDao {
    private final RowMapper<Office> rowMapper = (resultSet, rowNum) -> this.hydrate(resultSet);
    public static final String TABLE_NAME = getTableNameFromModel(Office.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Office.class);

    @Autowired
    public OfficeDaoImpl(DataSource dataSource) {
        super(dataSource, Office.class);
    }

    @Override
    public Collection<Office> findByCountry(Country country) {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join(LocalityDaoImpl.TABLE_NAME, "locality_id",  LocalityDaoImpl.PRIMARY_KEY_NAME)
                .join(ProvinceDaoImpl.TABLE_NAME, "province_id",  ProvinceDaoImpl.PRIMARY_KEY_NAME)
                .where(new JDBCWhereClauseBuilder()
                        .where("country_id", Operation.EQ, ":country_id")
                )
                .distinct();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("country_id", country.getId());

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Collection<Office> findByProvince(Province province) {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join(LocalityDaoImpl.TABLE_NAME, "locality_id",  LocalityDaoImpl.PRIMARY_KEY_NAME)
                .where(new JDBCWhereClauseBuilder()
                        .where("province_id", Operation.EQ, ":province_id")
                )
                .distinct();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("province_id", province.getId());

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public Collection<Office> findByLocality(Locality locality) {
        return this.findByField("locality_id", locality);
    }

    @Override
    protected RowMapper<Office> getRowMapper() {
        return this.rowMapper;
    }
}
