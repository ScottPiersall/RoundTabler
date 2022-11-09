package RoundTabler;
//
// A Class used for doing NACHAScan of fields
// for protected cards.
//

public class NACHAScan{

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static int charStringToInt(Character abaInt){
        return Character.getNumericValue(abaInt);
    }

    public boolean checkForValidABANumber(String abaNumber) {
        if (isNumeric(abaNumber) && abaNumber.length() == 9){
            int firstInt = charStringToInt(abaNumber.charAt(0));
            int secondInt = charStringToInt(abaNumber.charAt(1));
            int thirdInt = charStringToInt(abaNumber.charAt(2));
            int fourthInt = charStringToInt(abaNumber.charAt(3));
            int fifthInt = charStringToInt(abaNumber.charAt(4));
            int sixthInt = charStringToInt(abaNumber.charAt(5));
            int seventhInt = charStringToInt(abaNumber.charAt(6));
            int eighthInt = charStringToInt(abaNumber.charAt(7));
            int ninthInt = charStringToInt(abaNumber.charAt(8));

            int validation = (3 * (firstInt + fourthInt + seventhInt)
                    + 7 *(secondInt + fifthInt + eighthInt)
            + (thirdInt + sixthInt + ninthInt)) % 10;
            return validation == 0;
        }
        return false;
    }

}