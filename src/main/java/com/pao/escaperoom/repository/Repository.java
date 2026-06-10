package com.pao.escaperoom.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    void save(T entity) throws SQLException;
    Optional<T> findById(int id) throws SQLException;
    List<T> findAll() throws SQLException;
    void update(T entity) throws SQLException;
    void delete(int id) throws SQLException;
}
