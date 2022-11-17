package RoundTabler.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import RoundTabler.Configuration;

/*
 * MySQL database reader
 * Requires the MySQL JDBC Driver
*/

public class MySQLReader extends DBReader {

    PreparedStatement buildSchema = null;

    public MySQLReader(Configuration config) throws ClassNotFoundException, SQLException {
        super(config);
        
        // Check for JDBC driver; if fails, throw ClassNotFoundException
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Use args to establish database connection
        String jdbcUri = String.format("jdbc:mysql://%s:%s?user=%s&password=%s",
                                                config.getServer(), !config.getPort().isBlank() ? config.getPort() : "3306",
                                                config.getUser(), config.getPassword());

        this.conn = DriverManager.getConnection(jdbcUri);

        // Create the relevant query string for forming populating our SchemaItems
        StringBuilder stmtString = new StringBuilder();
        stmtString.append("SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE from information_schema.COLUMNS where DATA_TYPE IN ");
        stmtString.append("('mediumtext', 'longtext', 'text', 'tinytext', 'varchar') ");
        stmtString.append("AND TABLE_SCHEMA=?");
        if (!config.getTable().isBlank()) { stmtString.append(" AND TABLE_NAME=?"); }

        buildSchema = conn.prepareStatement(stmtString.toString());
        buildSchema.setString(1, config.getDatabase()); 
        if (!config.getTable().isBlank()) { buildSchema.setString(2, config.getTable()); }
    }

    // Reads the schema and stores its information in the SchemaItems structure
    public Boolean readSchema() {
        ResultSet rs = null;

        // If we have already read the schema with this reader, then disallow future reads
        if (!schemaItems.isEmpty())
            return true;

        try {
            rs = buildSchema.executeQuery();

            // If rs successfully produced results, then we iterate through it
            if (rs != null) {
                while (rs.next()) {
                    // Table_Name, Column_Name, Data_Type
                    schemaItems.Add(rs.getString(1), rs.getString(2), rs.getString(3));
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Error in SQL execution: " + e);
            return false;
        }

        return true;
    }

    // Return the contents of a column as an ArrayList
    // TODO: for exceptionally large databases, allow dynamically iterating through results (time + memory usage)
    public ArrayList<String> readColumn(SchemaItem item) {
        ArrayList<String> columnData = new ArrayList<String>();
        ResultSet rs = null;

        try {
            Statement select = this.conn.createStatement();
            String queryString = String.format("SELECT %s FROM %s.%s",
                                                       item.getColumnName(), this.config.getDatabase(), item.getTableName());

            rs = select.executeQuery(queryString);

            // If rs successfully produced results, then we iterate through it
            if (rs != null) {
                while (rs.next()) {
                    // Column data per row
                    columnData.add(rs.getString(1));
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e);
            return null;
        }

        return columnData;
    }
}