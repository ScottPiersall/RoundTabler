package RoundTabler;

import java.util.regex.*;
import RoundTabler.*;
//
// A Class used for doing PCIScan of fields
// for protected cards.
//
public class PCIScan{

	// Match potential card numbers
	// by any sequence of digits between 13 and 16 characters in length
	// with embedded dashes
	static String CardNumberSequenceRegex = "\b(?:\\d[-]*?){13,16}\b";
	static Pattern CardNumberPattern = Pattern.compile( CardNumberSequenceRegex);


	public PCIScan(){
		super();
	}

	public static int getConfidenceLevelmatch( String DatabaseRow ) {
		int result = 0;
		
		Matcher CardNumberSequenceMatcher = CardNumberPattern.matcher( DatabaseRow );


		// If we find what looks like a card sequence, make the confidence 75
		if ( CardNumberSequenceMatcher.matches())
			{
			result += 75;   // Assign a confidence Level of at least 75%
			// If the match passes LuhnsTest, Boost Confidence to 100
			if ( LuhnTest.Validate( DatabaseRow.substring( CardNumberSequenceMatcher.start(), CardNumberSequenceMatcher.end() ) ) )
				result += 25;
			}

		return result;
	}

	public void TestConfidence_100(){
		assert (PCIScan.getConfidenceLevelmatch("The customer's card number is 6011-1111-1111-1117 and they said on the phone we could sign them up for auto pay" )) == 100;
	}

	public void TestConfidence_75(){
		assert (PCIScan.getConfidenceLevelmatch("The number is 6011121511111117, which looks like a card but fails Luhn's test' " )) == 75;
	}

	public void TestConfidence_0(){
		assert (PCIScan.getConfidenceLevelmatch("There is no card data in this field" )) == 0;
		assert (PCIScan.getConfidenceLevelmatch("")) == 0;
	}

	public static void main(String[] args){
			PCIScan Tests;
			Tests = new PCIScan();
			Tests.TestConfidence_0();
			Tests.TestConfidence_75();
			Tests.TestConfidence_100();
	}

}