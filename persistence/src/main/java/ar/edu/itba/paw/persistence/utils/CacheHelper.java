package ar.edu.itba.paw.persistence.utils;

import ar.edu.itba.paw.models.GenericModel;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CacheHelper {
    private static final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
    private static final Map<Class<?>, Integer> sizes = new HashMap<>();
    static {
        cacheManager.init();
    }

    public static synchronized <M extends GenericModel<M, I>, I> void clean(Class<M> mClass, Class<I> iClass){
        cacheManager.removeCache(mClass.getName());
        sizes.put(mClass, 0);
        cacheManager.createCache(
                mClass.getName(),
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        iClass,
                        mClass,
                        ResourcePoolsBuilder.heap(500)
                )
        );
    }

        public static synchronized <M extends GenericModel<M, I>, I> int getSize(Class<M> mClass) {
        return sizes.getOrDefault(mClass, 0);
    }

    public static synchronized <M extends GenericModel<M, I>, I> boolean containsAllInstances(Class<M> mClass, Class<I> iClass) {
        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache == null)
            return false;

        int currentSize = 0;
        for (Cache.Entry<I, M> entry : cache) {
            currentSize++;
        }
        return currentSize == sizes.get(mClass);
    }

    public static synchronized <M extends GenericModel<M, I>, I> M get(Class<M> mClass, Class<I> iClass, I key) {
        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache == null) {
            createCache(mClass, iClass);
            return null;
        }
        M model = cache.get(key);
        if (model != null)
            return model.copy();
        return null;
    }

    public static synchronized <M extends GenericModel<M, I>, I> List<M> getAll(Class<M> mClass, Class<I> iClass) {
        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache == null) {
            createCache(mClass, iClass);
            return new LinkedList<>();
        }
        List<M> models = new LinkedList<>();
        for (Cache.Entry<I, M> entry : cache) {
            models.add(entry.getValue().copy());
        }
        return models;
    }

    public static synchronized <M extends GenericModel<M, I>, I> List<M> filter(Class<M> mClass, Class<I> iClass, Predicate<M> mPredicate) {
        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache == null) {
            createCache(mClass, iClass);
            return new LinkedList<>();
        }

        List<M> models = new LinkedList<>();
        for (Cache.Entry<I, M> entry : cache) {
            if (mPredicate.test(entry.getValue())) {
                models.add(entry.getValue().copy());
            }
        }

        return models;
    }

    public static synchronized <M extends GenericModel<M, I>, I> void remove(Class<M> mClass, Class<I> iClass, I id) {
        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache != null && cache.containsKey(id)) {
            cache.remove(id);
            sizes.put(mClass, sizes.get(mClass) - 1);
        }
    }

    public static synchronized <M extends GenericModel<M, I>, I> void set(Class<M> mClass, Class<I> iClass, M model) {
        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache == null)
            cache = createCache(mClass, iClass);
        cache.put(model.getId(), model);
    }

    public static synchronized <M extends GenericModel<M, I>, I> void set(Class<M> mClass, Class<I> iClass, Collection<M> models) {
        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache == null) {
            cache = createCache(mClass, iClass);
            cache.putAll(models.stream().collect(Collectors.toMap(GenericModel::getId, m -> m)));
            sizes.put(mClass, models.size());
        } else {
            int size = sizes.get(mClass);
            for (M model : models) {
                if (!cache.containsKey(model.getId()))
                    size++;
                cache.put(model.getId(), model);
            }
            sizes.put(mClass, size);
        }
    }

    private static synchronized <M extends GenericModel<M, I>, I> Cache<I, M> createCache(Class<M> mClass, Class<I> iClass) {
        sizes.put(mClass, 0);
        return cacheManager.createCache(
                mClass.getName(),
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        iClass,
                        mClass,
                        ResourcePoolsBuilder.heap(500)
                )
        );
    }
}
