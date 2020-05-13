package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.daos.PictureDao;
import ar.edu.itba.paw.interfaces.services.PictureService;
import ar.edu.itba.paw.models.Picture;
import ar.edu.itba.paw.services.generics.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PictureServiceImpl extends GenericServiceImpl<PictureDao, Picture, Integer> implements PictureService {
    @Autowired
    private PictureDao repository;

    @Override
    protected PictureDao getRepository() {
        return this.repository;
    }
}
