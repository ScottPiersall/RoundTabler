package RoundTabler;

import java.util.HashSet;
import java.util.regex.*;

import static utility.ApplicationUtility.*;

/*
 * Class used for scanning fields of data to check for NACHA compliance
*/

public class NACHAScan{

    private StringBuilder psbResults;

    private int pLastMatchStart;
    private int pLastMatchEnd;
    private String pLastMatchDescription;
    private final HashSet<String> abaNumbersList = getABANumbersFromFile();

    // We will not scan any rows less than NACHAScanMinLength
    private final int NACHAScanMinLength = 9;

    public NACHAScan() {
        super();
        this.psbResults = new StringBuilder();
        psbResults.append("\n");
    }

    public int getLastMatchStart() {
        return pLastMatchStart;
    }

    public int getLastMatchEnd() {
        return pLastMatchEnd;
    }

    public String getLastMatchDescription() {
        return pLastMatchDescription;
    }

    /**
     * Method that checks to see if an ABA number is valid
     * @param abaNumber String ABA number inputted
     * @return boolean whether ABA number is valid or invalid
    */
    public boolean checkForValidABANumber( String abaNumber ) {
        int abaLength = abaNumber.length();
        int validation = 0;

        if ( isNumeric(abaNumber) && abaLength == 9 ) {
            /*
             * Validation function uses checksum algorithm
             */
            for ( int i = 0; i < abaLength; i+= 3 ) {
                validation += (
                          ( 3 * charToInt(abaNumber.charAt(i))     )
                        + ( 7 * charToInt(abaNumber.charAt(i + 1)) )
                        + (     charToInt(abaNumber.charAt(i + 2)) )
                );
            }

            if ( validation != 0 ) {
                return validation % 10 == 0;
            }

            return false;
        }

        return false;
    }

    public boolean checkListOfAbaNumbers( String abaNumber ){
        if ( !isNumeric(abaNumber) ) {
            return false;
        }

        return abaNumbersList.contains(abaNumber);
    }


    /**
     * Method that is inputted with a database row string and outputs the confidence level
     * of the row as it pertains to containing an ABA number
     * @param databaseRow String to match with possible ABA number
     * @return confidenceLevel of ABA number presence in database row
    */
    public int getConfidenceLevelMatch( String databaseRow ) {

		if ( databaseRow.length() < NACHAScanMinLength ) {
            return 0;
        }

        final String ABANumberSequenceRegex = "\\b[0-9]{9}\\b";
        Pattern ABANumberPattern = Pattern.compile(ABANumberSequenceRegex);

        Matcher abaNumberSequenceMatcher = ABANumberPattern.matcher(databaseRow);

        int result = 0;
        boolean resultMatcher = abaNumberSequenceMatcher.find();

        // checks if the regex is found in the database row string
        // and if the number found is a valid ABA number
        if ( resultMatcher && checkForValidABANumber(abaNumberSequenceMatcher.group()) ){
            // if found, confidence is increased to 50%
            result += 50;
            pLastMatchDescription = "9 Digits<BR>Passes Validation Function";

            pLastMatchStart = abaNumberSequenceMatcher.start();
            pLastMatchEnd = abaNumberSequenceMatcher.end();
            if (checkListOfAbaNumbers(abaNumberSequenceMatcher.group())){
                // if number is a valid ABA Number on file list, confidence increased to 100%
                result += 50;
                pLastMatchDescription = "9 Digit Number<BR>Passes Validation Function<BR>Valid ABA Number";

                pLastMatchStart = abaNumberSequenceMatcher.start();
                pLastMatchEnd = abaNumberSequenceMatcher.end();
            }
        }

        return result;
    }

}