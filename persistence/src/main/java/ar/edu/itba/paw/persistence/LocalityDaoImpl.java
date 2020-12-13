package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.LocalityDao;
import ar.edu.itba.paw.models.Locality;
import ar.edu.itba.paw.models.Locality_;
import ar.edu.itba.paw.models.Province;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import ar.edu.itba.paw.persistence.utils.StringSearchType;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

@Repository
public class LocalityDaoImpl extends GenericSearchableDaoImpl<Locality, Integer> implements LocalityDao {
    public LocalityDaoImpl() {
        super(Locality.class, Locality_.id);
    }

    @Override
    public List<Locality> findByProvince(Province province) {
        return this.findBy(Locality_.province, province);
    }

    @Override
    public List<Locality> findByProvinceAndName(Province province, String name) {
        if (province == null || name == null) {
            throw new IllegalArgumentException();
        }
        CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Locality> query = builder.createQuery(Locality.class);
        Root<Locality> root = query.from(Locality.class);

        name = name.replace("%", "\\%");
        name = name.replace("_", "\\%");

        query.select(root);
        query.where(
                builder.and(
                        builder.like(
                                builder.lower(root.get(Locality_.name).as(String.class)),
                                StringSearchType.CONTAINS_NO_ACC.transform(name.toLowerCase())
                        ),
                        builder.equal(root.get(Locality_.province), province)
                )
        );

        return this.getEntityManager().createQuery(query).getResultList();
    }

    @Override
    protected SingularAttribute<? super Locality, ?> getNameAttribute() {
        return Locality_.name;
    }

    @Override
    protected void insertOrderBy(CriteriaBuilder builder, CriteriaQuery<Locality> query, Root<Locality> root) {
        query.orderBy(builder.asc(root.get(Locality_.name)));
    }
}
