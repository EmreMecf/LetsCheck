package com.example.letscheck.db;

import java.util.List;

public interface Dao<TEntity> {
    List<TEntity> getAll();
    TEntity get(int id);
    int delete(int id);
    void add(TEntity entity);
    void update(TEntity entity);
}
