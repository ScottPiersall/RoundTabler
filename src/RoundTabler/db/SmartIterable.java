package RoundTabler.db;

/*
 * Helps keep memory usage down by limiting database entries allowed in memory at once
 * Only built to go forward for the scanning, should not need to go backwards or arbitrarily
 * If I knew more about Java I would make this work for generic type E, but I was unable to do this initially
*/

import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import RoundTabler.HTMLErrorOut;

public class SmartIterable extends ArrayList<String> {

    private static final int MAX_ENTRIES = 2000;

    private ArrayList<String> cache;
    private ResultSet queryResults;

    public SmartIterable(ResultSet res) {
        this.cache = new ArrayList<String>();
        this.queryResults = res;
    }

    // Empty current set and begin reading next batch of data
    private Boolean readNextBatch() {
        this.cache.clear();
    
        try {
            if (this.queryResults.isClosed())
                return false;

            if (this.queryResults.next()) {
                do {
                    this.cache.add(this.queryResults.getString(1));
                } while (this.cache.size() < MAX_ENTRIES && this.queryResults.next());
            }
            else {
                this.queryResults.close();
                return false;
            }
        }
        catch (SQLException sqlex) {
            new HTMLErrorOut("Error in SQL Execution:" + sqlex);
            return false;
        }

        return true;
    }

    // Override size to return ResultSet size so typical iteration can be done
    @Override
    public int size() {
        int lastRow = 0;

        try {
            if (this.queryResults.isClosed())
                return 0;

            int currentRow = this.queryResults.getRow();
            this.queryResults.last();
            lastRow = this.queryResults.getRow();

            // Reset position
            this.queryResults.absolute(currentRow);
        }
        catch (SQLException sqlex) {
            new HTMLErrorOut("Error in SQL Execution: " + sqlex);
            return 0;
        }

        return lastRow;
    }

    // Override the ArrayList get to read from the ArrayList contained here
    // We will cycle old information out and pull new information
    @Override
    public String get(int index) {
        // If we get to an edge, then fetch new data
        if (index % MAX_ENTRIES == 0) {
            readNextBatch();
        }

        if (index % MAX_ENTRIES < this.cache.size()) {
            return this.cache.get(index % MAX_ENTRIES);
        }

        return "";
    }
}
