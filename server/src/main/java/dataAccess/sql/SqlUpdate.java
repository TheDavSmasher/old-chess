package dataAccess.sql;

import dataAccess.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlUpdate {
    void execute(PreparedStatement statement) throws SQLException, DataAccessException;
}
