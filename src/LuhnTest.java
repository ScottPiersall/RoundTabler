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

		// sum the digits alternating in weight




		return ( (runningsum % 10 ) == 0 );
	}




}
