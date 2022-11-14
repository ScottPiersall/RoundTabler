package RoundTabler.db;

import java.util.Collections;
import java.util.ArrayList;

public class SchemaItems{

	private ArrayList<RoundTabler.db.SchemaItem> SchemaItemResults = new ArrayList<RoundTabler.db.SchemaItem>();

	public SchemaItems(){
		super();
	}

	public void Add( SchemaItem NewItem ) {
		SchemaItemResults.add( NewItem );
	}

	public void Add( String TableName, String ColumnName){
		SchemaItem NewItem = new SchemaItem( TableName, ColumnName );
		SchemaItemResults.add( NewItem );
	}

}
