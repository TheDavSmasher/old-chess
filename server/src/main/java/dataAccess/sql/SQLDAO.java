package dataAccess.sql;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

interface SqlQuery<T> {
    T execute(Connection c) throws SQLException, DataAccessException;
}

interface SqlUpdate {
    void execute(Connection c) throws SQLException, DataAccessException;
}

public abstract class SQLDAO {
    private static boolean databaseConfigured = false;

    protected <T> T tryStatement(SqlQuery<T> query) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            return query.execute(connection);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    protected void tryStatement(SqlUpdate update) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            update.execute(connection);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    protected static void configureDatabase() throws DataAccessException {
        if (!databaseConfigured) {
            DatabaseManager.configureDatabase();
            databaseConfigured = true;
        }
    }
}
