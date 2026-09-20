package ru.rtumirea.meetly.repository;

import java.util.List;

public interface BaseRepository<T, ID> {
    T save(T entity);

    List<T> findAll();

    T findById(ID id);

    boolean deleteById(ID id);
}
