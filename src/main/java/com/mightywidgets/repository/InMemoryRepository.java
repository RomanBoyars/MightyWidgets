/*
 InMemoryRepository class

 Created 16.04.2019
*/

package com.mightywidgets.repository;

import com.mightywidgets.error.EntityNotFoundException;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Represents a repository of objects based on java.util.Map
 * All objects stored in memory
 *
 * @param <T>  type of entities to store in repository
 * @param <ID> type of keys
 */
public abstract class InMemoryRepository<T, ID> implements PagingAndSortingRepository<T, ID> {

    private Map<ID, T> entities = new ConcurrentHashMap<>();

    /**
     * You must implement this in child class to key generation to work
     *
     * @return returns generated primary key. Must return an (@link ID} type of key
     */
    public abstract ID generatePrimaryKey();

    @Override
    public List<T> findAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> iterable) {
        return null;
    }

    @Override
    public long count() {
        return entities.size();
    }

    @Override
    public void deleteById(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null!");
        }
        T removedEntity = entities.remove(id);
        if (removedEntity == null) {
            throw new EntityNotFoundException(id);
        }
    }

    @Override
    public void delete(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("widget must not be null!");
        }
        deleteById(getId(entity));
    }

    @Override
    public void deleteAll(Iterable<? extends T> entitiesToDelete) {
        if (entitiesToDelete == null) {
            throw new IllegalArgumentException("entitiesToDelete must not be null");
        }
        for (T entity : entitiesToDelete) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        entities.clear();
    }

    @Override
    public <S extends T> S save(S entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null!");
        }
        ID id = getId(entity);
        if (id == null) {
            id = generatePrimaryKey();
            setId(entity, id);
        }

        entities.put(id, entity);
        return entity;
    }

    /**
     * Gets value of an ID field of an entity.
     * Note that for this to work entity class must have field annotated with {@link Id}
     * and id field must be of type {@link ID}
     *
     * @param entity entity to get ID from
     * @param <S>    entity class that can inherit {@link T}
     * @return returns value of an {@link ID} field
     */
    private <S extends T> ID getId(S entity) {
        Field idField = getIdField(entity);
        ID id;
        try {
            id = (ID) idField.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't retrieve object ID!");
        }
        return id;
    }

    /**
     * Sets value of an ID field of an entity.
     * Note that for this to work entity class must have field annotated with {@link Id}
     * and id field must be of type {@link ID}
     *
     * @param entity entity to get ID from
     * @param <S>    entity class that can inherit {@link T}
     */
    private <S extends T> void setId(S entity, ID id) {
        Field idField = getIdField(entity);
        try {
            idField.set(entity, id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't retrieve object ID!");
        }
    }

    /**
     * Gets ID field of an entity.
     * Note that for this to work entity class must have field annotated with {@link Id}
     * and id field must be of type {@link ID}
     *
     * @param entity entity to get ID from
     * @param <S>    entity class that can inherit {@link T}
     * @return returns {@link ID} field of an entity
     */
    private <S extends T> Field getIdField(S entity) {
        Field idField;
        idField =
                Arrays.stream(entity.getClass().getDeclaredFields()).filter(e -> e.isAnnotationPresent(Id.class)).findFirst().get();
        if (idField == null) {
            throw new RuntimeException("Object type does not have annotated ID field!");
        }
        idField.setAccessible(true);
        return idField;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entitiesToSave) {
        if (entitiesToSave == null) {
            throw new IllegalArgumentException("entitiesToSave must not be null");
        }
        List<S> savedEntities = new ArrayList<>();
        for (S entity : entitiesToSave) {
            save(entity);
            savedEntities.add(entity);
        }
        return savedEntities;
    }

    @Override
    public Optional<T> findById(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null!");
        }
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public boolean existsById(ID id) {
        return entities.containsKey(id);
    }

    /**
     * {@inheritDoc}
     * Note that for this to work entity class must have getter method
     * for sorted field
     *
     * @param sort
     * @return all entities sorted by the given options
     * @throws RuntimeException throws when entity class does not have field to sort by or getter method for it
     */
    @Override
    public List<T> findAll(Sort sort) {
        List<T> values = findAll();
        return sort(values, sort);
    }

    /**
     * Sorts given list of entities
     * Note that for this to work entity class must have getter method for sorted field
     *
     * @param values the entities list
     * @param sort   the {@link Sort} object
     * @return returns sorted {@link List} of entities
     */
    protected List<T> sort(List<T> values, Sort sort) {
        Iterator<Sort.Order> it = sort.iterator();

        while (it.hasNext()) {
            Sort.Order order = it.next();
            String propertyName = order.getProperty();
            Sort.Direction direction = order.getDirection();

            values = values.stream().sorted((o1, o2) -> {
                try {
                    Method m = o1.getClass().getMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
                    Comparable<Object> s1 = (Comparable<Object>) m.invoke(o1);
                    Comparable<Object> s2 = (Comparable<Object>) m.invoke(o2);
                    if (direction == Sort.Direction.ASC) return s1.compareTo(s2);
                    else return s2.compareTo(s1);
                } catch (Exception e) {
                    throw new RuntimeException("Entity does not contain such field or field is unavailable due to its" +
                            " access level!");
                }
            }).collect(Collectors.toList());
        }
        return values;
    }

    public Page<T> findAll(Pageable pageable) {
        List<T> entities = findAll();
        return paginate(sort(entities, pageable.getSort()), pageable);
    }

    /**
     * Paginates given list of entities
     *
     * @param entitiesToPaginate the entities list
     * @param pageable           the {@link Pageable} object
     * @return returns {@link Page} of entities
     */
    protected Page<T> paginate(List<T> entitiesToPaginate, Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int startElem = pageNumber * pageSize;
        int size = entitiesToPaginate.size();

        int endElem = (startElem + pageSize < size) ? startElem + pageSize : size;

        if (startElem > count()) {
            throw new RuntimeException("No such page " + pageNumber + "!");
        }
        return new PageImpl<>(entitiesToPaginate.subList(startElem, endElem), pageable, size);
    }

}
