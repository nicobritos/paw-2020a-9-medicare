package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.StaffSpecialtyDao;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.models.StaffSpecialty_;
import ar.edu.itba.paw.persistence.generics.GenericSearchableDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

@Repository
public class StaffSpecialtyDaoImpl extends GenericSearchableDaoImpl<StaffSpecialty, Integer> implements StaffSpecialtyDao {
    public StaffSpecialtyDaoImpl() {
        super(StaffSpecialty.class, StaffSpecialty_.id);
    }

    @Override
    protected SingularAttribute<? super StaffSpecialty, ?> getNameAttribute() {
        return StaffSpecialty_.name;
    }

    @Override
    protected void insertOrderBy(CriteriaBuilder builder, CriteriaQuery<StaffSpecialty> query, Root<StaffSpecialty> root) {
        query.orderBy(builder.asc(root.get(StaffSpecialty_.name)));
    }
}
