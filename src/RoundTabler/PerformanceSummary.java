package RoundTabler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.ArrayList;

public class PerformanceSummary{

	private ArrayList<RoundTabler.PerformanceResult> PerfResults = new ArrayList<RoundTabler.PerformanceResult>();

	public PerformanceSummary(){
		super();
		}

	public void AddResult( RoundTabler.PerformanceResult NewResult) {
		PerfResults.add( NewResult );
	}

	public String toString() {
		StringBuilder temp;
		int Index;
		temp = new StringBuilder();
		for( Index=1; Index <= PerfResults.size(); Index++ ){
			temp.append( PerfResults.get(Index).toString + "\n");
		}
		return temp.toString();
	}

}

