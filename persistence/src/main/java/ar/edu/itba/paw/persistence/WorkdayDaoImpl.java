package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.WorkdayDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.generics.GenericDaoImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WorkdayDaoImpl extends GenericDaoImpl<Workday, Integer> implements WorkdayDao {
    public WorkdayDaoImpl() {
        super(Workday.class, Workday_.id);
    }

    @Override
    public List<Workday> findByUser(User user) {
        if (user == null){
            throw new IllegalArgumentException();
        }
        CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Workday> query = builder.createQuery(Workday.class);
        Root<Workday> root = query.from(Workday.class);
        Join<Workday, Staff> staffJoin = root.join(Workday_.staff);
        Join<Staff, User> userJoin = staffJoin.join(Staff_.user);

        query.select(root);
        query.where(builder.equal(userJoin.get(User_.id), user.getId()));

        return this.getEntityManager().createQuery(query).getResultList();
    }

    @Override
    public List<Workday> findByStaff(Staff staff) {
        if (staff == null){
            throw new IllegalArgumentException();
        }
        return this.findBy(Workday_.staff, staff);
    }

    @Override
    public List<Workday> findByStaff(Staff staff, WorkdayDay day) {
        if(staff == null || day == null){
            throw new IllegalArgumentException();
        }
        Map<SingularAttribute<? super Workday, ?>, Object> parametersValues = new HashMap<>();
        parametersValues.put(Workday_.staff, staff);
        parametersValues.put(Workday_.day, day);
        return this.findBy(parametersValues);
    }

    @Override
    public boolean isStaffWorking(Staff staff, AppointmentTimeSlot timeSlot) {
        if (staff == null || timeSlot == null)
            throw new IllegalArgumentException();

        CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createQuery(Tuple.class);
        Root<Workday> root = query.from(Workday.class);

        Predicate[] predicates = new Predicate[4];
        predicates[0] = builder.equal(root.get(Workday_.staff), staff);
        predicates[1] = builder.equal(root.get(Workday_.day), WorkdayDay.from(timeSlot.getDate()));
        predicates[2] = builder.le(root.get(Workday_.startHour), timeSlot.getDate().getHourOfDay());
        if (timeSlot.getToDate().getMinuteOfHour() == 0) {
            predicates[3] = builder.ge(root.get(Workday_.endHour), timeSlot.getToDate().getHourOfDay());
        } else {
            predicates[3] = builder.gt(root.get(Workday_.endHour), timeSlot.getToDate().getHourOfDay());
        }

        query.where(builder.and(predicates));

        return this.exists(builder, query, root);
    }


    @Override
    protected void insertOrderBy(CriteriaBuilder builder, CriteriaQuery<Workday> query, Root<Workday> root) {
    }
}
