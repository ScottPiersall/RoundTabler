package RoundTabler.db;

import RoundTabler.Configuration;

import java.sql.SQLException;
import java.util.InputMismatchException;

// DBReader factory
public class ReaderMaker {
    // Return the reader created by this
    static public DBReader getReader(Configuration config) throws ClassNotFoundException, SQLException, InputMismatchException {
        switch (config.getDbType().toLowerCase()) {
            case "mysql":
                return new MySQLReader(config);
            case "mariadb":
                return new MariaReader(config);
            case "mongo":
            case "mongodb":
                return new MongoReader(config);
            default:
                // No accepted database provided, throw InputMismatchException
                throw new InputMismatchException(String.format("Database type of %s not recognized or not supported; cannot instantiate DBReader.",
                                                               config.getDbType()));
        }
    }
}