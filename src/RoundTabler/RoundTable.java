package RoundTabler;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import RoundTabler.Configuration;

public class RoundTable {

    public static void main(String[] args){

        Configuration config = new Configuration();

        try {

            //Iterate through all args given through the command line.

            for (String elem : args) {
                String[] argParts = elem.split("=");

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
                        throw(new InputMismatchException("Invalid parameter field of type " + argParts[0].replace("--", "") + "."));
                }
            }

            //Ensures all required parameters needed for RoundTabler to run are filled out. THIS DOES NOT ENSURE --resultfile IS FILLED OUT BECAUSE ITS NOT A REQUIRED PARAMETER
            if(!config.allFilled())throw(new InputMismatchException("Missing required parameter flag."));


            switch(config.getDbType()){
                case "mariaDB":
                    if(config.getTable().length() != 0){
                        config.setQueryStatement("SELECT TABLE_NAME, COLUMN_NAME from information_schema.COLUMNS where table_schema=DATABASE() and TABLE_NAME =" + config.getTable() + "and DATA_TYPE in (mediumtext, longtext, text, tinytext, varchar);");
                    }else{
                        config.setQueryStatement("SELECT TABLE_NAME, COLUMN_NAME from information_schema.COLUMNS where table_schema=DATABASE() and DATA_TYPE in (mediumtext, longtext, text, tinytext, varchar);");
                    }
                    break;
                case "mysql":
                    if(config.getTable().length() != 0){
                        config.setQueryStatement("SELECT table_name, column_name FROM information_schema.tables WHERE table_type='BASE TABLE' AND table_schema = '" + config.getDatabase() + "' AND table_name = '" + config.getTable() + "' AND data_type in (mediumtext, longtext, text, tinytext, varchar);");
                    }else{
                        config.setQueryStatement("SELECT table_name, column_name FROM information_schema.tables WHERE table_type='BASE TABLE' AND table_schema = '" + config.getDatabase() + "' AND data_type in (mediumtext, longtext, text, tinytext, varchar);\"");
                    }
                    break;
                case "mongo":
                    if(config.getTable().length() != 0){
                        config.setQueryStatement("");
                    }else{
                        config.setQueryStatement("");
                    }
                    break;

            }


            //Print Statements signifying next step in RoundTabler process... can be removed just used as a checkpoint to ensure all arg checking is complete.
            System.out.println("ALL REQUIRED PARAMETERS FULFILLED...");

            System.out.println("INITIALIZING DATABASE CONNECTION");





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

    }

}
