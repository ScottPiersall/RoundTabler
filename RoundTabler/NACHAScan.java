package RoundTabler;

/**
 * Class used for scanning fields of data to check for NACHA compliance
 */
public class NACHAScan{

    /**
     * Function that defines if a string value is a number or not
     * @param strNum String numerical value
     * @return boolean whether the value is a number or not
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Converts a character into an int
     * @param character character inputted into converter
     * @return converted integer value of character
     */
    public static int charToInt(char character){
        /*
         * If the character imported is not a digit,
         * then a NumberFormatException is thrown
         */
        if (!Character.isDigit(character)){
            throw new NumberFormatException("Not a digit!");
        }
        return Character.getNumericValue(character);
    }

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