package RoundTabler.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import RoundTabler.Configuration;

/*
 * MySQL database reader
*/

public class MySQLReader extends DBReader {

    public MySQLReader(Configuration config) {
        super(config);
        
        try {
            // Check for JDBC driver; if fails, throw ClassNotFoundException
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Use args to establish database connection
            String jdbcUri = String.format("jdbc:mysql://%s?user=%s&password=%s",
                                                    config.getServer(),
                                                    config.getUser(), config.getPassword());

            this.conn = DriverManager.getConnection(jdbcUri);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Error in locating driver: " + e);
        }
        catch (SQLException e) {
            System.out.println("Error in establishing connection: " + e);
        }
    }

    public void executeQuery() {}

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