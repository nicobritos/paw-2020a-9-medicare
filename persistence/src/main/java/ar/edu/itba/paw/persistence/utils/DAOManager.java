package ar.edu.itba.paw.persistence.utils;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.GenericModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DAOManager {
    private static Map<Class<?>, GenericDao<?, Object>> daosMap = new HashMap<>();

    @Autowired
    private void initDaos(List<GenericDao> genericDaos) {
        for (GenericDao genericDao : genericDaos) {
            daosMap.put(genericDao.getModelClass(), genericDao);
        }
    }

    public static <DAO extends GenericDao<M, I>, M extends GenericModel<I>, I> DAO getDaoForModel(Class<M> mClass) {
        return (DAO) daosMap.get(mClass);
    }
}
