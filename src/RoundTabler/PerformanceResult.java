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
			Double dRows = new Double(RowsScanned);
			Double dElapsed = new Double(elapsed);
			RowsPerSecond = dRows / dElapsed; 
					
		} else { RowsPerSecond = 0.0;}

	}

	
	public String toString(){
		this.CalculatePerformance();
	return 
	"<TR><TD>" + TableName + "</TD>" +
	    "<TD>" + TableColumn + "</TD>"+
		"<TD>" + MatchType + "</TD>"+
		"<TD>" + String.format("%,d", RowsScanned ) + "</TD>" +
		"<TD>" + String.format("%,d", RowsMatched )+ "</TD>" +
		"<TD>" + String.format("%2f", RowsPerSecond ) + "</TD>" + 
		
		"</TR>";"

	}


}