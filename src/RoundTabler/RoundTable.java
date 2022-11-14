package RoundTabler;

import RoundTabler.db.MariaReader;

import java.sql.SQLException;
import java.util.InputMismatchException;


public class RoundTable {

    public static void main(String[] args){

        Configuration config = new Configuration();

        PerformanceSummary SummaryOfPerformance = new PerformanceSummary();


        try {

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
                                    "\t--resultfile= Include a file path to store the results or any error logs\n" +
                                    "\t--table= Specify one table you would like to have scanned instead of the entire database\n"
                    );
                    return;
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
                    case "--resultfile":
                        config.setFile(argParts[1]);
                        break;
                    case "--table":
                        config.setTable(argParts[1]);
                        break;
                    default:
                        //If a argument does not match any of the valid argument types then throw exception and stop.
                        throw(new InputMismatchException("Invalid parameter field of type " + argParts[0].replace("--", "") + ". Please refer to the RoundTabler documentation for valid parameter flags."));
                }
            }

            //Ensures all required parameters needed for RoundTabler to run are filled out. THIS DOES NOT ENSURE --resultfile IS FILLED OUT BECAUSE ITS NOT A REQUIRED PARAMETER
            if(!config.allFilled())throw(new InputMismatchException("Missing required parameter flag. Please refer to the RoundTabler documentation for all parameter flags required to run."));


            //Print Statements signifying next step in RoundTabler process... can be removed just used as a checkpoint to ensure all arg checking is complete.
            System.out.println();
            System.out.println("ALL REQUIRED PARAMETERS FULFILLED...");

            System.out.println();
            System.out.println("INITIALIZING DATABASE CONNECTION");
            System.out.println();

            if ( config.getDbType().toUpperCase().compareTo("MARIADB") == 0 ) {
                System.out.println("DEBUG: Attempting Connection");
                try {
                    MariaReader mr = new MariaReader( config );
                }
                catch ( SQLException sqlex ) {
                    System.out.println(sqlex.toString() );
                    return;
                }
                catch ( Exception ex ) {
                    System.out.println(ex.toString() );
                    return;
                }
                PCIScan lPCI;
                int Counter;
                lPCI = new PCIScan( config, SummaryOfPerformance  );
                try { 
                Counter = lPCI.ScanMariaDB();
                }
                catch (SQLException sqlex ) {
                    System.out.println("DEBUG: " + sqlex.toString() );
                }

            }


        }catch(InputMismatchException e){

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

            new HTMLErrorOut(filePath, e.getMessage());

        }
        return;
    }

}
