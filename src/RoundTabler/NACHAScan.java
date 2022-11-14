package RoundTabler;

import java.util.regex.*;

import static utility.ApplicationUtility.*;

/**
 * Class used for scanning fields of data to check for NACHA compliance
 */
public class NACHAScan{

    /**
     * Method that checks to see if an ABA number is valid
     * @param abaNumber String ABA number inputted
     * @return boolean whether ABA number is valid or invalid
     */
    public boolean checkForValidABANumber(String abaNumber) {
        int abaLength = abaNumber.length();
        int validation = 0;
        if (isNumeric(abaNumber) && abaLength == 9){
            /*
             * Validation function uses checksum algorithm
             */
            for (int i = 0; i < abaLength; i+= 3){
                validation += (
                        (3 * charToInt(abaNumber.charAt(i)) )
                        + (7 * charToInt(abaNumber.charAt(i + 1)))
                        + (charToInt(abaNumber.charAt(i + 2)))
                );
            }
            if (validation != 0){
                return validation % 10 == 0;
            }
            return false;
        }
        return false;
    }

    /**
     * Method that is inputted with a database row string and outputs the confidence level
     * of the row as it pertains to containing an ABA number
     * @param databaseRow String to match with possible ABA number
     * @return confidenceLevel of ABA number presence in database row
     */
    public int getConfidenceLevelMatch(String databaseRow){
        String ABANumberSequenceRegex = "\\b[0-9]{9}\\b";
        Pattern ABANumberPattern = Pattern.compile(ABANumberSequenceRegex);

        Matcher abaNumberSequenceMatcher = ABANumberPattern.matcher(databaseRow);

        int result = 0;
        boolean resultMatcher = abaNumberSequenceMatcher.find();
        // checks if the regex is found in the database row string
        if (resultMatcher){
            // if found, confidence is increased to 75%
            result += 75;
            // further checks if the number found is a valid ABA number
            if (checkForValidABANumber(abaNumberSequenceMatcher.group())){
                // if number is a valid ABA Number, confidence increased to 100%
                result += 25;
            }
        }
        return result;
    }

}