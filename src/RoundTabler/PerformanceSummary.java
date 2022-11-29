package RoundTabler;

import java.util.ArrayList;

public class PerformanceSummary{

	private ArrayList<RoundTabler.PerformanceResult> PerfResults = new ArrayList<>();

	public PerformanceSummary(){
		super();
	}

	public void addResult( RoundTabler.PerformanceResult NewResult) {
		PerfResults.add( NewResult );
	}

	public String toString() {
		StringBuilder temp;
		int Index;
		temp = new StringBuilder();
		for( Index=0; Index < PerfResults.size(); Index++ ){
			temp.append( PerfResults.get(Index).toString() + "\n");
		}
		return temp.toString();
	}

	public String getSummaryRow() {
		StringBuilder temp;
		long RowsScanned=0;
		long RowsMatched=0;
		int index;
		temp = new StringBuilder();

		temp.append("<TR><TH COLSPAN=\"3\">Totals</TH>");

		for( index =0; index < PerfResults.size(); index++ ) {
			RowsScanned += PerfResults.get(index).RowsScanned;
			RowsMatched += PerfResults.get(index).RowsMatched;
		}
		temp.append("<TH ALIGN=\"RIGHT\">" + String.format("%,d", RowsScanned ) + "</TH>");
		temp.append("<TH ALIGN=\"RIGHT\">" + String.format("%,d", RowsMatched ) + "</TH>");			
		temp.append("<TH></TH>");			

		temp.append("</TR>");
		temp.append("\n");
		return temp.toString();
	}



	public Boolean isEmpty() {
		return PerfResults.isEmpty();
	}

}

