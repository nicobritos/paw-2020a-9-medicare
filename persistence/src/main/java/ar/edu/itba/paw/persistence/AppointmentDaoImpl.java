package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.AppointmentDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.generics.GenericDaoImpl;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.*;

@Repository
public class AppointmentDaoImpl extends GenericDaoImpl<Appointment, Integer> implements AppointmentDao {
    public AppointmentDaoImpl() {
        super(Appointment.class, Appointment_.id);
    }

    @Override
    public List<Appointment> find(Patient patient) {
        return this.findBy(Appointment_.patient, patient);
    }

    @Override
    public List<Appointment> findByPatients(List<Patient> patients) {
        return this.findByIn(Appointment_.patient, patients);
    }

    @Override
    public List<Appointment> find(Staff staff) {
        return this.findBy(Appointment_.staff, staff);
    }

    @Override
    public List<Appointment> findByStaffs(List<Staff> staffs) {
        return this.findByIn(Appointment_.staff, staffs);
    }

    @Override
    public List<Appointment> findPending(Patient patient) {
        Map<SingularAttribute<? super Appointment, ?>, Object> parameters = new HashMap<>();
        parameters.put(Appointment_.patient, patient);
        parameters.put(Appointment_.appointmentStatus, AppointmentStatus.PENDING);
        return this.findBy(parameters);
    }

    @Override
    public List<Appointment> findPending(Staff staff) {
        Map<SingularAttribute<? super Appointment, ?>, Object> parameters = new HashMap<>();
        parameters.put(Appointment_.staff, staff);
        parameters.put(Appointment_.appointmentStatus, AppointmentStatus.PENDING);
        return this.findBy(parameters);
    }

    @Override
    public List<Appointment> findPending(Patient patient, Staff staff) {
        Map<SingularAttribute<? super Appointment, ?>, Object> parameters = new HashMap<>();
        parameters.put(Appointment_.patient, patient);
        parameters.put(Appointment_.staff, staff);
        parameters.put(Appointment_.appointmentStatus, AppointmentStatus.PENDING);
        return this.findBy(parameters);
    }

    @Override
    public List<Appointment> findByStaffsAndDate(Collection<Staff> staffs, DateTime date) {
        DateTime fromDate = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 0, 0);
        DateTime toDate = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 23, 59);
        return this.findByStaffsAndDate(staffs, fromDate, toDate);
    }

    @Override
    public List<Appointment> findByStaffsAndDate(Collection<Staff> staffs, DateTime fromDate, DateTime toDate) {
        if (staffs.isEmpty())
            return Collections.emptyList();

        CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Appointment> query = builder.createQuery(Appointment.class);
        Root<Appointment> root = query.from(Appointment.class);

        query.select(root);
        Path<?> expression = root.get(Appointment_.staff);
        Predicate predicate = expression.in(staffs);
        query.where(builder.and(
                predicate,
                builder.between(
                        root.get(Appointment_.fromDate),
                        fromDate,
                        toDate
                )
        ));

        return this.selectQuery(builder, query, root);
    }

    @Override
    public List<Appointment> findByPatientsAndDate(Collection<Patient> patients, DateTime date) {
        if (patients.isEmpty())
            return Collections.emptyList();

        CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Appointment> query = builder.createQuery(Appointment.class);
        Root<Appointment> root = query.from(Appointment.class);

        DateTime fromDate = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 0, 0);
        DateTime toDate = new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 23, 59);

        query.select(root);
        Path<?> expression = root.get(Appointment_.patient);
        Predicate predicate = expression.in(patients);
        query.where(builder.and(
                predicate,
                builder.between(
                        root.get(Appointment_.fromDate),
                        fromDate,
                        toDate
                )
        ));

        return this.selectQuery(builder, query, root);
    }

    @Override
    public List<Appointment> findByDate(Patient patient, DateTime date) {
        List<Patient> patients = new LinkedList<>();
        patients.add(patient);
        return this.findByPatientsAndDate(patients, date);
    }

    @Override
    public List<Appointment> findByPatientsFromDate(Collection<Patient> patients, DateTime from) {
        if (patients.isEmpty())
            return Collections.emptyList();

        CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Appointment> query = builder.createQuery(Appointment.class);
        Root<Appointment> root = query.from(Appointment.class);

        query.select(root);
        Path<?> expression = root.get(Appointment_.patient);
        Predicate predicate = expression.in(patients);
        query.where(builder.and(
                predicate,
                builder.greaterThanOrEqualTo(root.get(Appointment_.fromDate), from)
        ));

        return this.selectQuery(builder, query, root);
    }

    @Override
    protected void insertOrderBy(CriteriaBuilder builder, CriteriaQuery<Appointment> query, Root<Appointment> root) {
        query.orderBy(builder.asc(root.get(Appointment_.fromDate)));
    }
}
