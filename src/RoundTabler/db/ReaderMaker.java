package RoundTabler.db;

import RoundTabler.Configuration;

import java.sql.SQLException;
import java.util.InputMismatchException;

// DBReader factory
public class ReaderMaker extends DBReader {
    DBReader reader = null;

    // When constructed, uses the configuration to create the proper DBReader type
    public ReaderMaker(Configuration config) throws ClassNotFoundException, SQLException, InputMismatchException {
        super(config);

        switch (config.getDbType().toLowerCase()) {
            case "mysql":
                this.reader = new MySQLReader(config);
                break;
            case "mariadb":
                this.reader = new MariaReader(config);
                break;
            case "mongo":
            case "mongodb":
                this.reader = new MongoReader(config);
                break;
            default:
                // No accepted database provided, throw InputMismatchException
                throw new InputMismatchException(String.format("Database type of %s not recognized or not supported; cannot instantiate DBReader.",
                                                               config.getDbType()));
        }
    }

    // Return the reader created by this
    public DBReader getReader() {
        return this.reader;
    }

    // Must cover the bases for an abstract class, these are not intended to be called
    public Boolean readSchema() { return false; }

    public java.util.ArrayList<String> readColumn(SchemaItem item) { return null; }
}