package dataAccess.sql;

import dataAccess.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlQuery<T> {
    T execute(Connection c) throws SQLException, DataAccessException;
}
