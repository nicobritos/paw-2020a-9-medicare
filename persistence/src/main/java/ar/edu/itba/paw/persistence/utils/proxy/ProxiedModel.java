package ar.edu.itba.paw.persistence.utils.proxy;

import ar.edu.itba.paw.interfaces.daos.generic.GenericDao;
import ar.edu.itba.paw.models.GenericModel;
import ar.edu.itba.paw.persistence.utils.DAOManager;
import ar.edu.itba.paw.persistence.utils.ReflectionGetterSetter;
import ar.edu.itba.paw.persistenceAnnotations.ManyToMany;
import ar.edu.itba.paw.persistenceAnnotations.ManyToOne;
import ar.edu.itba.paw.persistenceAnnotations.OneToMany;
import ar.edu.itba.paw.persistenceAnnotations.OneToOne;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

class ProxiedModel<M extends GenericModel<M, I>, I> implements ProxiedModelCollection {
    private final HashMap<String, Collection<GenericModel<Object, Object>>> savedCollection;
    private final HashMap<String, ProxyField> bypassFindByIds;
    private final HashMap<String, ProxyField> bypassFindById;
    private final HashMap<String, ProxyField> lazyFindByIds;
    private final HashMap<String, ProxyField> lazyFindById;
    private MethodInterceptor methodInterceptor;
    private GenericDao<M, I> dao;
    private M target;

    public ProxiedModel(M target) {
        this.savedCollection = new HashMap<>();
        this.bypassFindByIds = new HashMap<>();
        this.bypassFindById = new HashMap<>();
        this.lazyFindByIds = new HashMap<>();
        this.lazyFindById = new HashMap<>();
        this.target = target;
        this.dao = DAOManager.getDaoForModel(this.target.getClass());
        this.initialize();
    }

