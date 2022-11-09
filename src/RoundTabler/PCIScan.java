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


}