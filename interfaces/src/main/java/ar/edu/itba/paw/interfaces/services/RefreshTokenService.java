package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericService;
import ar.edu.itba.paw.models.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService extends GenericService<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    RefreshToken generate();

    String refresh(RefreshToken refreshToken);
}
