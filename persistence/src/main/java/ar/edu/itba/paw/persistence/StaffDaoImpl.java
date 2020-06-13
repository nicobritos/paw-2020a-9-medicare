package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.daos.StaffDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.generics.GenericDaoImpl;
import ar.edu.itba.paw.persistence.utils.StringSearchType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StaffDaoImpl extends GenericDaoImpl<Staff, Integer> implements StaffDao {
    private static final int DEFAULT_PAGE_SIZE = 10;

    public StaffDaoImpl() {
        super(Staff.class, Staff_.id);
    }

    @Override
    public List<Staff> findBy(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities) {
        Collection<String> names;
        Collection<String> surnames;
        if (name != null && !name.isEmpty()) {
            names = new LinkedList<>();
            names.add(name);
        } else {
            names = Collections.emptyList();
        }
        if (surname != null && !surname.isEmpty()) {
            surnames = new LinkedList<>();
            surnames.add(surname);
        } else {
            surnames = Collections.emptyList();
        }

        return this.findBy(names, surnames, offices, staffSpecialties, localities);
    }

    @Override
    public List<Staff> findBy(Collection<String> names, Collection<String> surnames, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities) {
        if (names == null) {
            names = Collections.emptyList();
        } else {
            names = names.stream().map(name -> StringUtils.stripAccents(name).toLowerCase()).collect(Collectors.toList());
        }
        if (surnames == null) {
            surnames = Collections.emptyList();
        } else {
            surnames = surnames.stream().map(surname -> StringUtils.stripAccents(surname).toLowerCase()).collect(Collectors.toList());
        }
        if (offices == null) {
            offices = Collections.emptyList();
        }
        if (staffSpecialties == null) {
            staffSpecialties = Collections.emptyList();
        }
        if (localities == null) {
            localities = Collections.emptyList();
        }

        CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Staff> query = builder.createQuery(Staff.class);
        Root<Staff> root = query.from(Staff.class);
        Join<Staff, Office> officeJoin = root.join(Staff_.office);
        Join<Staff, User> userJoin = root.join(Staff_.user);

        query.select(root);
        query.where(builder.and(this.getPredicates(
                names,
                surnames,
                offices,
                staffSpecialties,
                localities,
                builder,
                root,
                officeJoin,
                userJoin
        )));

        return this.selectQuery(builder, query, root);
    }

    @Override
    public Paginator<Staff> findBy(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities, int page, int pageSize) {
        Collection<String> names;
        Collection<String> surnames;
        if (name != null && !name.isEmpty()) {
            names = new LinkedList<>();
            names.add(name);
        } else {
            names = Collections.emptyList();
        }
        if (surname != null && !surname.isEmpty()) {
            surnames = new LinkedList<>();
            surnames.add(surname);
        } else {
            surnames = Collections.emptyList();
        }

        return this.findBy(names, surnames, offices, staffSpecialties, localities, page, pageSize);
    }

    @Override
    public Paginator<Staff> findBy(Collection<String> names, Collection<String> surnames, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities, int page, int pageSize) {
        if (page <= 0) page = 1;
        if (pageSize <= 0) pageSize = DEFAULT_PAGE_SIZE;

        if (names == null) {
            names = Collections.emptyList();
        } else {
            names = names.stream().map(name -> StringUtils.stripAccents(name).toLowerCase()).collect(Collectors.toList());
        }
        if (surnames == null) {
            surnames = Collections.emptyList();
        } else {
            surnames = surnames.stream().map(surname -> StringUtils.stripAccents(surname).toLowerCase()).collect(Collectors.toList());
        }
        if (offices == null) {
            offices = Collections.emptyList();
        }
        if (staffSpecialties == null) {
            staffSpecialties = Collections.emptyList();
        }
        if (localities == null) {
            localities = Collections.emptyList();
        }

        CriteriaBuilder builder = this.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Staff> query = builder.createQuery(Staff.class);
        Root<Staff> root = query.from(Staff.class);
        Join<Staff, Office> officeJoin = root.join(Staff_.office);
        Join<Staff, User> userJoin = root.join(Staff_.user);

        CriteriaQuery<Tuple> tupleQuery = builder.createQuery(Tuple.class);
        Root<Staff> rootCount = tupleQuery.from(Staff.class);
        Join<Staff, Office> officeJoinCount = rootCount.join(Staff_.office);
        Join<Staff, User> userJoinCount = rootCount.join(Staff_.user);

        query.select(root);
        query.where(builder.and(this.getPredicates(
                names,
                surnames,
                offices,
                staffSpecialties,
                localities,
                builder,
                root,
                officeJoin,
                userJoin
        )));

        tupleQuery.where(builder.and(this.getPredicates(
                names,
                surnames,
                offices,
                staffSpecialties,
                localities,
                builder,
                rootCount,
                officeJoinCount,
                userJoinCount
        )));
        tupleQuery.distinct(true);

        return this.selectQuery(builder, query, tupleQuery, root, page, pageSize);
    }

    @Override
    public List<Staff> findByUser(User user) {
        return this.findBy(Staff_.user, user);
    }

    @Override
    protected void insertOrderBy(CriteriaBuilder builder, CriteriaQuery<Staff> query, Root<Staff> root) {
        query.orderBy(builder.asc(root.get(Staff_.user).get(User_.firstName)), builder.asc(root.get(Staff_.user).get(User_.surname)));
    }

    private Predicate[] getPredicates(Collection<String> names,
                                      Collection<String> surnames,
                                      Collection<Office> offices,
                                      Collection<StaffSpecialty> staffSpecialties,
                                      Collection<Locality> localities,
                                      CriteriaBuilder builder,
                                      Root<Staff> root,
                                      Join<Staff, Office> officeJoin,
                                      Join<Staff, User> userJoin
    ) {
        List<Predicate> predicates = new LinkedList<>();
        Predicate predicate;
        predicate = this.getNamePredicate(names, surnames, builder, userJoin);
        if (predicate != null) predicates.add(predicate);
        predicate = this.getOfficePredicate(offices, root);
        if (predicate != null) predicates.add(predicate);
        predicate = this.getStaffSpecialtyPredicate(staffSpecialties, builder, root);
        if (predicate != null) predicates.add(predicate);
        predicate = this.getLocalityPredicate(localities, officeJoin);
        if (predicate != null) predicates.add(predicate);

        Predicate[] predicatesArrayCount = new Predicate[predicates.size()];
        return predicates.toArray(predicatesArrayCount);
    }

    private Predicate getLocalityPredicate(Collection<Locality> localities, Join<Staff, Office> officeJoin) {
        if (localities.isEmpty())
            return null;

        Path<?> expression = officeJoin.get(Office_.locality);
        return expression.in(localities);
    }

    private Predicate getStaffSpecialtyPredicate(Collection<StaffSpecialty> staffSpecialties, CriteriaBuilder builder, Root<Staff> root) {
        if (staffSpecialties.isEmpty())
            return null;

        List<Predicate> predicates = new LinkedList<>();
        Expression<Collection<StaffSpecialty>> expression = root.get(Staff_.staffSpecialties);

        for (StaffSpecialty staffSpecialty : staffSpecialties) {
            predicates.add(builder.isMember(staffSpecialty, expression));
        }

        Predicate[] predicatesArray = new Predicate[predicates.size()];
        predicatesArray = predicates.toArray(predicatesArray);
        return builder.or(predicatesArray);
    }

    private Predicate getOfficePredicate(Collection<Office> offices, Root<Staff> root) {
        if (offices.isEmpty())
            return null;

        Path<?> expression = root.get(Staff_.office);
        return expression.in(offices);
    }

    private Predicate getNamePredicate(Collection<String> firstNames, Collection<String> surnames, CriteriaBuilder builder, Join<Staff, User> userJoin) {
        if (firstNames.isEmpty() && surnames.isEmpty())
            return null;

        List<Predicate> predicates = new LinkedList<>();

        Expression<String> expression = userJoin.get(User_.firstName).as(String.class);
        for (String name : firstNames) {
            if (name.isEmpty()) continue;
            predicates.add(
                    builder.like(
                            builder.lower(expression),
                            StringSearchType.CONTAINS_NO_ACC.transform(name.toLowerCase())
                    )
            );
        }

        expression = userJoin.get(User_.surname).as(String.class);
        for (String name : surnames) {
            if (name.isEmpty()) continue;
            predicates.add(
                    builder.like(
                            builder.lower(expression),
                            StringSearchType.CONTAINS_NO_ACC.transform(name.toLowerCase())
                    )
            );
        }

        Predicate[] predicatesArray = new Predicate[predicates.size()];
        predicatesArray = predicates.toArray(predicatesArray);
        return builder.or(predicatesArray);
    }
}