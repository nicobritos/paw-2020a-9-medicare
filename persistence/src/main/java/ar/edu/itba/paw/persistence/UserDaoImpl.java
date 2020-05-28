package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.User_;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDaoImpl extends GenericSearchableDaoImpl<User, Integer> implements UserDao {
    public UserDaoImpl() {
        super(User.class, User_.id);
    }

    @Override
    public boolean existsEmail(String email) {
        Map<SingularAttribute<? super User, ?>, Object> parametersValues = new HashMap<>();
        parametersValues.put(User_.email, email);
        return this.exists(parametersValues);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if(email == null){
            throw new IllegalArgumentException();
        }
        return this.findBy(User_.email, email).stream().findFirst();
    }

    @Override
    public boolean existsToken(String token) {
        Map<SingularAttribute<? super User, ?>, Object> parametersValues = new HashMap<>();
        parametersValues.put(User_.token, token);
        return this.exists(parametersValues);
    }

    @Override
    public Optional<User> findByToken(String token) {
        if (token == null){
            throw new IllegalArgumentException();
        }
        return this.findBy(User_.token, token).stream().findFirst();
    }

    @Override
    protected SingularAttribute<? super User, ?> getNameAttribute() {
        return User_.firstName;
    }

    @Override
    protected void insertOrderBy(CriteriaBuilder builder, CriteriaQuery<User> query, Root<User> root) {
    }
}
