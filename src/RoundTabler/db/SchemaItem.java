package RoundTabler.db;

public class SchemaItem{

	public String getTableName() { return this.pTableName; }
	public String getColumnName() { return this.pColumnName; }

	private String pTableName;
	private String pColumnName;
	
	public SchemaItem( String TableName, String ColumnName ){
		super();
		this.pTableName = TableName;
		this.pColumnName = ColumnName;
	}

}