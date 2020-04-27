package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableService;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserService extends GenericSearchableService<User, Integer> {
    Optional<User> findByUsername(String username);

    User create(User user, Office office);
}
