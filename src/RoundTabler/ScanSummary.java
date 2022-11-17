package RoundTabler;

import java.util.ArrayList;

public class ScanSummary{

	private ArrayList<RoundTabler.ScanResult> ScannerResults = new ArrayList<RoundTabler.ScanResult>();

	public ScanSummary(){
		super();
		}

	public void addResult( RoundTabler.ScanResult NewResult) {
		ScannerResults.add( NewResult );
	}

	public String toString() {
		StringBuilder temp;
		int Index;
		temp = new StringBuilder();
		for( Index=0; Index < ScannerResults.size(); Index++ ){
			temp.append( ScannerResults.get(Index).toString() + "\n");
		}
		return temp.toString();
	}

}