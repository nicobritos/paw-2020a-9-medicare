package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MediCareException;
import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.interfaces.services.OfficeService;
import ar.edu.itba.paw.interfaces.services.StaffService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Office;
import ar.edu.itba.paw.models.Staff;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.exceptions.EmailAlreadyExistsException;
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
    private PasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {
        if (this.repository.existsEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return super.create(user);
    }

    @Override
    @Transactional
    public User create(User user, Office office){
        User newUser;
        newUser = create(user);

        office = this.officeService.create(office);

        Staff staff = new Staff();
        staff.setEmail(newUser.getEmail());
        staff.setFirstName(newUser.getFirstName());
        staff.setSurname(newUser.getSurname());
        staff = this.staffService.create(staff);

        office.getStaffs().add(staff);
        this.officeService.update(office);

        newUser.getStaffs().add(staff);
        update(newUser);

        return newUser;
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
