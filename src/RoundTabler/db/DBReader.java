package RoundTabler.db;

import java.sql.Connection;
import java.sql.ResultSet;
import RoundTabler.Configuration;

/*
 * Generic Database Reader
 * Extend this class to add support for specific databases
 * Results are expected to be in the same format for each database
*/

public abstract class DBReader {

    Connection conn = null;

    // Constructor
    // If cannot find driver (e.g., JDBC for MySQL), throws ClassNotFoundException
    // If the arguments passed yield some connection error, throws SQLException
    DBReader(Configuration config) {

            // Perform per-database configuration with args
            // Args can include specific database or connection pool settings
            // as well as connection and authentication details
    }
    
    // Get tables (or their equivalent) and return them in an iterable list
    abstract public void getTables();

    // Get the columns of a particular table
    abstract public ResultSet getColumns();

    // Get the structure or data of a column
    abstract public void getColumnInfo();

    // Return the current connection object (if applicable for that database type)
    // If not applicable or connection failed to establish, conn == null
    public Connection getConnection() {
        return this.conn;
    }
}