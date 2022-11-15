package RoundTabler.db;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.sql.SQLException;
import RoundTabler.Configuration;

/*
 * MongoDB database reader
 * Requires the Mongo Java Driver
*/

public class MongoReader extends DBReader {

    private MongoClient conn = null;

    public MongoReader(Configuration config) throws ClassNotFoundException, SQLException {
        super(config);

        // Check for Java driver; if fails, throw ClassNotFoundException
        Class.forName("com.mongodb.client");

        // Use args to establish database connection
        String dbUri = String.format("mongodb://%s:%s@%s:%s",
                                    config.getUser(), config.getPassword(),
                                    config.getServer(), !config.getPort().isBlank() ? config.getPort() : "27017");

        // The SQLException is fake here, but is done this way to ensure consistency across readers
        try {
            conn = MongoClients.create(dbUri);
        }
        catch (MongoException me) {
            // Convert the MongoException to an SQLException to be handled later
            throw new SQLException(me);
        }
    }

    public Boolean readSchema() { 
        return false; 
    }

    public java.util.ArrayList<String> readColumn(SchemaItem item) { return null; }
}
