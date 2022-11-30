package RoundTabler;

import java.util.ArrayList;

/*
 * Class/structure that contains all ScanResults
*/

public class ScanSummary{

    private ArrayList<RoundTabler.ScanResult> ScannerResults = new ArrayList<RoundTabler.ScanResult>();

    public ScanSummary() {
        super();
    }

    public void addResult( RoundTabler.ScanResult NewResult ) {
        ScannerResults.add( NewResult );
    }

    public String toString() {
        StringBuilder temp;
        int index;
        temp = new StringBuilder();
        
        for( index=0; index < ScannerResults.size(); index++ ){
            temp.append( ScannerResults.get(index).toString() + "\n" );
        }

        return temp.toString();
    }
}