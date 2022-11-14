package RoundTabler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//
// A Class used for doing PCIScan of fields
// for protected cards.
//
public class PCIScan {

	// Match potential card numbers
	// by any sequence of digits between 13 and 16 characters in length
	// with embedded dashes
	static String CardNumberSequenceRegex = "\\b(?:\\d[-]*?){13,16}\\b";
	static Pattern CardNumberPattern = Pattern.compile(CardNumberSequenceRegex);

	public String JDBC_DRIVER;

	private RoundTabler.Configuration pScanConfiguration;

	private Connection pDBConnection;
	private String pTableName; 
	private String pMYSQLColumnSelect;
	private String pMYSQLColumnTypes;

	private PerformanceSummary pPerformanceSummary;

	private StringBuilder psbResults;

	private int pLastMatchStart;
	private int pLastMatchEnd;


	public PCIScan(){
		super();
		this.pMYSQLColumnTypes = "(mediumtext, longtext, text, tinytext, varchar)";
		this.psbResults = new StringBuilder();
		psbResults.append("\n");
	}

	public PCIScan(RoundTabler.Configuration ScanConfiguration, RoundTabler.PerformanceSummary Summary) {
		this();
		pScanConfiguration = ScanConfiguration;
		pPerformanceSummary = Summary;
	}

	public String ScanResult() { return this.psbResults.toString(); }

	public int ScanMariaDB() throws SQLException {
		//
		// Performs MySQL-Based Scan
		// using the settings and configuration 
		// contained in pScanConfiguration
		if ( pScanConfiguration.getDbType().toUpperCase().compareTo("MARIADB") != 0 ) {
			new HTMLErrorOut(pScanConfiguration.getFile(), "Database Type Mismatch. Database Type Configuration " + pScanConfiguration.getDbType() + " cannot be used with MySQL Scan" );
			return 0;
		}

	
		int currentConfidenceLevel = 0;
		String currentTable = "";
		String currentColumn = "";

		String currentRow = "";
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


							currentConfidenceLevel = getConfidenceLevelMatch(currentRow);
							if ( currentConfidenceLevel > 0 ) {

								psbResults.append("<TR>");
							
								psbResults.append("<TD>");
								psbResults.append(currentTable);
								psbResults.append("</TD>");

								psbResults.append("<TD>");
								psbResults.append(currentRow);
								psbResults.append("</TD>");

								psbResults.append("<TD>");
								psbResults.append(insertStrongEmphasisInto(currentRow, pLastMatchStart, pLastMatchEnd));
								psbResults.append("</TD>");

								psbResults.append("<TD ALIGN =\"RIGHT\">");
								psbResults.append(currentConfidenceLevel );
								psbResults.append("</TD>");

								psbResults.append("</TR>");
							}

					// GetEndTime -> PerformanceResult
				// Add PerformanceResult to Performance Summary
		return 0;
	}


	/**
	*
	* Returns a string with strong and emphasis tags surrounding the StartLocation 
	* and EndLocation 
	*
	*/
	private String insertStrongEmphasisInto( String MatchedRow, int StartLocation, int EndLocation){
		StringBuilder tsb = new StringBuilder(MatchedRow);
		tsb.insert(EndLocation, "</EM></STRONG>");
		tsb.insert(StartLocation, "<STRONG><EM>");
		return tsb.toString();
	}



	private int getConfidenceLevelMatch(String DatabaseRow ) {
		int result = 0;
		
		Matcher CardNumberSequenceMatcher = CardNumberPattern.matcher( DatabaseRow );


		// If we find what looks like a card sequence, make the confidence 75
		if (CardNumberSequenceMatcher.find()) {
			result += 75;   // Assign a confidence Level of at least 75%
			// If the match passes LuhnsTest, Boost Confidence to 100
			if (LuhnTest.Validate(DatabaseRow.substring(CardNumberSequenceMatcher.start(), CardNumberSequenceMatcher.end())))
				result += 25;
		
			pLastMatchStart = CardNumberSequenceMatcher.start();
			pLastMatchEnd = CardNumberSequenceMatcher.end();

		}

		return result;
	}

}