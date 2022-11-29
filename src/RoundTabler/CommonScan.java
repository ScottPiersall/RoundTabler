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

    public void scanMariaDB(String typeOfScan) throws SQLException {

        if (nScanConfiguration.getDbType().toUpperCase().compareTo("MARIADB") != 0) {
            new HTMLErrorOut("Database Type Mismatch. Database Type Configuration "
                    + nScanConfiguration.getDbType() + " cannot be used with a MariaDB Database");
            throw new SQLException();
        }

        int currentConfidenceLevel = 0;
        String currentTable;
        String currentColumn;

        String currentRow;

        SchemaItems tablesAndColumns;
        if (nDBReader.readSchema()) {
            int i = 0;
            int endIteration = 0;
            if (Objects.equals(typeOfScan, "NACHA")) {
                i = 1;
                endIteration = 1;
            } else if (Objects.equals(typeOfScan, "ALL")) {
                endIteration = 1;
            }
            tablesAndColumns = nDBReader.getSchemaItems();
            int WorkSize;
            WorkSize = tablesAndColumns.size() * ( endIteration + 1 );
            int StepCount = 0;
            String progressBar = "|";
            System.out.println("\nSCANNING...");
            System.out.println();
            while (i <= endIteration) {
                int index;
                String scanType = "";
                int PercentCompleted = 0;
                int PreviousPercentCompleted = 0;
                for (index = 0; index < tablesAndColumns.size(); index++) {
                    StepCount += 1;
                    currentTable = tablesAndColumns.get(index).getTableName();
                    currentColumn = tablesAndColumns.get(index).getColumnName();
                    PercentCompleted = ( StepCount * 100 ) / WorkSize;
                    if ( ( PercentCompleted - PreviousPercentCompleted)  >= 10 ) {
                        PreviousPercentCompleted = ( PercentCompleted / 10 ) * 10;
                        progressBar += "##";
                        //System.out.println( String.format("%d", PreviousPercentCompleted) + "% Completed");
                        if(PreviousPercentCompleted != 100) {
                            System.out.print(progressBar + "|\r");
                            System.out.print("\t\t\t" + PreviousPercentCompleted + "%\r");
                        }
                        else{
                            System.out.println("|####################| 100%");

                        }
                    }

                    ArrayList<String> rowsData;
                    rowsData = nDBReader.readColumn(tablesAndColumns.get(index));
                    int rowindex;
                    PerformanceResult currentResult;
                    currentResult = new PerformanceResult();
                    currentResult.TableName = currentTable;
                    currentResult.TableColumn = currentColumn;
                    if (i == 0) {
                        currentResult.MatchType = "PCIDSS";
                        scanType = "PCIDSS";
                    } else if (i == 1) {
                        currentResult.MatchType = "NACHA";
                        scanType = "NACHA";
                    }
                    currentResult.RowsMatched = 0;
                    currentResult.RowsScanned = rowsData.size();
                    currentResult.ScanStarted = LocalDateTime.now();

                    for (rowindex = 0; rowindex < rowsData.size(); rowindex++) {
                        currentRow = rowsData.get(rowindex);

                        if (i == 0) {
                            currentConfidenceLevel = pciScan.getConfidenceLevelMatch(rowsData.get(rowindex));
                        } else if (i == 1) {
                            currentConfidenceLevel = nachaScan.getConfidenceLevelMatch(rowsData.get(rowindex));
                        }

                        if (currentConfidenceLevel > 0) {
                            AppendMatch(scanType, currentTable, currentColumn, currentRow, currentConfidenceLevel);
                            currentResult.RowsMatched++;
                        }
                    }
                    currentResult.ScanFinished = LocalDateTime.now();
                    nPerformanceScan.addResult(currentResult);
                }
                i++;
            }
        }
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
        tsb.insert(EndLocation, "</SPAN></EM></STRONG>");
        tsb.insert(StartLocation, "<STRONG><EM><SPAN STYLE=\"background-color: #FFFF00\">");
        return tsb.toString();
    }
}
