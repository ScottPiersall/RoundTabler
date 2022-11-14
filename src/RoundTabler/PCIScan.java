package RoundTabler;

import java.util.regex.*;
import RoundTabler.*;
import java.sql.*;

import static utility.ApplicationUtility.*;
import RoundTabler.db.SchemaItems;
import RoundTabler.db.SchemaItem;

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

	private RoundTabler.Configuration pScanConfiguration;

	private Connection pDBConnection;
	private String pTableName; 
	private String pMYSQLColumnSelect;
	private String pMYSQLColumnTypes;

	private PerformanceSummary pPerformanceSummary;

	private StringBuilder psbResults;

	public PCIScan(){
		super();
		this.pMYSQLColumnTypes = "(mediumtext, longtext, text, tinytext, varchar)";
		this.psbResults = new StringBuilder();
		psbResults.append("\n");
	}

	public PCIScan( RoundTabler.Configuration ScanConfiguration, RoundTabler.PerformanceSummary Summary){
		this();
		pScanConfiguration = ScanConfiguration;
		pPerformanceSummary = Summary;
	}

	public String ScanResult() { return this.psbResults.toString(); }

	public int ScanMySQL() throws SQLException {
		//
		// Performs MySQL-Based Scan
		// using the settings and configuration 
		// contained in pScanConfiguration
		if ( pScanConfiguration.getDbType().toUpperCase().compareTo("MYSQL") != 0 ) {
			new HTMLErrorOut(pScanConfiguration.getFile(), "Database Type Mismatch. Database Type Configuration " + pScanConfiguration.getDbType() + " cannot be used with MySQL Scan" );
			return 0;
		}

		





		// Pseudocode
		// gather table list
			// gather column list for each table
				// Instantiate New PerformanceResult
					// TableName->PerformanceResult ColumnName->PerformanceResult
					// GetStartTime ->PerformanceResult
						// getConfidenceLevelmatch
						// TableRows+=1  
						// If ConfidenceLevel > 0
							// Increment MatchedRows in PerformanceResult
							// Place Row in StringBuilder

					// GetEndTime -> PerformanceResult
				// Add PerformanceResult to Performance Summary



		return 0;
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