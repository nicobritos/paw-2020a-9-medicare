package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MediCareException;
import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.interfaces.services.OfficeService;
import ar.edu.itba.paw.interfaces.services.PatientService;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.generics.GenericSearchableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl extends GenericSearchableServiceImpl<UserDao, User, Integer> implements UserService {
    @Autowired
    private UserDao repository;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User create(User user) throws EmailAlreadyExistsException {
        if (this.repository.existsEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return super.create(user);
    }

    @Override
    public boolean isStaff(User user) {
        return !this.staffService.findByUser(user.getId()).isEmpty();
    }

    @Override
    @Transactional
    public Patient createNewPatient(Patient patient) throws EmailAlreadyExistsException {
        return this.patientService.create(patient);
    }

    @Override
    @Transactional
    public User createAsStaff(User user, Office office) throws EmailAlreadyExistsException {
        User newUser = this.create(user);
        Optional<Office> officeOptional = this.officeService.findById(office.getId());
        if (!officeOptional.isPresent())
            office = this.officeService.create(office);
        else
            office = officeOptional.get();

        Staff staff = new Staff();
        staff.setEmail(newUser.getEmail());
        staff.setFirstName(newUser.getFirstName());
        staff.setSurname(newUser.getSurname());
        staff.setUser(user);
        staff.setOffice(office);
        this.staffService.create(staff);

        return newUser;
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(this.passwordEncoder.encode(newPassword));
        super.update(user);
    }

    @Override
    @Transactional
    public void update(User user){
        Optional<User> userOptional = this.repository.findByEmail(user.getEmail());
        if (userOptional.isPresent() && !userOptional.get().equals(user)) {
            throw new EmailAlreadyExistsException();
        }
        if (this.repository.existsToken(user.getToken()))
            throw new MediCareException("Confirmation token already exists");

        super.update(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.repository.findByEmail(username);
    }

    @Override
    @Transactional
    public String generateVerificationToken(User user) {
        boolean set = false;
        int tries = 10;
        do {
            try {
                user.setToken(UUID.randomUUID().toString());
                this.update(user);
                set = true;
            } catch (MediCareException ignored) {
                tries--;
            }
        } while (!set && tries > 0);
        if (!set)
            throw new MediCareException("");

        return user.getToken();
    }

    @Override
    protected UserDao getRepository() {
        return this.repository;
    }
}
