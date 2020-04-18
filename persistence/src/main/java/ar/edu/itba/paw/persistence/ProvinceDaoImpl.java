package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.ProvinceDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.StringSearchType;
import ar.edu.itba.paw.persistence.utils.builder.JDBCQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.ColumnTransformer;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import ar.edu.itba.paw.persistence.utils.cache.CacheHelper;
import ar.edu.itba.paw.persistence.utils.cache.FilteredCachedCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Set;

@Repository
public class ProvinceDaoImpl extends GenericSearchableDaoImpl<Province, Integer> implements ProvinceDao {
    private final RowMapper<Province> rowMapper = (resultSet, rowNum) -> this.hydrate(resultSet);
    public static final String TABLE_NAME = getTableNameFromModel(Province.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(Province.class);

    @Autowired
    public ProvinceDaoImpl(DataSource dataSource) {
        super(dataSource, Province.class, Integer.class);
    }

    @Override
    public Set<Province> findByCountry(Country country) {
        return this.findByField("country_id", Operation.EQ, country);
    }

    @Override
    public Set<Province> findByCountryAndName(Country country, String name) {
        name = name.toLowerCase();
        String finalName = name;
        FilteredCachedCollection<Province> cachedCollection = CacheHelper.filter(
                Province.class,
                Integer.class,
                province -> province.getName().toLowerCase().contains(finalName) && province.getCountry().equals(country)
        );
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsSet();
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("country", country.getId());
        parameterSource.addValue("name", StringSearchType.CONTAINS.transform(name));

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("name"), Operation.LIKE, ":name", ColumnTransformer.LOWER)
                .and()
                .where(this.formatColumnFromName("country_id"), Operation.EQ, ":country");

        if (!cachedCollection.getCollection().isEmpty()) {
            this.excludeModels(cachedCollection.getCompleteCollection(), parameterSource, whereClauseBuilder);
        }

        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .selectAll()
                .from(this.getTableName())
                .where(whereClauseBuilder);

        return this.selectQuery(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    protected RowMapper<Province> getRowMapper() {
        return this.rowMapper;
    }
}
