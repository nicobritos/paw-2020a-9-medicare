package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.LocalityDao;
import ar.edu.itba.paw.models.Locality;
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
import java.util.List;

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
    public List<Locality> findByProvince(Province province) {
        return this.findByField("province_id", Operation.EQ, province, locality -> locality.getProvince().equals(province));
    }

    @Override
    public List<Locality> findByProvinceAndName(Province province, String name) {
        name = name.toLowerCase();
        String finalName = name;
        FilteredCachedCollection<Locality> cachedCollection = CacheHelper.filter(
                Locality.class,
                Integer.class,
                locality -> locality.getName().toLowerCase().contains(finalName) && locality.getProvince().equals(province)
        );
        if (this.isCacheComplete(cachedCollection)) {
            return cachedCollection.getCollectionAsList();
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("province", province.getId());
        parameterSource.addValue("name", StringSearchType.CONTAINS_NO_ACC.transform(name));

        JDBCWhereClauseBuilder whereClauseBuilder = new JDBCWhereClauseBuilder()
                .where(this.formatColumnFromName("name"), Operation.LIKE, ":name", ColumnTransformer.LOWER)
                .and()
                .where(this.formatColumnFromName("province_id"), Operation.EQ, ":province");

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
    protected RowMapper<Locality> getRowMapper() {
        return this.rowMapper;
    }
}
