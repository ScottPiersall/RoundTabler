package RoundTabler;

import RoundTabler.db.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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


	static String CardPartialSequenceRegex = "\\b(AMEX|VISA|MC)-\\d{4}\\b";
	static Pattern CardPartialPattern = Pattern.compile(CardPartialSequenceRegex, Pattern.CASE_INSENSITIVE);

	private RoundTabler.Configuration pScanConfiguration;
	private PerformanceSummary pPerformanceSummary;
	private ScanSummary pScanSummary;
	private StringBuilder psbResults;

	private int pLastMatchStart;
	private int pLastMatchEnd;
	private DBReader pDBReader;

	public PCIScan(){
		super();
		this.psbResults = new StringBuilder();
		psbResults.append("\n");
	}

	public PCIScan(RoundTabler.Configuration ScanConfiguration, RoundTabler.PerformanceSummary Summary, RoundTabler.ScanSummary Scans, DBReader DatabaseReader ) {
		this();
		pScanConfiguration = ScanConfiguration;
		pPerformanceSummary = Summary;
		pScanSummary = Scans;
		pDBReader = DatabaseReader;
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

		SchemaItems tablesandcolumns;
	
		if ( pDBReader.readSchema() ) 
		{
			tablesandcolumns = pDBReader.getSchemaItems();
			int index;
			for( index = 0; index < tablesandcolumns.size(); index ++ ){
				currentTable = tablesandcolumns.get(index).getTableName();
				currentColumn = tablesandcolumns.get(index).getColumnName();
				ArrayList<String> rowsData;
				rowsData  = pDBReader.readColumn( tablesandcolumns.get(index) );
				int rowindex;
				PerformanceResult currentResult;
				currentResult = new PerformanceResult();
				currentResult.TableName = currentTable;
				currentResult.TableColumn = currentColumn;
				currentResult.MatchType = "PCIDSS";
				currentResult.RowsMatched = 0;
				currentResult.RowsScanned = rowsData.size();
				currentResult.ScanStarted = LocalDateTime.now();
	
				for (rowindex =0; rowindex < rowsData.size(); rowindex++ ){
					currentRow = rowsData.get(rowindex).toString();

					currentConfidenceLevel = getConfidenceLevelMatch( rowsData.get(rowindex).toString() );

					if ( currentConfidenceLevel > 0 ) { 
							AppendMatch( currentTable, currentColumn, currentRow, currentConfidenceLevel, "");
							currentResult.RowsMatched++;
							}
				}

				currentResult.ScanFinished = LocalDateTime.now();
				pPerformanceSummary.addResult(currentResult);

			}
		} else {
			
		}
		return 0;
	}


	private void AppendMatch( String currentTable, String currentColumn, String currentRow, int currentConfidenceLevel, String matchDescription) {
			ScanResult tResult;
			tResult = new ScanResult();
			tResult.ConfidenceLevel = currentConfidenceLevel;
			tResult.MatchType = "PCIDSS";
			tResult.TableName = currentTable;
			tResult.TableColumn = currentColumn;
			tResult.HTMLEmphasizedResult = insertStrongEmphasisInto(currentRow, pLastMatchStart, pLastMatchEnd);
			tResult.MatchResultRule = matchDescription;
			pScanSummary.addResult( tResult );
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



	public int getConfidenceLevelMatch(String DatabaseRow ) {
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

		if ( result == 0 ){
			Matcher CardPartialSequenceMatcher = CardPartialPattern.matcher( DatabaseRow );
			if ( CardPartialSequenceMatcher.find() ) {
				result = 100;
				pLastMatchStart = CardPartialSequenceMatcher.start();
				pLastMatchEnd = CardPartialSequenceMatcher.end();
			}
		}	

		return result;
	}

}