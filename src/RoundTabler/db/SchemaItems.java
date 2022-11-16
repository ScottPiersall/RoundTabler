package RoundTabler.db;

import java.util.ArrayList;

public class SchemaItems{

	public ArrayList<SchemaItem> SchemaItemResults = new ArrayList<SchemaItem>();

	public SchemaItems(){ }

	public void Add( SchemaItem NewItem ) {
		SchemaItemResults.add( NewItem );
	}

	public void Add( String TableName, String ColumnName ) {
		SchemaItem NewItem = new SchemaItem( TableName, ColumnName );
		SchemaItemResults.add( NewItem );
	}

	public void Add( String TableName, String ColumnName, String ColumnType ) {
		SchemaItem NewItem = new SchemaItem( TableName, ColumnName, ColumnType );
		SchemaItemResults.add( NewItem );
	}

	// Returns true if there is >= 1 members of results, otherwise false
	public Boolean isEmpty() {
		return this.SchemaItemResults.isEmpty();
	}

	// Returns the number of SchemaItems contained in this structure
	public int size() {
		return this.SchemaItemResults.size();
	}

	// Returns a reference this SchemaItems held by this object
	public ArrayList<SchemaItem> getItems() {
		return this.SchemaItemResults;
	}

	// Mimics the ArrayList get function (kinda proxies it)
	public SchemaItem get(int index) {
		return this.SchemaItemResults.get(index);
	}
}
