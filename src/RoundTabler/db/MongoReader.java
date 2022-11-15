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
        String dbUri = String.format("mongodb://%s:%s@%s:%s/%s",
                                    config.getUser(), config.getPassword(),
                                    config.getServer(), !config.getPort().isBlank() ? config.getPort() : "27017",
                                    config.getDatabase());

        
    }

    public Boolean readSchema() { return false; }

    public java.util.ArrayList<String> readColumn(SchemaItem item) { return null; }
}
