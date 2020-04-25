package ar.edu.itba.paw.persistence.utils.cache;

import ar.edu.itba.paw.models.GenericModel;
import ar.edu.itba.paw.persistence.utils.ReflectionGetterSetter;
import ar.edu.itba.paw.persistence.utils.proxy.ProxyHelper;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class CacheHelper {
    public static final String CACHED_MODEL_FIELD_NAME = "cached";
    private static final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
    private static final Map<String, Cache<?, ?>> cacheMap = new HashMap<>();
    private static final Map<String, Integer> sizes = new HashMap<>();
    public static boolean cacheEnabled;
    public static int cacheSize;

    @Autowired
    private void setCacheEnabled(boolean isCacheEnabled, int cacheSize) {
        if (isCacheEnabled) {
            cacheManager.init();
            CacheHelper.cacheSize = cacheSize;
        }
        cacheEnabled = isCacheEnabled;
    }

    public static synchronized <M extends GenericModel<M, I>, I> void clean() {
        for (Cache<?, ?> cache : cacheMap.values()) {
            cache.clear();
        }
    }

    public static synchronized <M extends GenericModel<M, I>, I> int getSize(Class<M> mClass) {
        return sizes.getOrDefault(mClass.getName(), 0);
    }

    public static synchronized <M extends GenericModel<M, I>, I> M get(Class<M> mClass, Class<I> iClass, I key) {
        if (!cacheEnabled)
            return null;

        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache == null) {
            createCache(mClass, iClass);
            return null;
        }

        M model = cache.get(key);
        if (model != null) {
            model = ProxyHelper.copyInstance(model);

            try {
                ProxyHelper.setField(model, ReflectionGetterSetter.getFieldByName(CACHED_MODEL_FIELD_NAME, mClass), true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return model;
    }

    public static synchronized <M extends GenericModel<M, I>, I> CachedCollection<M> getAll(Class<M> mClass, Class<I> iClass) {
        if (!cacheEnabled)
            return new CachedCollection<>();

        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache == null) {
            createCache(mClass, iClass);
            return new CachedCollection<>(CachedCollectionStatus.COMPLETE);
        }

        List<M> models = new LinkedList<>();
        for (Cache.Entry<I, M> entry : cache) {
            M model = ProxyHelper.copyInstance(entry.getValue());

            try {
                ProxyHelper.setField(model, ReflectionGetterSetter.getFieldByName(CACHED_MODEL_FIELD_NAME, mClass), true);
            } catch (NoSuchFieldException ignored) {
            }

            models.add(model);
        }

        return new CachedCollection<>(
                models,
                sizes.get(mClass.getName()) == models.size() ? CachedCollectionStatus.COMPLETE : CachedCollectionStatus.INCOMPLETE
        );
    }

    public static synchronized <M extends GenericModel<M, I>, I> FilteredCachedCollection<M> filter(Class<M> mClass, Class<I> iClass, Predicate<M> mPredicate) {
        if (!cacheEnabled)
            return new FilteredCachedCollection<>();

        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache == null) {
            createCache(mClass, iClass);
            return new FilteredCachedCollection<>(CachedCollectionStatus.COMPLETE);
        }

        List<M> models = new LinkedList<>();
        List<M> allModels = new LinkedList<>();
        for (Cache.Entry<I, M> entry : cache) {
            M model = ProxyHelper.copyInstance(entry.getValue());
            if (mPredicate.test(entry.getValue())) {
                try {
                    ProxyHelper.setField(model, ReflectionGetterSetter.getFieldByName(CACHED_MODEL_FIELD_NAME, mClass), true);
                } catch (NoSuchFieldException ignored) {
                }
                models.add(model);
            }
            allModels.add(model);
        }

        return new FilteredCachedCollection<>(
                allModels,
                models,
                sizes.get(mClass.getName()) == allModels.size() ? CachedCollectionStatus.COMPLETE : CachedCollectionStatus.INCOMPLETE
        );
    }

    public static synchronized <M extends GenericModel<M, I>, I> FilteredCachedCollection<M> filter(Class<M> mClass, Class<I> iClass, Predicate<M> mPredicate, int limit, int offset) { //TODO
        if (!cacheEnabled)
            return new FilteredCachedCollection<>();

        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache == null) {
            createCache(mClass, iClass);
            return new FilteredCachedCollection<>(CachedCollectionStatus.COMPLETE);
        }

        List<M> models = new LinkedList<>();
        List<M> allModels = new LinkedList<>();
        for (Cache.Entry<I, M> entry : cache) {
            M model = ProxyHelper.copyInstance(entry.getValue());
            if (mPredicate.test(entry.getValue())) {
                try {
                    ProxyHelper.setField(model, ReflectionGetterSetter.getFieldByName(CACHED_MODEL_FIELD_NAME, mClass), true);
                } catch (NoSuchFieldException ignored) {
                }
                models.add(model);
            }
            allModels.add(model);
        }

        return new FilteredCachedCollection<>(
                allModels,
                models,
                sizes.get(mClass.getName()) == allModels.size() ? CachedCollectionStatus.COMPLETE : CachedCollectionStatus.INCOMPLETE
        );
    }

    public static synchronized <M extends GenericModel<M, I>, I> void remove(Class<M> mClass, Class<I> iClass, I id) {
        if (!cacheEnabled)
            return;

        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache != null && cache.containsKey(id)) {
            cache.remove(id);
            sizes.put(mClass.getName(), sizes.get(mClass.getName()) - 1);
        }
    }

    public static synchronized <M extends GenericModel<M, I>, I> void set(Class<M> mClass, Class<I> iClass, M model) {
        if (!cacheEnabled)
            return;

        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache == null) {
            cache = createCache(mClass, iClass);
        }

        cache.put(model.getId(), model);
    }

    public static synchronized <M extends GenericModel<M, I>, I> void set(Class<M> mClass, Class<I> iClass, Collection<M> models) {
        if (!cacheEnabled)
            return;

        Cache<I, M> cache = cacheManager.getCache(mClass.getName(), iClass, mClass);
        if (cache == null) {
            cache = createCache(mClass, iClass);
            cache.putAll(models.stream().collect(Collectors.toMap(GenericModel::getId, m -> m)));
            sizes.put(mClass.getName(), models.size());
        } else {
            int size = sizes.get(mClass.getName());
            for (M model : models) {
                if (!cache.containsKey(model.getId()))
                    size++;
                cache.put(model.getId(), model);
            }
            sizes.put(mClass.getName(), size);
        }
    }

    private static synchronized <M extends GenericModel<M, I>, I> Cache<I, M> createCache(Class<M> mClass, Class<I> iClass) {
        if (!cacheEnabled)
            return null;

        sizes.put(mClass.getName(), 0);
        Cache<I, M> cache = cacheManager.createCache(
                mClass.getName(),
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        iClass,
                        mClass,
                        ResourcePoolsBuilder.heap(cacheSize)
                )
        );
        cacheMap.put(mClass.getName(), cache);
        return cache;
    }
}
