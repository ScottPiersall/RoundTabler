package RoundTabler.db;

// Keeps track of a member of the schema
public class SchemaItem{

	private String pTableName;
	private String pColumnName;
	private String pColumnDataType;
	
	// Constructor for database types without explicit column types
	public SchemaItem( String TableName, String ColumnName){
		this.pTableName = TableName;
		this.pColumnName = ColumnName;
	}

	public SchemaItem( String TableName, String ColumnName, String DataType ){
		this.pTableName = TableName;
		this.pColumnName = ColumnName;
		this.pColumnDataType = DataType;
	}

	public String getTableName() { return this.pTableName; }
	public String getColumnName() { return this.pColumnName; }
	public String getColumnType() { return this.pColumnDataType; }

	@Override
	public String toString() {
		return String.format("(%s, %s)", this.pTableName, this.pColumnName);
	}
}