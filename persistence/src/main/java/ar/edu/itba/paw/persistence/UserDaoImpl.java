package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.JDBCArgumentValue;
import ar.edu.itba.paw.persistence.utils.RowMapperAlias;
import ar.edu.itba.paw.persistence.utils.builder.JDBCSelectQueryBuilder;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserDaoImpl extends GenericSearchableDaoImpl<User, Integer> implements UserDao {
    public static final RowMapperAlias<User> ROW_MAPPER = (prefix, resultSet) -> {
        User user = new User();
        try {
            user.setId(resultSet.getInt(formatColumnFromName(UserDaoImpl.PRIMARY_KEY_NAME, prefix)));
        } catch (SQLException e) {
            user.setId(resultSet.getInt(UserDaoImpl.PRIMARY_KEY_NAME));
        }
        populateEntity(user, resultSet, prefix);
        return user;
    };
    public static final String TABLE_NAME = getTableNameFromModel(User.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(User.class);

    @Autowired
    public UserDaoImpl(DataSource dataSource) {
        super(dataSource, User.class);
    }

    @Override
    public boolean existsEmail(String email) {
        Map<String, String> values = new HashMap<>();
        values.put("email", email);
        return this.exists(values);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.findByField("email", Operation.EQ, email).stream().findFirst();
    }

    @Override
    protected ResultSetExtractor<List<User>> getResultSetExtractor() {
        return resultSet -> {
            Map<Integer, User> entityMap = new HashMap<>();
            List<User> sortedEntities = new LinkedList<>();
            while (resultSet.next()) {
                int id;
                try {
                    id = resultSet.getInt(this.formatColumnFromAlias(this.getIdColumnName()));
                } catch (SQLException e) {
                    id = resultSet.getInt(this.getIdColumnName());
                }
                if (resultSet.wasNull())
                    continue;
                entityMap.computeIfAbsent(id, string -> {
                    try {
                        User newEntity = ROW_MAPPER.mapRow(this.getTableAlias(), resultSet);
                        sortedEntities.add(newEntity);
                        return newEntity;
                    } catch (SQLException e) {
                        return null;
                    }
                });
            }
            return sortedEntities;
        };
    }

    @Override
    protected void populateJoins(JDBCSelectQueryBuilder selectQueryBuilder) {

    }

    @Override
    protected Map<String, JDBCArgumentValue> getModelRelationsArgumentValue(User model, String prefix) {
        return null;
    }
}
