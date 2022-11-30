package RoundTabler;

import static utility.ApplicationUtility.charToInt;

/* 
* Utility Class For Checking Card Numbers
* To See if they Pass Luhn's Algorithm
*
* Credit Card Numbers, to be valid, must pass Luhn's Algorithm
*/
public class LuhnTest {

	public static boolean Validate( String cardNumber ) {
		int runningSum = 0;
		boolean isSecondNumber = false;  // Use as a toggle as formula changes digit weight for every other digit

		// Remove Any Non-digit Characters from Card Number
		// using Regex which matches any characters not in [0..9] by replacing them with nothing
		cardNumber = cardNumber.replaceAll("[^0-9]", "");

		int cardNumberLength = cardNumber.length();

		if ( cardNumberLength == 0 ) {
			return false;
		}

		// Iterate through the card number backwards
		for ( int i = cardNumberLength - 1; i >= 0; i-- ) {
			int addDigits = 0;
			int digit = charToInt(cardNumber.charAt(i));
			if ( isSecondNumber ) {
				digit *= 2;
				if ( digit >= 10 ) {
					String stringDigit = Integer.toString(digit);
					for ( int j = 0; j < stringDigit.length(); j++ ) {
						addDigits += charToInt( stringDigit.charAt(j) );
					}
					digit = addDigits;
				}
			}
			runningSum += digit;
			isSecondNumber = !isSecondNumber;
		}
		return ( (runningSum % 10 ) == 0 );
	}
}