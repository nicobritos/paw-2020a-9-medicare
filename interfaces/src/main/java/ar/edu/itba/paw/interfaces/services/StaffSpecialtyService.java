package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableService;
import ar.edu.itba.paw.models.StaffSpecialty;
import ar.edu.itba.paw.models.User;

public interface StaffSpecialtyService extends GenericSearchableService<StaffSpecialty, Integer> {
    void addToUser(int staffSpecialty, User user);

    void removeFromUser(int staffSpecialty, User user);
}
