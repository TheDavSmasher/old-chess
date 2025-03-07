package dataAccess.sql;

import dataAccess.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlUpdate {
    void execute(Connection c) throws SQLException, DataAccessException;
}
