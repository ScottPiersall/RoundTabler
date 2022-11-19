package RoundTabler.db;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.BsonInt64;
import org.bson.conversions.Bson;
import com.mongodb.MongoException;
import com.mongodb.client.*;

import java.sql.SQLException;
import RoundTabler.Configuration;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;

/*
 * MongoDB database reader
 * Requires the Mongo Java Driver
*/

public class MongoReader extends DBReader {

    private MongoClient conn = null;

    // Collection of "collections" for use when searching, so we don't have to retrieve the collection so often
    private HashMap<String, MongoCollection<Document>> collections = new HashMap<>();

    public MongoReader(Configuration config) throws ClassNotFoundException, SQLException {
        super(config);

        // Check for Java driver; if fails, throw ClassNotFoundException
        Class.forName("com.mongodb.client.MongoClient");

        // Use args to establish database connection
        // MongoDB can use an auth source, which is where things can get weird, for now assume it is the database chosen
        String dbUri = String.format("mongodb://%s:%s@%s:%s/?authSource=%s",
                                    config.getUser(), config.getPassword(),
                                    config.getServer(), !config.getPort().isBlank() ? config.getPort() : "27017",
                                    config.getDatabase());

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
        // If we have already read the schema with this reader, then disallow future reads
        if (!schemaItems.isEmpty())
            return true;

        MongoDatabase db = conn.getDatabase(this.config.getDatabase());
        
        // Collections are essentially tables, so we want to get a list of them
        ListCollectionsIterable<Document> collections = db.listCollections();

        // Iterate through the collections and build our schema in the same format as a relational database
        // Have to keep in mind the table argument passed here, if it was given
        // Many simplifications are performed to dumb results down to string values that are easily represented
        for (Document doc : collections) {
            if (!this.config.getTable().isBlank() && doc.getString("name") != this.config.getTable() || doc.isEmpty())
                continue;

            MongoCollection<Document> collection = db.getCollection(doc.getString("name"));

            this.collections.put(doc.getString("name"), collection); // "Cache" the collection reference

            // Retrieve the first object in this collection to find the column names
            // Since we check for emptiness earlier, this should always work
            // TODO: a notable edge case is that not all documents have the same property set in Mongo, need a solution for this
            Document first = collection.find().first();

            // The set of keys are the column names for this collection(table)
            // Naively assume that the type is always string (at least for now)
            for (String key : first.keySet()) {
                // Skip the _id column: our scans should never uncover useful information there
                if (Objects.equals(key, "_id")) {
                    continue;
                }

                // TableName, ColumnName, DataType
                schemaItems.Add(doc.get("name").toString(), key, "varchar");
            }
        }

        return true;
    }

    public java.util.ArrayList<String> readColumn(SchemaItem item) { 
        ArrayList<String> columnData = new ArrayList<String>();

        // Retrieve the relevant collection from our cache
        MongoCollection<Document> collection = this.collections.get(item.getTableName());

        // Read all objects from the collection with the property name of this item
        // Recovers one string per-document, similar to a relational database
        for (Document doc : collection.find()) {
            // If not empty, add it to the list to check (same as NULL check for MySQL)
            // Some strange behavior with the library makes get().toString() have different results than getString()
            if (doc.get(item.getColumnName()).toString().isEmpty())
                continue;

            columnData.add(doc.getString(item.getColumnName()));
        }

        return columnData;
    }
}
