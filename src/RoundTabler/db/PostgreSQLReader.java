package RoundTabler.db;

import java.sql.SQLException;
import RoundTabler.Configuration;

/*
 * PostgreSQL Database reader
 * Requires the PostgreSQL JDBC driver
*/

public class PostgreSQLReader extends JDBCUser {
    public PostgreSQLReader(Configuration config) throws ClassNotFoundException, SQLException {
        super(config);
        
        // Check for JDBC driver; if fails, throw ClassNotFoundException
        Class.forName("org.postgresql.core.BaseConnection");

        // Use args to establish database connection
        String jdbcUri = String.format("jdbc:postgresql://%s:%s/%s?user=%s&password=%s",
                                                config.getServer(), !config.getPort().isBlank() ? config.getPort() : "5432",
                                                config.getDatabase(),
                                                config.getUser(), config.getPassword());

        startConnection(jdbcUri);

        // Postgres has slightly different data types that we may want to examine
        this.datatypes = "('mediumtext', 'longtext', 'text', 'tinytext', 'varchar', 'character varying') ";
        
        buildSchemaQuery();
    }
}