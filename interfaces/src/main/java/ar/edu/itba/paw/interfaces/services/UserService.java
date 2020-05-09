package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.services.generic.GenericSearchableService;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserService extends GenericSearchableService<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByToken(String token);

    boolean isStaff(User user);

    User createAsStaff(User user, Office office) throws EmailAlreadyExistsException;

    void updatePassword(User user, String newPassword);

    void update(User user) throws EmailAlreadyExistsException;

    Patient createNewPatient(Patient patient);

    String generateVerificationToken(User user);

    boolean confirm(User user, String token);
}
