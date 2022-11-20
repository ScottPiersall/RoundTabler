package RoundTabler;

import RoundTabler.db.DBReader;
import RoundTabler.db.ReaderMaker;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;


public class RoundTable {

    public static void main(String[] args){
        round(args);
    }

    public static int round(String[] args){

        Configuration config = new Configuration();

        DBReader reader;

        PerformanceSummary SummaryOfPerformance = new PerformanceSummary();
        ScanSummary SummaryOfScans = new ScanSummary();

        try{

            //Iterate through all args given through the command line.

            for (String elem : args) {
                String[] argParts = elem.split("=");

                if(elem.contains("--help")){
                    System.out.println();
                    System.out.println(
                            "REQUIRED PARAMETERS:\n" +
                                    "\t--type= Options include all, pci, nacha\n" +
                                    "\t--dbtype= Only option currently available is mariadb\n" +
                                    "\t--server= IP address of database. (Optional) include a colon followed by the port number i.e., 127.0.0.1:3306\n" +
                                    "\t--user= Your username to log into the database\n" +
                                    "\t--password= Your password to log into the database\n" +
                                    "\t--database= The name of the database you want to scan\n" +
                                    "\nOPTIONAL PARAMETERS:\n" +
                                    "\t--table= Specify one table you would like to have scanned instead of the entire database\n"
                    );
                    return -1;
                }

                //If any parameters are left empty i.e. "--server=" then throw exception and stop.
                if(argParts.length == 1)throw(new InputMismatchException("Must provide an argument for parameter flag: " + argParts[0].replace("--", "")));

                //Initialize Config fields for easy access to all arguments needed for execution.
                switch (argParts[0]) {
                    case "--type":
                        config.setType(argParts[1]);
                        if(!config.validateScanType())throw (new InputMismatchException("Invalid scan type of: " + argParts[1] + "."));
                        break;
                    case "--dbtype":
                        config.setDbType(argParts[1]);
                        if(!config.validateDbType())throw (new InputMismatchException("Invalid database type of: " + argParts[1] + "."));
                        break;
                    case "--server":
                        config.setServer(argParts[1]);
                        break;
                    case "--user":
                        config.setUser(argParts[1]);
                        break;
                    case "--password":
                        config.setPassword(argParts[1]);
                        break;
                    case "--database":
                        config.setDatabase(argParts[1]);
                        break;
                    case "--table":
                        config.setTable(argParts[1]);
                        break;
                    default:
                        //If a argument does not match any of the valid argument types then throw exception and stop.
                        throw(new InputMismatchException("Invalid parameter field of type " + argParts[0].replace("--", "") + "."));
                }
            }

            //Ensures all required parameters needed for RoundTabler to run are filled out. THIS DOES NOT ENSURE --resultfile IS FILLED OUT BECAUSE ITS NOT A REQUIRED PARAMETER
            if(!config.allFilled())throw(new InputMismatchException("Missing required parameter flag: " + config.missingParameter + "."));

            //Print Statements signifying next step in RoundTabler process... can be removed just used as a checkpoint to ensure all arg checking is complete.
            System.out.println();
            System.out.println("ALL REQUIRED PARAMETERS FULFILLED...");

            System.out.println();
            System.out.println("INITIALIZING DATABASE CONNECTION");


            // Technically a bad practice to just create this factory and forget about it,
            // but we do not need it for anything else after it makes our single reader.

            reader = ReaderMaker.getReader(config);

            String scanType = config.getType().toUpperCase().trim();

            CommonScan scan = new CommonScan(config, SummaryOfPerformance, SummaryOfScans, reader);
            try {
                scan.scanMariaDB(scanType);
            } catch (SQLException sqlex) {
                System.out.println("DEBUG: " + sqlex);
            }

            WriteResultsToHTMLFile(SummaryOfScans, SummaryOfPerformance, config);

            return 0;

        }catch(InputMismatchException | IllegalAccessException e){

            String filePath = "";

            /*
                Still need to iterate through all args even if one is empty because --resultfile could still be filled out i.e.

                    java RoundTabler.RoundTable --type=All --dbtype=mysql --server= --user=username --password=1234 --database=DBToTest --resultfile=C://test

                --server being empty would throw an exception before --resultfile is read. so need to iterate through rest of args in order to print to the requested result file.
            */

            for (String elem : args) {
                if(elem.contains("--resultfile") && elem.split("=").length > 1){
                    filePath = elem.split("=")[1];
                    break;
                }
            }

            new HTMLErrorOut(e.getMessage());

            return -1;

        }
        catch ( SQLException sqlex ) {
            System.out.println(sqlex);
            return -1;
        }
        catch ( ClassNotFoundException cnfex ) {
            System.out.println(cnfex); // The .toString() is implicit
            return -1;
        }
        catch ( Exception ex ) {
            System.out.println(ex);
            return -1;
        }
    }

