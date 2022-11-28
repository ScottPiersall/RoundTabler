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

            reader = ReaderMaker.getReader(config);

            String scanType = config.getType().toUpperCase().trim();

            CommonScan scan = new CommonScan(config, SummaryOfPerformance, SummaryOfScans, reader);
            try {
                scan.scanMariaDB(scanType);
            } catch (SQLException sqlex) {
                return -1;
            }

            // Should not write empty results
            // Checking performance because it records results even when no matches occur
            if (!SummaryOfPerformance.isEmpty()) {
                WriteResultsToHTMLFile(SummaryOfScans, SummaryOfPerformance, config);
                return 0;
            }else {
                new HTMLErrorOut("No scannable content was found in the specified database/table.");
                return -1;
            }

        }catch(InputMismatchException | IllegalAccessException | SQLException | ClassNotFoundException e){

            String filePath = "";

            for (String elem : args) {
                if(elem.contains("--resultfile") && elem.split("=").length > 1){
                    filePath = elem.split("=")[1];
                    break;
                }
            }

            new HTMLErrorOut(e.getMessage());

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


        sbHTML.append("<HTML><BODY style=\"margin: 0; font-family: 'monospace', sans-serif;\"><TITLE>Results</TITLE>");

        sbHTML.append("<div style=\"display: flex; flex: 1; flex-direction: row; width: 100%; background: linear-gradient(to left, #f2c413, #FFFFFF); align-items: flex-start; justify-content: space-between; padding-inline: 15px; box-sizing: border-box; flex-wrap: wrap;\"><div>");
        sbHTML.append("<h1>RoundTabler Scan Results for:</h1>\n");
        sbHTML.append("<h2>Database: <span style=\"font-weight: lighter;\">" + config.getDatabase() + "</span></h2>\n");
        sbHTML.append("<h2>Server: <span style=\"font-weight: lighter;\">" + config.getServer() + "</span></h2>\n");
        sbHTML.append("<h2>Scan Type: <span style=\"font-weight: lighter;\">" + config.getType() + "</span></h2>\n");

        if(!config.getTable().equals("")){
            sbHTML.append("<h2>Table: <span style=\"font-weight: lighter;\">" + config.getTable() + "</span></h2></div>\n");
        }else{
            sbHTML.append("</div>");
        }

        sbHTML.append("<div style=\"display: flex; flex: 1; align-items: center; justify-content: flex-end; padding: 15px; min-height: 100%;\">");
        sbHTML.append("<svg id=\"Layer_1\" style=\"width: 10%;\" data-name=\"Layer 1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" viewBox=\"0 0 185.52 157.33\">\n" +
                "                <defs>\n" +
                "                    <linearGradient id=\"linear-gradient\" x1=\"100.3\" y1=\"49.34\" x2=\"161.56\" y2=\"62.51\" gradientTransform=\"translate(-1.53 2.19) rotate(-1.16)\" gradientUnits=\"userSpaceOnUse\">\n" +
                "                    <stop offset=\"0\" stop-color=\"#fff\" stop-opacity=\".6\"/>\n" +
                "                    <stop offset=\".43\" stop-color=\"#fff\" stop-opacity=\".3\"/>\n" +
                "                    <stop offset=\"1\" stop-color=\"#fff\" stop-opacity=\".25\"/>\n" +
                "                    </linearGradient>\n" +
                "                </defs>\n" +
                "                <g>\n" +
                "                    <circle cx=\"107\" cy=\"78.5\" r=\"78.5\" fill=\"#a67c52\"/>\n" +
                "                    <path d=\"M147.94,11.51c22.53,13.8,37.56,38.64,37.56,66.99,0,43.35-35.15,78.5-78.5,78.5-18.17,0-34.9-6.17-48.2-16.54\" fill=\"#a67c52\"/>\n" +
                "                </g>\n" +
                "                <g>\n" +
                "                    <g>\n" +
                "                    <rect x=\"120.09\" y=\"107.52\" width=\"8\" height=\"20\" transform=\"translate(-45.38 95.38) rotate(-36)\" fill=\"#333\"/>\n" +
                "                    <g>\n" +
                "                        <polyline points=\"116.45 107 111.59 110.53 58.69 37.71 63.55 34.19\" fill=\"#b3b3b3\"/>\n" +
                "                        <polyline points=\"63.55 34.19 68.4 30.66 121.3 103.47 116.45 107 63.55 34.19 58.69 37.71 58.75 27.58\" fill=\"#b3b3b3\"/>\n" +
                "                        <polygon points=\"58.75 27.58 68.4 30.66 63.55 34.19 58.75 27.58\" fill=\"#b3b3b3\"/>\n" +
                "                        <g>\n" +
                "                        <line x1=\"58.89\" y1=\"27.77\" x2=\"116.45\" y2=\"107\" fill=\"none\" stroke=\"#999\" stroke-linejoin=\"bevel\" stroke-width=\".5\"/>\n" +
                "                        <polygon points=\"59.13 27.64 58.69 27.96 58.75 27.58 59.13 27.64\" fill=\"#999\"/>\n" +
                "                        </g>\n" +
                "                    </g>\n" +
                "                    <g>\n" +
                "                        <g>\n" +
                "                        <circle cx=\"103.11\" cy=\"118.77\" r=\"2.29\" fill=\"#f2c413\"/>\n" +
                "                        <path d=\"M118.78,110.21s-6.68,3.44-7.61,4.11-5.94,5.27-5.94,5.27l-2.25-3.1s3.82-.41,6.84-4.02,5.59-6.89,5.59-6.89\" fill=\"#f2c413\"/>\n" +
                "                        </g>\n" +
                "                        <g>\n" +
                "                        <ellipse cx=\"133.2\" cy=\"96.91\" rx=\"2.51\" ry=\"2.29\" transform=\"translate(-31.52 96.8) rotate(-36)\" fill=\"#f2c413\"/>\n" +
                "                        <path d=\"M118.78,110.21s5.94-5.73,6.96-6.47,7.48-4.48,7.48-4.48l-2.25-3.1s-1.84,3.7-6.58,5.73-8.97,3.69-8.97,3.69\" fill=\"#f2c413\"/>\n" +
                "                        </g>\n" +
                "                    </g>\n" +
                "                    <circle cx=\"131.14\" cy=\"127.22\" r=\"5\" fill=\"#b3b3b3\"/>\n" +
                "                    </g>\n" +
                "                    <g opacity=\".1\">\n" +
                "                    <path d=\"M147.95,11.84c22.53,13.8,37.56,38.64,37.56,66.99,0,43.35-35.15,78.5-78.5,78.5-18.17,0-34.9-6.17-48.2-16.54\"/>\n" +
                "                    </g>\n" +
                "                    <g>\n" +
                "                    <circle cx=\"129.81\" cy=\"55.32\" r=\"31.32\" fill=\"url(#linear-gradient)\" stroke=\"#333\" stroke-miterlimit=\"10\" stroke-width=\"3\"/>\n" +
                "                    <rect x=\"62.51\" y=\"99.42\" width=\"62.02\" height=\"9.4\" rx=\"3\" ry=\"3\" transform=\"translate(-45.65 118.9) rotate(-54.13)\" fill=\"#333\"/>\n" +
                "                    </g>\n" +
                "                </g>\n" +
                "                </svg></div>");


        sbHTML.append("</div><div style=\"padding-inline: 15px;\"><h2>Scan Results:</h2><BR>\n");

        if(Scans.toString().length() != 0) {

            sbHTML.append("<center><TABLE style=\"font-family: 'monospace', sans-serif;\" BORDER=\"2\">");
            sbHTML.append("<TR><TH>Table Name</TH>" +
                    "<TH>Table Column</TH>" +
                    "<TH>Match Type</TH>" +
                    "<TH>Row Data Match</TH>" +
                    "<TH>Confidence Level</TH>" +
                    "<TH>Match Rule(s)</TH>" +
                    "</TR>");
            sbHTML.append(Scans.toString());
            sbHTML.append("</TABLE></center><BR><BR>");
        }else{
            sbHTML.append("<h3 style=\"text-align: center;\">No unencrypted credit card or ACH information found.</h3><BR>\n");
        }

        sbHTML.append("\n\n");

        sbHTML.append("<h2>Scan Performance Summary:</h2><BR><CENTER>\n");

        sbHTML.append("<TABLE style=\"font-family: 'monospace', sans-serif;\"  BORDER=\"2\">");
        sbHTML.append("<TR><TH>Table Name</TH>" +
        "<TH>Column Name</TH>"+
        "<TH>Scan Type</TH>"+
        "<TH>Rows Scanned</TH>" +
        "<TH>Rows Matched</TH>" +
        "<TH>Rows/Second</TH><TR>" + "\n" );


        sbHTML.append( Performance.toString() );
        sbHTML.append("</TABLE><BR><BR>");

        sbHTML.append("</CENTER></div></BODY></HTML>");

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
