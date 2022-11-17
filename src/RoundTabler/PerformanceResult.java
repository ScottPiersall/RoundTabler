package RoundTabler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class PerformanceResult{

	public String TableName;
	public String TableColumn;
	public int RowsScanned;
	public int RowsMatched;
	public String MatchType;    //"PCIDSS or NACHA"
	public LocalDateTime	ScanStarted;
	public LocalDateTime	ScanFinished;
	public Double			RowsPerSecond;

	public PerformanceResult(){
		super();

	}

	private void CalculatePerformance(){
		long elapsed = ChronoUnit.SECONDS.between( ScanStarted, ScanFinished);
		if ( elapsed != 0 ) { 
			Double dRows = Double.valueOf(RowsScanned);
			Double dElapsed = Double.valueOf(elapsed);
			RowsPerSecond = dRows / dElapsed; 
					
		} else { RowsPerSecond = 0.0;}
		if ( ( RowsPerSecond == 0.0 ) && ( RowsScanned > 0 ) ) {
			RowsPerSecond = Double.valueOf( RowsScanned);
		}
	}

	
	public String toString(){
		this.CalculatePerformance();
	return 
	"<TR><TD>" + TableName + "</TD>" +
	    "<TD>" + TableColumn + "</TD>"+
		"<TD>" + MatchType + "</TD>"+
		"<TD ALIGN=\"RIGHT\">" + String.format("%,d", RowsScanned ) + "</TD>" +
		"<TD ALIGN=\"RIGHT\">" + String.format("%,d", RowsMatched )+ "</TD>" +
		"<TD ALIGN=\"RIGHT\">" + String.format("%.2f", RowsPerSecond ) + "</TD>" + 
		
		"</TR>";

	}


}