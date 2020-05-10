package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.services.generic.GenericService;
import ar.edu.itba.paw.models.Picture;
import ar.edu.itba.paw.models.User;

import java.util.List;

public interface PictureService extends GenericService<Picture, Integer> {
    List<Picture> findByUser(User user);
}