    /**
     *
     *
     *

     */
    public static void WriteResultsToHTMLFile( ScanSummary Scans, PerformanceSummary Performance, Configuration config ){
        StringBuilder sbHTML = new StringBuilder();


        sbHTML.append("<HTML><BODY><TITLE>RoundTabler Results for </TITLE><BR><BR><CENTER><BR>");

        sbHTML.append("<h1>RoundTabler Scan Resuls for</h1><br>\n");
        sbHTML.append("<h2>Database " + config.getDatabase() + "</h2><br>\n");
        sbHTML.append("<h2>on host " + config.getServer() + "</h2><br>\n");
        sbHTML.append("<h2>scan type " + config.getType() + "</h2></br>\n");


        sbHTML.append("<h2>Scan Results</h2><BR>\n");
        sbHTML.append("<TABLE BORDER=\"2\">");
        sbHTML.append("<TR><TH>Table Name</TH>" +
        "<TH>Table Column</TH>"+
        "<TH>Match Type</TH>"+
        "<TH>Row Data Match</TH>" +
        "<TH>Confidence Level</TH>" +
        "<TH>Match Rule(s)</TH>" +
        "</TR>");
        sbHTML.append( Scans.toString() );
        sbHTML.append("</TABLE><BR><BR>");

        sbHTML.append("\n\n");

        sbHTML.append("<h2>Scan Performance Summary</h2><BR>\n");

        sbHTML.append("<TABLE BORDER=\"2\">");
        sbHTML.append("<TR><TH>Table Name</TH>" +
        "<TH>Column Name</TH>"+
        "<TH>Scan Type</TH>"+
        "<TH>Rows Scanned</TH>" +
        "<TH>Rows Matched</TH>" +
        "<TH>Rows/Second</TH><TR>" + "\n" );


        sbHTML.append( Performance.toString() );
        sbHTML.append("</TABLE><BR><BR>");

        sbHTML.append("</CENTER></BODY></HTML>");

        //
        // If no filename was specified in configuration, we write to standard output
        // otherwise, we write to the filename provided in the config
        //


        String saveFileName;
        String dbName = config.getDatabase();
        // Remove any characters in database name which are illegal in a fileNAme
        dbName = dbName.replaceAll("[^a-zA-Z0-9]", "_");
        saveFileName =
        "RESULTS_" + dbName + "_" +
        String.format("%d%02d%02d_%02d%02d%02d.html",
        LocalDateTime.now().getYear(),
        LocalDateTime.now().getMonthValue(),
        LocalDateTime.now().getDayOfMonth(),
        LocalDateTime.now().getHour(),
        LocalDateTime.now().getMinute(),
        LocalDateTime.now().getSecond()   );

        try(
            FileWriter fileW = new FileWriter("html/" + saveFileName );
            BufferedWriter writer = new BufferedWriter( fileW )
        ) {
            writer.write(sbHTML.toString() );
            System.out.println("DEBUG: Results written to " +  saveFileName );
        }
        catch (Exception ex ){
            System.out.println("Could not create results files " + ex);
        }

        WriteResultNameToJSFile(saveFileName);

    }

    public static int WriteResultNameToJSFile(String fileName){

        File resultList = new File("html/resultList.js");

        try {
            Scanner reader = new Scanner(resultList);
            String list = "";

            while(reader.hasNext()){
                list = reader.next();
            }

            reader.close();

            String[] parts = list.split("\"");

            parts[0] = "export default \"";

            parts[1] = parts[1].replace("+errorLog.html", "");

            parts[1] += "+" + fileName + "\"";

            list = String.join("", parts);

            FileWriter writer = new FileWriter( resultList);

            writer.write(list);

            writer.close();

            return 0;

        }catch(IOException e) {

            new HTMLErrorOut("There was an error updating the result list. To view your results please visit localhost:8000/" + fileName);
            return -1;

        }
    }
}
