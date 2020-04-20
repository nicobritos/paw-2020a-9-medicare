package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.UserDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.generics.GenericSearchableServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl extends GenericSearchableServiceImpl<UserDao, User, Integer> implements UserService {
    @Autowired
    private UserDao repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {
        if (this.repository.existsEmail(user.getEmail())) {
//            throw new EmailAlreadyExistsException();
            throw new RuntimeException("Email ya existe");
        }

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return super.create(user);
    }

    @Override
    public Optional<User> login(String email, String password) {
        Optional<User> user = this.repository.findByEmail(email);
        if (!user.isPresent())
            return user;
        if (!this.passwordEncoder.matches(user.get().getPassword(), password))
            return Optional.empty();
        return user;
    }

    @Override
    protected UserDao getRepository() {
        return this.repository;
    }
}
