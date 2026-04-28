package com.github.owenliou.campkeeper.base.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CommonService<T, ID extends Serializable> {
    
    <S extends T> S save(S entity);

    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    /**
     * Returns a reference to the entity with the given identifier.
     *
     * @param id must not be {@literal null}.
     * @return a reference to the entity with the given identifier.
     * @throws jakarta.persistence.EntityNotFoundException if no entity exists for given {@code id}.
     */
    T getOne(ID id);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    List<T> findAll();

    List<T> findAllById(Iterable<ID> ids);

    List<T> findAll(Sort sort);

    Page<T> findAll(Pageable pageable);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAll(Iterable<? extends T> entities);

    void deleteAll();

    void flush();

    T saveAndFlush(T entity);

}
