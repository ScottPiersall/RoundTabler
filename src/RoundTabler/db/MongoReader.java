package RoundTabler.db;

import RoundTabler.Configuration;

/*
 * MongoDB database reader
 * Requires the Mongo Java Driver
*/

public class MongoReader extends DBReader {

    public MongoReader(Configuration config) throws ClassNotFoundException {
        super(config);

        // Check for Java driver; if fails, throw ClassNotFoundException
        Class.forName("com.mongodb.client");

        // Use args to establish database connection
        //String dbUri = String.format("mongodb://%s:27017/local",
        //                                       config.getServer());
    }

    public Boolean readSchema() { return false; }

    public java.util.ArrayList<String> readColumn(SchemaItem item) { return null; }
}
