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
		return SchemaItemResults.isEmpty();
	}
}
