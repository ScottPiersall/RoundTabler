package RoundTabler;

import java.util.regex.*;
import RoundTabler.*;
import java.sql.*;


//
// A Class used for doing PCIScan of fields
// for protected cards.
//
public class PCIScan{

	// Match potential card numbers
	// by any sequence of digits between 13 and 16 characters in length
	// with embedded dashes
	static String CardNumberSequenceRegex = "\b(?:\\d[-]*?){13,16}\b";
	static Pattern CardNumberPattern = Pattern.compile( CardNumberSequenceRegex);
	
	public String JDBC_DRIVER;

	public string ScanResult;

	private Conenction pDBConnection;
	private String pTableName; 
	private String pMYSQLColumnSelect;
	private String pMYSQLColumnTypes;

	public PCIScan(){
		super();
		this.pMYSQLColumnTypes = "(mediumtext, longtext, text, tinytext, varchar)";
	}

	public PCIScan(Connection MYSqlDBConnection, string TableName  ){
		this();
		pDBConnection = DBConnection;
		pTableName = TableName;
		ScanResult = String.Empty;
		JDBC_DRIVER ="org.mariadb.jdbc.Driver";
		if ( pTableName.compareTo("*") == 0){
			// We are scanning all tables
			// so select should retrieve all table all columns which can store text
			pMYSQLColumnSelect = "SELECT TABLE_NAME, COLUMN_NAME from information_schema.COLUMNS where table_schema=DATABASE() and DATA_TYPE in " + this.pMySQLColumnTypes + ';';


		} else {
			// We are scanning a specific table
			// so select should retrieve all table all columns which can store text
		    pMYSQLColumnSelect = "SELECT TABLE_NAME, COLUMN_NAME from information_schema.COLUMNS where table_schema=DATABASE() and TABLE_NAME = '" + pTableName + " and DATA_TYPE in "  + this.pMySQLColumnTypes + ';';


		}

	}

	
	public void ScanMYSQLTables(){

		Statement cStatement = this.pDBConnection.createStatement();
		cStatement.executeSelect(pMYSQLColumnSelect);

		// for every row returned by cStatement

		// select columnName from TableName 
			// For each of these rows  
				// get confidencelevel Match
					// and when level > 0
						// place is result stringbuilder





	}




	public static int getConfidenceLevelmatch( String DatabaseRow ) {
		int result = 0;
		
		Matcher CardNumberSequenceMatcher = CardNumberPattern.matcher( DatabaseRow );


		// If we find what looks like a card sequence, make the confidence 75
		if ( CardNumberSequenceMatcher.matches())
			{
			result += 75;   // Assign a confidence Level of at least 75%
			// If the match passes LuhnsTest, Boost Confidence to 100
			if ( LuhnTest.Validate( DatabaseRow.substring( CardNumberSequenceMatcher.start(), CardNumberSequenceMatcher.end() ) ) )
				result += 25;
			}

		return result;
	}

	public void TestConfidence_100(){
		assert (PCIScan.getConfidenceLevelmatch("The customer's card number is 6011-1111-1111-1117 and they said on the phone we could sign them up for auto pay" )) == 100;
	}

	public void TestConfidence_75(){
		assert (PCIScan.getConfidenceLevelmatch("The number is 6011121511111117, which looks like a card but fails Luhn's test' " )) == 75;
	}

	public void TestConfidence_0(){
		assert (PCIScan.getConfidenceLevelmatch("There is no card data in this field" )) == 0;
		assert (PCIScan.getConfidenceLevelmatch("")) == 0;
	}

	public static void main(String[] args){
			PCIScan Tests;
			Tests = new PCIScan();
			Tests.TestConfidence_0();
			Tests.TestConfidence_75();
			Tests.TestConfidence_100();
	}

}