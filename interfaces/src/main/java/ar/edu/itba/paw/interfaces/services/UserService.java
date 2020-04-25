package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableService;
import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserService extends GenericSearchableService<User, Integer> {
    Optional<User> login(String email, String password);

    Optional<User> findByUsername(String username);
}
