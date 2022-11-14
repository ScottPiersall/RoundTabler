package RoundTabler.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import RoundTabler.Configuration;

/*
 * MariaDB database reader
 * Requires the MariaDB JDBC Driver
*/

public class MariaReader extends DBReader {

    public MariaReader(Configuration config) throws SQLException {
        super(config);

        // JDBC driver check fails for MariaDB JDBC driver always, for some reason
        // I think it is an issue with my development environment, but the below statement will throw
        // a descriptive error if the driver is not present
               
        // Use args to establish database connection
        String jdbcUri = String.format("jdbc:mariadb://%s?user=%s&password=%s",
                                                  config.getServer(),
                                                   config.getUser(), config.getPassword());

        this.conn = DriverManager.getConnection(jdbcUri);
        
    }

    public void getTables() {}

    // Test connection for demonstration
    // Gets the table, column, and privileges for columns
    // TODO: refine the sql statement to not use %'s: just used as a proof of concept
    public ResultSet getColumns() {
        ResultSet rs = null;

        try {
            Statement stmt = this.conn.createStatement();
            rs = stmt.executeQuery("SELECT TABLE_NAME, COLUMN_NAME, PRIVILEGES FROM information_schema.COLUMNS WHERE COLUMN_TYPE LIKE 'varchar%' OR COLUMN_TYPE LIKE '%text%' AND PRIVILEGES like '%elect%'");
        }
        catch (SQLException e) {
            System.out.println("Error in SQL execution: " + e);
        }

        return rs;
    }

    public void getColumnInfo() {}

}