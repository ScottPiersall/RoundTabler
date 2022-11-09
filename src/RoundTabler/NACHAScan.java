package RoundTabler;

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

}