package RoundTabler;

//
// Utility Class
// For Checking Card Numbers
// To See if they Pass Luhn's Algorithm
//
// Credit Card Numbers, to be valid, must pass Luhn's Algorithm
//
public class LuhnTest{

	public static boolean Validate( String CardNumber ){
		int runningsum =0;
		boolean Second = false;  // Use as a toggle as formula changes digit weight for every other digit
		
		// Remove Any Non-digit Characters from Card Number
		// using Regex which matches any characters not in [0..9] by replacing them with nothing
		CardNumber = CardNumber.replaceAll("[^0-9]", "");

		
		for (int index = CardNumber.length() - 1; index >=0 ; index--) {
			int digit = CardNumber.charAt(index) - '0';
			if (Second){
				digit *= 2;
			}
			runningsum += digit / 2;
			runningsum += digit % 10;
			Second = !Second;
		}
		return ( (runningsum % 10 ) == 0 );
	}


	public void TestNumbersWhichFail() {
		// Make sure we do not certify blank strings
		boolean Blank;
		Blank = LuhnTest.Validate("");
		assert Blank = false;

		// No valid digits should "reduce" to the result of a blank string
		boolean NoValidDigits;
		NoValidDigits = LuhnTest.Validate("asdjfh-kasjdhf-kjashd-kfjhas-kdjfh-kasjdhf");
		assert NoValidDigits = false;

		// This is a card number which will fail Luhn's test
		boolean BadCard;
		BadCard = LuhnTest.Validate("70198888888481821");
		assert BadCard = false;
			}
  
	public void TestNumbersWhichPass() {

		// This list of valid cards was obtained from:
		//https://www.paypalobjects.com/en_GB/vhelp/paypalmanager_help/credit_card_numbers.htm
		//

		boolean Amex1;
		Amex1 = LuhnTest.Validate("378282246310005");
		assert Amex1 = true;

		boolean Amex2;
		Amex2 = LuhnTest.Validate("371449635398431");
		assert Amex2 = true;

		boolean Discover1;
		Discover1 = LuhnTest.Validate("6011111111111117");
		assert Discover1 = true;

		boolean Discover2;
		Discover2 = LuhnTest.Validate("6011000990139424");
		assert Discover2 = true;

		boolean Visa1;
		Visa1 = LuhnTest.Validate("4111111111111111");
		assert Visa1 = true;

		boolean Visa2;
		Visa2 = LuhnTest.Validate("4012888888881881");
		assert Visa2 = true;
		
	}

	public static void main(String[] args){

		LuhnTest Tests;
		Tests = new LuhnTest();

		Tests.TestNumbersWhichFail();
		Tests.TestNumbersWhichPass();
	}



}