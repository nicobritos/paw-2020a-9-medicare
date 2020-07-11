package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericService;
import ar.edu.itba.paw.models.*;

import java.util.Collection;
import java.util.List;

public interface StaffService extends GenericService<Staff, Integer> {
    List<Staff> findBy(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities);

    List<Staff> findBy(Collection<String> names, Collection<String> surnames, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities);

    Paginator<Staff> findBy(String name, String surname, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities, int page, int perPage);

    Paginator<Staff> findBy(Collection<String> names, Collection<String> surnames, Collection<Office> offices, Collection<StaffSpecialty> staffSpecialties, Collection<Locality> localities, int page, int perPage);

    List<Staff> findByUser(User user);
}
