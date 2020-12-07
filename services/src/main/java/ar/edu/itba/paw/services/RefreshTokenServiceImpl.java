package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.MediCareException;
import ar.edu.itba.paw.interfaces.daos.RefreshTokenDao;
import ar.edu.itba.paw.interfaces.services.RefreshTokenService;
import ar.edu.itba.paw.models.RefreshToken;
import ar.edu.itba.paw.services.generics.GenericServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;

@Service
public class RefreshTokenServiceImpl extends GenericServiceImpl<RefreshTokenDao, RefreshToken, Integer> implements RefreshTokenService {
    private static final int RANDOM_LENGTH = 256;

    @Autowired
    private RefreshTokenDao repository;

    public RefreshTokenServiceImpl() {
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return this.repository.findByToken(token);
    }

    // Should not be able to manually update
    @Override
    public void update(RefreshToken model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RefreshToken generate() {
        RefreshToken refreshToken = new RefreshToken();
        this.generateAndSaveToken(refreshToken, rt -> {
            rt = this.create(rt);
            refreshToken.setId(rt.getId());
        });
        return refreshToken;
    }

    @Override
    public String refresh(RefreshToken refreshToken) {
        this.generateAndSaveToken(refreshToken, this::secureUpdate);
        return refreshToken.getToken();
    }

    @Override
    public void removeByToken(String token) {
        this.repository.removeByToken(token);
    }

    @Override
    protected RefreshTokenDao getRepository() {
        return this.repository;
    }

    private void secureUpdate(RefreshToken refreshToken) {
        super.update(refreshToken);
    }

    private void generateAndSaveToken(RefreshToken refreshToken, Consumer<RefreshToken> saver) {
        boolean set = false;
        int tries = 10;

        do {
            try {
                refreshToken.setToken(RandomStringUtils.random(RANDOM_LENGTH, true, false));
                refreshToken.setCreatedDate(DateTime.now());
                saver.accept(refreshToken);
                set = true;
            } catch (MediCareException ignored) {
                tries--;
            }
        } while (!set && tries > 0);

        if (!set)
            throw new MediCareException("");
    }
}
