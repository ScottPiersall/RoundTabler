package RoundTabler.db;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import RoundTabler.Configuration;
import RoundTabler.HTMLErrorOut;

/*
 * A DBReader that uses JDBC, so the format of usage is the same
*/

public class JDBCUser extends DBReader {
    
    private PreparedStatement buildSchema = null;
    protected String info_schema = "information_schema.COLUMNS";
    protected String datatypes = "('mediumtext', 'longtext', 'text', 'tinytext', 'varchar')";

    public JDBCUser( Configuration config ) {
        super(config);
    }

    // Reads the schema and stores its information in the SchemaItems structure
    public Boolean readSchema() {
        ResultSet rs = null;

        // If we have already read the schema with this reader, then disallow future reads
        if ( !schemaItems.isEmpty() )
            return true;

        try {
            rs = buildSchema.executeQuery();

            // If rs successfully produced results, then we iterate through it
            if ( rs.next() ) {
                do {
                    // Table_Name, Column_Name, Data_Type
                    schemaItems.Add( rs.getString(1), rs.getString(2), rs.getString(3) );
                } while ( rs.next() );
            }
            else {
                // Failed to yield results, some argument must be incorrect or the user must not have access
                new HTMLErrorOut("Error in fetching the schema: no data found. Please check your --database or --table arguments, as well as if the passed user has access to this database.");
                return false;
            }
        }
        catch ( SQLException e ) {
            new HTMLErrorOut("Error in SQL execution: " + e);
            return false;
        }

        return true;
    }

    // Return the contents of a column as an ArrayList
    public ArrayList<String> readColumn( SchemaItem item ) {
        ResultSet rs = null;

        try {
            Statement select = this.conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE );
            String queryString = String.format("SELECT %s FROM %s.%s WHERE %s IS NOT NULL AND LENGTH(%s) > 5",
                                                       item.getColumnName(), this.config.getDatabase(), item.getTableName(), item.getColumnName(), item.getColumnName() );

            rs = select.executeQuery(queryString);

            // If rs successfully produced results, then we return an iterable for it
            return new SmartIterable(rs);
        }
        catch ( SQLException e ) {
            new HTMLErrorOut("Error in SQL execution: " + e);
            return null;
        }
    }

    // Using the passed connection string, attempts to initiate a connection
    protected Boolean startConnection( String connString ) throws SQLException {
        this.conn = DriverManager.getConnection(connString);

        if (this.conn != null)
            return true;

        return false;
    }

    protected void buildSchemaQuery() throws SQLException {
        // Create the relevant query string for forming populating our SchemaItems
        StringBuilder stmtString = new StringBuilder();
        stmtString.append("SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE from ");
        stmtString.append(this.info_schema);
        stmtString.append(" where DATA_TYPE IN ");
        stmtString.append(this.datatypes);
        stmtString.append("AND TABLE_SCHEMA=?");
        if ( !config.getTable().isBlank() ) { stmtString.append(" AND TABLE_NAME=?"); }

        buildSchema = conn.prepareStatement(stmtString.toString(), ResultSet.TYPE_SCROLL_SENSITIVE);
        buildSchema.setString( 1, config.getDatabase() ); 
        if ( !config.getTable().isBlank() ) { buildSchema.setString( 2, config.getTable() ); }
    }
}
