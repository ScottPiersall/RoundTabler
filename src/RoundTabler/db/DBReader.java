package RoundTabler.db;

import java.sql.Connection;
import java.sql.SQLException;

import RoundTabler.Configuration;

/*
 * Generic Database Reader
 * Extend this class to add support for specific databases
 * Results are expected to be in the same format for each database
*/

public abstract class DBReader {

    protected Connection conn = null;
    protected Configuration config = null;
    
    protected SchemaItems schemaItems = new SchemaItems();

    // Constructor
    // If cannot find driver (e.g., JDBC for MySQL), throws ClassNotFoundException
    // If the arguments passed yield some connection error, throws SQLException
    DBReader(Configuration config) {
            // Perform per-database configuration with args
            // Args can include specific database or connection pool settings
            // as well as connection and authentication details

            this.config = config;
    }

    // Return the current connection object (if applicable for that database type)
    // If not applicable or connection failed to establish, conn == null
    public Connection getConnection() {
        return this.conn;
    }

    // Return the current configuration attached to the creation of this reader
    public Configuration getConfiguration() {
        return this.config;
    }

    // Return the SchemaItems retrieved by this DBReader, which may be empty
    public SchemaItems getSchemaItems() {
        return this.schemaItems;
    }

    // Close the connection
    public void closeConnection() {
        if (this.conn != null)
            try { this.conn.close(); } catch ( SQLException e ) { e.printStackTrace(System.out); }
    }

    // Using a ResultSet or similar data type construct SchemaItems
    // Implemented on a per-database basis, returns true if successful, false otherwise
    abstract public Boolean readSchema();

    // Given a SchemaItem, get its contents as an array of strings
    abstract public java.util.ArrayList<String> readColumn(SchemaItem item);
}