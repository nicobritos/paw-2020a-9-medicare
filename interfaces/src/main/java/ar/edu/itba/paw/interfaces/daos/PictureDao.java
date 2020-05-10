package ar.edu.itba.paw.interfaces.daos;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.Picture;
import ar.edu.itba.paw.models.User;

import java.util.List;

public interface PictureDao extends GenericDao<Picture, Integer> {
    List<Picture> findByUser(User user);
}
