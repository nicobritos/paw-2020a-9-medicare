package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.builder.JDBCWhereClauseBuilder.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDaoImpl extends GenericSearchableDaoImpl<User, Integer> implements UserDao {
    private final RowMapper<User> rowMapper = (resultSet, rowNum) -> this.hydrate(resultSet);
    public static final String TABLE_NAME = getTableNameFromModel(User.class);
    public static final String PRIMARY_KEY_NAME = getPrimaryKeyNameFromModel(User.class);

    @Autowired
    public UserDaoImpl(DataSource dataSource) {
        super(dataSource, User.class, Integer.class);
    }

    @Override
    public boolean existsEmail(String email) {
        Map<String, String> values = new HashMap<>();
        values.put("email", email);
        return this.exists(values);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.findByField("email", Operation.EQ, email, user -> user.getEmail().equals(email)).stream().findFirst();
    }

    @Override
    protected RowMapper<User> getRowMapper() {
        return this.rowMapper;
    }
}
