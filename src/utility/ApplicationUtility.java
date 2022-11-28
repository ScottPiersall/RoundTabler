package utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

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

    /**
     * Forms an ArrayList of all ABA Numbers from the FedACHdir.txt file
     * @return list of all abaNumbers from file
     */

    public static HashSet<String> getABANumbersFromFile() {
        HashSet<String> abaNumbers = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Resources/FedACHdir.txt"))) {
            boolean keepGoing = true;
            do {
                String currentLine = reader.readLine();
                if (currentLine == null || currentLine.trim().equals("")){
                    keepGoing = false;
                }
                else {
                    String abaNumber = currentLine.substring(0,9);
                    abaNumbers.add(abaNumber);
                }
            } while (keepGoing);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return abaNumbers;
    }
}
