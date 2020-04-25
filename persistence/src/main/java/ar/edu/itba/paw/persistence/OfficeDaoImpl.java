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
import ar.edu.itba.paw.persistence.utils.cache.CacheHelper;
import ar.edu.itba.paw.persistence.utils.cache.FilteredCachedCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OfficeDaoImpl extends GenericSearchableDaoImpl<Office, Integer> implements OfficeDao {
    private final RowMapper<Office> rowMapper = (resultSet, rowNum) -> this.hydrate(resultSet);
    public static final String TABLE_NAME = getTableNameFromModel(Office.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Office.class);

    @Autowired
    public OfficeDaoImpl(DataSource dataSource) {
        super(dataSource, Office.class, Integer.class);
    }

    @Override
    public List<Office> findByCountry(Country country) {
        FilteredCachedCollection<Office> cachedCollection = CacheHelper.filter(
                Office.class,
                Integer.class,
                office -> office.getLocality().getProvince().getCountry().equals(country)
        );
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsList();
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("country_id", country.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where("country_id", Operation.EQ, ":country_id");

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join("locality_id", LocalityDaoImpl.TABLE_NAME, LocalityDaoImpl.PRIMARY_KEY_NAME)
                .join(LocalityDaoImpl.TABLE_NAME, "province_id", ProvinceDaoImpl.TABLE_NAME, ProvinceDaoImpl.PRIMARY_KEY_NAME)
                .where(whereClauseBuilder)
                .distinct();

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public List<Office> findByProvince(Province province) {
        FilteredCachedCollection<Office> cachedCollection = CacheHelper.filter(
                Office.class,
                Integer.class,
                office -> office.getLocality().getProvince().equals(province)
        );
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsList();
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("province_id", province.getId());

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where("province_id", Operation.EQ, ":province_id");

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableAlias())
                .join("locality_id", LocalityDaoImpl.TABLE_NAME, LocalityDaoImpl.PRIMARY_KEY_NAME)
                .where(whereClauseBuilder)
                .distinct();

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    public List<Office> findByLocality(Locality locality) {
        return this.findByField("locality_id", locality);
    }

    @Override
    protected RowMapper<Office> getRowMapper() {
        return this.rowMapper;
    }
}
