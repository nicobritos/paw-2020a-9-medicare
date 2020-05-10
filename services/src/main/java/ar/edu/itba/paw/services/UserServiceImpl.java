package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.interfaces.services.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.generics.GenericSearchableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        Patient newPatient = this.patientService.create(patient);
        // TODO
//        Office office = patient.getOffice();
//        office.getPatients().add(newPatient);
//        this.officeService.update(office);
//        User user = patient.getUser();
//        user.getPatients().add(newPatient);
//        this.update(user);
        return newPatient;
    }

    @Override
    @Transactional
    public User createAsStaff(User user, Office office) throws EmailAlreadyExistsException {
        User newUser;
        newUser = this.create(user);

        office = this.officeService.create(office);

        Staff staff = new Staff();
        staff.setEmail(newUser.getEmail());
        staff.setFirstName(newUser.getFirstName());
        staff.setSurname(newUser.getSurname());
        staff.setUser(newUser);
        staff.setOffice(office);
        staff = this.staffService.create(staff);

        return newUser;
    }

    @Override
    @Transactional
    public void setProfile(User user, Picture picture) {
        // We DON'T reuse profile pictures
        picture = this.pictureService.create(picture);
        if (user.getProfileId() != null) {
            // As we don't reuse them, we can safely delete the old one
            this.pictureService.remove(picture.getId());
        }
        user.setProfileId(picture.getId());
        this.update(user);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(this.passwordEncoder.encode(newPassword));
        super.update(user);
    }

    @Override
    public void update(User user){
        Optional<User> userOptional = this.repository.findByEmail(user.getEmail());
        if (userOptional.isPresent() && !userOptional.get().equals(user)) {
            throw new EmailAlreadyExistsException();
        }

        super.update(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.repository.findByEmail(username);
    }

    @Override
    protected UserDao getRepository() {
        return this.repository;
    }
}
