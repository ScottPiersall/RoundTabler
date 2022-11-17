package utility;

public class ApplicationUtility {
    /**
     * Function that defines if a string value is a number or not
     * @param strNum String numerical value
     * @return boolean whether the value is a number or not
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null || strNum.trim().equals("")) {
            return false;
        }
        else {
            return strNum.chars().allMatch(Character::isDigit);
        }
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
        return (byte) character - 48;
    }
}
