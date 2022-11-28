package RoundTabler.db;

import java.sql.SQLException;
import RoundTabler.Configuration;

/*
 * MariaDB database reader
 * Actually just uses the MySQL reader, since our application should not run into any edge cases
*/

public class MariaReader extends MySQLReader {
    public MariaReader(Configuration config) throws ClassNotFoundException, SQLException {
        super(config);
    }
}