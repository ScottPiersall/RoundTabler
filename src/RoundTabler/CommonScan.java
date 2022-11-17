package RoundTabler;

import RoundTabler.db.DBReader;
import RoundTabler.db.SchemaItems;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class CommonScan {

    NACHAScan nachaScan = new NACHAScan();
    PCIScan pciScan = new PCIScan();

    private Configuration nScanConfiguration;
    private PerformanceSummary nPerformanceScan;
    private ScanSummary pScanSummary;
    private StringBuilder nsbResults;

    private DBReader nDBReader;

    public CommonScan(){
        super();
        this.nsbResults = new StringBuilder();
        nsbResults.append("\n");
    }

    public CommonScan(Configuration scanConfiguration, PerformanceSummary performanceSummary,
                     ScanSummary scans, DBReader databaseReader){
        this();
        nScanConfiguration = scanConfiguration;
        nPerformanceScan = performanceSummary;
        pScanSummary = scans;
        nDBReader = databaseReader;
    }

    public int ScanMariaDB(String scanType) throws SQLException {
        if (nScanConfiguration.getDbType().toUpperCase().compareTo("MARIADB") != 0) {
            new HTMLErrorOut(nScanConfiguration.getFile(), "Database Type Mismatch. Database Type Configuration "
                    + nScanConfiguration.getDbType() + " cannot be used with MySQL Scan" );
            return 0;
        }

        int currentConfidenceLevel = 0;
        String currentTable = "";
        String currentColumn = "";

        String currentRow = "";

        SchemaItems tablesAndColumns;
        if (nDBReader.readSchema()){
            tablesAndColumns = nDBReader.getSchemaItems();
            int index;
            for (index = 0; index < tablesAndColumns.size(); index++){
                currentTable = tablesAndColumns.get(index).getTableName();
                currentColumn = tablesAndColumns.get(index).getColumnName();
                ArrayList<String> rowsData;
                rowsData  = nDBReader.readColumn( tablesAndColumns.get(index) );
                int rowindex;
                PerformanceResult currentResult;
                currentResult = new PerformanceResult();
                currentResult.TableName = currentTable;
                currentResult.TableColumn = currentColumn;
                if (Objects.equals(scanType, "NACHA")){
                    currentResult.MatchType = "NACHA";
                }
                else if (Objects.equals(scanType, "PCIDSS")){
                    currentResult.MatchType = "PCIDSS";
                }
                currentResult.RowsMatched = 0;
                currentResult.RowsScanned = rowsData.size();
                currentResult.ScanStarted = LocalDateTime.now();

                for (rowindex =0; rowindex < rowsData.size(); rowindex++ ){
                    currentRow = rowsData.get(rowindex);

                    if (Objects.equals(scanType, "NACHA")){
                        currentConfidenceLevel = nachaScan.getConfidenceLevelMatch(rowsData.get(rowindex));
                    }
                    else if (Objects.equals(scanType, "PCIDSS")) {
                        currentConfidenceLevel = pciScan.getConfidenceLevelMatch(rowsData.get(rowindex));
                    }

                    if ( currentConfidenceLevel > 0 ) {
                        AppendMatch(scanType, currentTable, currentColumn, currentRow, currentConfidenceLevel);
                        currentResult.RowsMatched++;
                    }
                }
                currentResult.ScanFinished = LocalDateTime.now();
                nPerformanceScan.addResult(currentResult);
            }
        }

        return 0;
    }

    private void AppendMatch(String scanType, String currentTable, String currentColumn, String currentRow,
                             int currentConfidenceLevel) {
        ScanResult tResult;
        tResult = new ScanResult();
        tResult.ConfidenceLevel = currentConfidenceLevel;
        tResult.TableName = currentTable;
        if (Objects.equals(scanType, "NACHA")){
            tResult.MatchType = "NACHA";
            tResult.HTMLEmphasizedResult = insertStrongEmphasisInto(currentRow,
                    nachaScan.getLastMatchStart(), nachaScan.getLastMatchEnd());
            tResult.MatchResultRule = nachaScan.getLastMatchDescription();
        } else if (Objects.equals(scanType, "PCIDSS")) {
            tResult.MatchType = "PCIDSS";
            tResult.HTMLEmphasizedResult = insertStrongEmphasisInto(currentRow,
                    pciScan.getLastMatchStart(), pciScan.getLastMatchEnd());
            tResult.MatchResultRule = pciScan.getLastMatchDescription();
        }
        tResult.TableColumn = currentColumn;
        pScanSummary.addResult( tResult );

    }

    /**
     *
     * Returns a string with strong and emphasis tags surrounding the StartLocation
     * and EndLocation
     *
     */
    private String insertStrongEmphasisInto(String MatchedRow, int StartLocation, int EndLocation){
        StringBuilder tsb = new StringBuilder(MatchedRow);
        tsb.insert(EndLocation, "</EM></STRONG>");
        tsb.insert(StartLocation, "<STRONG><EM>");
        return tsb.toString();
    }
}