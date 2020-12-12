package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.RefreshTokenDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.generics.GenericDaoImpl;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@Repository
public class RefreshTokenDaoImpl extends GenericDaoImpl<RefreshToken, Integer> implements RefreshTokenDao {
    public RefreshTokenDaoImpl() {
        super(RefreshToken.class, RefreshToken_.id);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException();
        }
        return this.findBy(RefreshToken_.token, token).stream().findFirst();
    }

    @Override
    @Transactional
    public void removeByToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException();
        }

        Optional<RefreshToken> refreshToken = this.findByToken(token);
        refreshToken.ifPresent(value -> this.getEntityManager().remove(value));
    }

    @Override
    @Transactional
    public int removeTokensOlderThan(int days) {
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(days);

        CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        CriteriaDelete<RefreshToken> delete = builder.createCriteriaDelete(RefreshToken.class);
        Root<RefreshToken> root = delete.from(RefreshToken.class);

        delete.where(builder.greaterThanOrEqualTo(root.get(RefreshToken_.createdDate), expiryDate));
        return getEntityManager().createQuery(delete).executeUpdate();
    }

    @Override
    protected void insertOrderBy(CriteriaBuilder builder, CriteriaQuery<RefreshToken> query, Root<RefreshToken> root) {
    }
}
