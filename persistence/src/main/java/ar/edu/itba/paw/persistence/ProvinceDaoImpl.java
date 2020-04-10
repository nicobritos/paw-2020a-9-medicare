package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.ProvinceDao;
import ar.edu.itba.paw.models.Country;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.Pair;
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
import java.util.HashMap;
import java.util.Map;

@Repository
public class ProvinceDaoImpl extends GenericSearchableDaoImpl<Province, Integer> implements ProvinceDao {
    private static final RowMapper<Province> ROW_MAPPER = (resultSet, rowNum) -> {
        // TODO: Fix hydration
        Country country = new Country();
        country.setId(resultSet.getString("country_id"));

        Province province = new Province();
        province.setId(resultSet.getInt("province_id"));
        province.setCountry(country);
        province.setName(resultSet.getString("name"));

        return province;
    };

    @Autowired
    public ProvinceDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Collection<Province> findByCountry(Country country) {
        return this.findByField("country_id", Operation.EQ, country.getId());
    }

    @Override
    public Collection<Province> findByCountryAndName(Country country, String name) {
        JDBCQueryBuilder queryBuilder = new JDBCSelectQueryBuilder()
                .select(JDBCSelectQueryBuilder.ALL)
                .from(this.getTableName())
                .where(new JDBCWhereClauseBuilder()
                        .where(this.formatColumnFromName("name"), Operation.LIKE, ":name", ColumnTransformer.LOWER)
                        .and()
                        .where(this.formatColumnFromName("country_id"), Operation.EQ, ":country")
                );

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("country", country.getId());
        parameterSource.addValue("name", name);

        return this.query(queryBuilder.getQueryAsString(), parameterSource);
    }

    @Override
    protected RowMapper<Province> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    protected String getTableName() {
        return "system_province";
    }

    @Override
    protected String getIdColumnName() {
        return "province_id";
    }

    @Override
    protected Map<String, Pair<String, Object>> getModelColumnsArgumentValue(Province model) {
        Map<String, Pair<String, Object>> map = new HashMap<>();

        map.put("office_id", new Pair<>(":office_id", model.getId()));
        map.put("province_id", new Pair<>(":province_id", model.getCountry().getId()));
        map.put("name", new Pair<>(":name", model.getName()));

        return map;
    }
}