    public ProxiedModel(ProxiedModel<M, I> targetCopy) {
        this.savedCollection = new HashMap<>();
        this.bypassFindByIds = new HashMap<>();
        this.bypassFindById = new HashMap<>();
        this.lazyFindByIds = new HashMap<>();
        this.lazyFindById = new HashMap<>();
        try {
            this.target = (M) targetCopy.target.getClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        this.dao = DAOManager.getDaoForModel(this.target.getClass());

        ReflectionGetterSetter.setValues(this.target, ReflectionGetterSetter.listValues(targetCopy.target), true);
        this.initialize();
    }

    @Override
    public Collection<GenericModel<Object, Object>> getPreviousModels(Field field) {
        return this.savedCollection.get(field.getName());
    }

    @Override
    public void setPreviousCollection(Field field, Collection<GenericModel<Object, Object>> models) {
        this.savedCollection.put(field.getName(), new LinkedList<>(models));
    }

    public void setField(Field field, Object value) {
        ReflectionGetterSetter.set(this.target, field, value, true);
    }

    public M getTarget() {
        return this.target;
    }

    private void initialize() {
        Class<M> mClass = (Class<M>) this.target.getClass();

        ReflectionGetterSetter.iterateFields(mClass, OneToOne.class, field -> {
            OneToOne relation = field.getAnnotation(OneToOne.class);
            if (!relation.inverse() && !relation.lazy())
                return;

            Class<? extends GenericModel<?, Object>> otherClass;
            try {
                otherClass = (Class<? extends GenericModel<?, Object>>) field.getType();
            } catch (ClassCastException e) {
                // TODO
                throw new RuntimeException(e);
            }

            GenericDao<? extends GenericModel<?, Object>, Object> otherDao = DAOManager.getDaoForModel(otherClass);
            try {
                if (relation.inverse()) {
                    this.bypassFindById.put(
                            ReflectionGetterSetter.getter(mClass, field).getName(),
                            new ProxyField(
                                    otherDao,
                                    otherDao.getClass().getMethod("findById", Object.class),
                                    this.target,
                                    field,
                                    false
                            )
                    );
                } else {
                    this.lazyFindById.put(
                            ReflectionGetterSetter.getter(mClass, field).getName(),
                            new ProxyField(
                                    otherDao,
                                    otherDao.getClass().getMethod("findById", Object.class),
                                    this.target,
                                    field,
                                    false
                            )
                    );
                }
            } catch (NoSuchMethodException e) {
                // TODO
                e.printStackTrace();
            }
        });
        ReflectionGetterSetter.iterateFields(mClass, OneToMany.class, field -> {
            OneToMany relation = field.getAnnotation(OneToMany.class);
            if (!relation.inverse() && !relation.lazy())
                return;

            Class<? extends GenericModel<?, Object>> otherClass;
            try {
                otherClass = (Class<? extends GenericModel<?, Object>>) relation.className();
            } catch (ClassCastException e) {
                // TODO
                throw new RuntimeException(e);
            }

            GenericDao<? extends GenericModel<?, Object>, Object> otherDao = DAOManager.getDaoForModel(otherClass);
            try {
                if (relation.inverse()) {
                    this.bypassFindByIds.put(
                            ReflectionGetterSetter.getter(mClass, field).getName(),
                            new ProxyField(
                                    otherDao,
                                    otherDao.getClass().getMethod("findByIds", Collection.class),
                                    this.target,
                                    field,
                                    false
                            )
                    );
                } else {
                    this.lazyFindByIds.put(
                            ReflectionGetterSetter.getter(mClass, field).getName(),
                            new ProxyField(
                                    otherDao,
                                    otherDao.getClass().getMethod("findByIds", Collection.class),
                                    this.target,
                                    field,
                                    false
                            )
                    );
                }
            } catch (NoSuchMethodException e) {
                // TODO
                e.printStackTrace();
            }
        });
        ReflectionGetterSetter.iterateFields(mClass, ManyToOne.class, field -> {
            ManyToOne relation = field.getAnnotation(ManyToOne.class);
            if (!relation.inverse() && !relation.lazy())
                return;

            Class<? extends GenericModel<?, Object>> otherClass;
            try {
                otherClass = (Class<? extends GenericModel<?, Object>>) field.getType();
            } catch (ClassCastException e) {
                // TODO
                throw new RuntimeException(e);
            }

            GenericDao<? extends GenericModel<?, Object>, Object> otherDao = DAOManager.getDaoForModel(otherClass);
            try {
                if (relation.inverse()) {
                    this.bypassFindById.put(
                            ReflectionGetterSetter.getter(mClass, field).getName(),
                            new ProxyField(
                                    otherDao,
                                    otherDao.getClass().getMethod("findById", Object.class),
                                    this.target,
                                    field,
                                    false
                            )
                    );
                } else {
                    this.lazyFindById.put(
                            ReflectionGetterSetter.getter(mClass, field).getName(),
                            new ProxyField(
                                    otherDao,
                                    otherDao.getClass().getMethod("findById", Object.class),
                                    this.target,
                                    field,
                                    false
                            )
                    );
                }
            } catch (NoSuchMethodException e) {
                // TODO
                e.printStackTrace();
            }
        });
        ReflectionGetterSetter.iterateFields(mClass, ManyToMany.class, field -> {
            ManyToMany relation = field.getAnnotation(ManyToMany.class);
            if (!relation.inverse() && !relation.lazy())
                return;

            Class<? extends GenericModel<?, Object>> otherClass;
            try {
                otherClass = (Class<? extends GenericModel<?, Object>>) relation.className();
            } catch (ClassCastException e) {
                // TODO
                throw new RuntimeException(e);
            }

            GenericDao<? extends GenericModel<?, Object>, Object> otherDao = DAOManager.getDaoForModel(otherClass);
            try {
                if (relation.inverse()) {
                    this.bypassFindByIds.put(
                            ReflectionGetterSetter.getter(mClass, field).getName(),
                            new ProxyField(
                                    otherDao,
                                    otherDao.getClass().getMethod("findByIds", Collection.class),
                                    this.target,
                                    field,
                                    false
                            )
                    );
                } else {
                    this.lazyFindByIds.put(
                            ReflectionGetterSetter.getter(mClass, field).getName(),
                            new ProxyField(
                                    otherDao,
                                    otherDao.getClass().getMethod("findByIds", Collection.class),
                                    this.target,
                                    field,
                                    false
                            )
                    );
                }
            } catch (NoSuchMethodException e) {
                // TODO
                e.printStackTrace();
            }
        });

        this.methodInterceptor = (proxy, method, args, methodProxy) -> {
            if (proxy == null && method == null && args == null && methodProxy == null)
                return this;

            proxy = this.target;
            ProxyField savedMethodData = this.bypassFindById.get(method.getName());
            if (savedMethodData != null) {
                // We always want to refresh the model reference so we need to retrieve it from the persistence layer
                Object result, modelId;
                if (savedMethodData.getInstanceField().isAnnotationPresent(OneToOne.class)) {
                    OneToOne relation = savedMethodData.getInstanceField().getAnnotation(OneToOne.class);
                    if (relation.inverse()) {
                        result = savedMethodData.getDao().findByField(relation.name(), this.target.getId()).stream().findFirst().orElse(null);
                        modelId = null;
                    } else {
                        modelId = this.dao.findFieldById(this.target.getId(), relation.name()).orElse(null);
                        result = null;
                    }
                } else {
                    ManyToOne relation = savedMethodData.getInstanceField().getAnnotation(ManyToOne.class);
                    modelId = this.dao.findFieldById(this.target.getId(), relation.name()).orElse(null);
                    result = null;
                }

                if (modelId != null) {
                    Method savedMethod = savedMethodData.getDaoMethod();
                    if (method.isAccessible()) {
                        result = savedMethod.invoke(savedMethodData.getDao(), modelId);
                    } else {
                        method.setAccessible(true);
                        result = savedMethod.invoke(savedMethodData.getDao(), modelId);
                        method.setAccessible(false);
                    }

                    try {
                        Optional<Object> optionalResult = (Optional<Object>) result;
                        result = optionalResult.orElse(null);
                    } catch (ClassCastException ignored) {
                    }
                }

                ReflectionGetterSetter.set(proxy, savedMethodData.getInstanceField(), result, true);
                return result;
            }

            savedMethodData = this.bypassFindByIds.get(method.getName());
            if (savedMethodData != null) {
                // We always want to refresh the model reference so we need to retrieve it from the persistence layer
                Collection<?> modelIds;
                if (savedMethodData.getInstanceField().isAnnotationPresent(ManyToMany.class)) {
                    ManyToMany relation = savedMethodData.getInstanceField().getAnnotation(ManyToMany.class);
                    modelIds = this.dao.findFieldByIdManyToMany(this.target.getId(), relation.name(), relation.otherName(), relation.tableName());
                } else {
                    OneToMany relation = savedMethodData.getInstanceField().getAnnotation(OneToMany.class);
                    modelIds = savedMethodData.getDao().findByField(relation.name(), this.target.getId());
                }

                Method savedMethod = savedMethodData.getDaoMethod();
                Collection<GenericModel<Object, Object>> result;
                if (method.isAccessible()) {
                    result = (Collection<GenericModel<Object, Object>>) savedMethod.invoke(savedMethodData.getDao(), modelIds);
                } else {
                    method.setAccessible(true);
                    result = (Collection<GenericModel<Object, Object>>) savedMethod.invoke(savedMethodData.getDao(), modelIds);
                    method.setAccessible(false);
                }

                ReflectionGetterSetter.set(proxy, savedMethodData.getInstanceField(), result, true);

                this.savedCollection.put(savedMethodData.instanceField.getName(), new LinkedList<>(result));
                return result;
            }

            savedMethodData = this.lazyFindById.get(method.getName());
            if (savedMethodData != null) {
                if (savedMethodData.getHasBeenLoaded()) {
                    if (method.isAccessible()) {
                        return method.invoke(proxy, args);
                    } else {
                        method.setAccessible(true);
                        Object result = method.invoke(proxy, args);
                        method.setAccessible(false);
                        return result;
                    }
                }

                Object result;
                Method savedMethod = savedMethodData.getDaoMethod();
                if (method.isAccessible()) {
                    result = savedMethod.invoke(savedMethodData.getDao(), savedMethodData.getModelId());
                } else {
                    method.setAccessible(true);
                    result = savedMethod.invoke(savedMethodData.getDao(), savedMethodData.getModelId());
                    method.setAccessible(false);
                }
                try {
                    Optional<Object> optionalResult = (Optional<Object>) result;
                    result = optionalResult.orElse(null);
                } catch (ClassCastException ignored) {
                }

                ReflectionGetterSetter.set(proxy, savedMethodData.getInstanceField(), result, true);
                savedMethodData.setHasBeenLoaded(true);
                return result;
            }

            savedMethodData = this.lazyFindByIds.get(method.getName());
            if (savedMethodData != null) {
                if (savedMethodData.getHasBeenLoaded()) {
                    if (method.isAccessible()) {
                        return method.invoke(proxy, args);
                    } else {
                        method.setAccessible(true);
                        Object result = method.invoke(proxy, args);
                        method.setAccessible(false);
                        return result;
                    }
                }

                Method savedMethod = savedMethodData.getDaoMethod();
                Collection<GenericModel<Object, Object>> result;
                if (method.isAccessible()) {
                    result = (Collection<GenericModel<Object, Object>>) savedMethod.invoke(savedMethodData.getDao(), savedMethodData.getModelIdCollection());
                } else {
                    method.setAccessible(true);
                    result = (Collection<GenericModel<Object, Object>>) savedMethod.invoke(savedMethodData.getDao(), savedMethodData.getModelIdCollection());
                    method.setAccessible(false);
                }

                ReflectionGetterSetter.set(proxy, savedMethodData.getInstanceField(), result, true);

                savedMethodData.setHasBeenLoaded(true);
                this.savedCollection.put(savedMethodData.instanceField.getName(), new LinkedList<>(result));

                return result;
            }

            if (method.isAccessible()) {
                return method.invoke(proxy, args);
            } else {
                method.setAccessible(true);
                Object result = method.invoke(proxy, args);
                method.setAccessible(false);
                return result;
            }
        };
    }

    public MethodInterceptor getMethodInterceptor() {
        return this.methodInterceptor;
    }

    private static class ProxyField {
        private boolean hasBeenLoaded;
        private final GenericDao<? extends GenericModel, Object> dao;
        private final Field instanceField;
        private final Method daoMethod;
        private final Object instance;

        ProxyField(GenericDao<? extends GenericModel, Object> dao, Method daoMethod, Object instance, Field instanceField, boolean hasBeenLoaded) {
            this.hasBeenLoaded = hasBeenLoaded;
            this.instanceField = instanceField;
            this.daoMethod = daoMethod;
            this.instance = instance;
            this.dao = dao;
        }

        public boolean getHasBeenLoaded() {
            return this.hasBeenLoaded;
        }

        public GenericDao<? extends GenericModel, Object> getDao() {
            return this.dao;
        }

        public Field getInstanceField() {
            return this.instanceField;
        }

        public Method getDaoMethod() {
            return this.daoMethod;
        }

        public void setHasBeenLoaded(boolean hasBeenLoaded) {
            this.hasBeenLoaded = hasBeenLoaded;
        }

        public Object getModelId() {
            GenericModel model = ((GenericModel) ReflectionGetterSetter.get(this.instance, this.instanceField, true));
            if (model != null)
                return model.getId();
            return null;
        }

        public Collection<Object> getModelIdCollection() {
            Collection<GenericModel> models = (Collection<GenericModel>) ReflectionGetterSetter.get(this.instance, this.instanceField, true);
            if (models != null) {
                return models.stream().map(GenericModel::getId).collect(Collectors.toList());
            }
            return new LinkedList<>();
        }
    }
}
