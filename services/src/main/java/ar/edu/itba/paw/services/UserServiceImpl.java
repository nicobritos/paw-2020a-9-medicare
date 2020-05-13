package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MediCareException;
import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.generics.GenericSearchableServiceImpl;
import org.joda.time.DateTime;
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
    private PictureService pictureService;
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
        staff.setUser(newUser);
        staff.setOffice(office);
        this.staffService.create(staff);

        return newUser;
    }

    @Override
    @Transactional
    public void setProfile(User user, Picture picture) {
        if (user.getProfileId() == null) {
            picture = this.pictureService.create(picture);
            user.setProfileId(picture.getId());
            this.update(user);
        } else {
            picture.setId(user.getProfileId());
            this.pictureService.update(picture);
        }
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
        if (userOptional.isPresent()) {
            if (!userOptional.get().equals(user)) {
                throw new EmailAlreadyExistsException();
            }
            if (!userOptional.get().equals(user) && this.repository.existsToken(user.getToken())) {
                throw new MediCareException("Confirmation token already exists");
            }
        } else {
            // Already exists in DB, the email has changed
            if (user.getId() != null && this.findById(user.getId()).isPresent()) {
                user.setVerified(false);
                user.setToken(null);
            } else {
                Optional<User> userToken = this.repository.findByToken(user.getToken());
                if (userToken.isPresent() && !userToken.get().equals(user)){
                    throw new MediCareException("Confirmation token already exists");
                }
            }
        }

        super.update(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.repository.findByEmail(username);
    }

    @Override
    public Optional<User> findByToken(String token){
        return this.repository.findByToken(token);
    }

    @Override
    @Transactional
    public String generateVerificationToken(User user) {
        if (user.getVerified())
            return null;
        if (user.getToken() != null) {
            user.setTokenCreatedDate(DateTime.now());
            this.update(user);
            return user.getToken();
        }

        boolean set = false;
        int tries = 10;
        user.setVerified(false);
        do {
            try {
                user.setToken(UUID.randomUUID().toString());
                user.setTokenCreatedDate(DateTime.now());
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
    @Transactional
    public boolean confirm(User user, String token) {
        if(user.getVerified()){
            return false;
        }
        if(user.getToken() != null && user.getToken().equals(token)){
            user.setVerified(true);
            user.setToken(null);
            user.setTokenCreatedDate(null);
            this.update(user);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    protected UserDao getRepository() {
        return this.repository;
    }
}
