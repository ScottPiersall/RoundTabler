package RoundTabler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



//
// A Class used for doing PCIScan of fields
// for protected cards.
//
public class PCIScan {

	// Match potential card numbers
	// by any sequence of digits between 13 and 16 characters in length
	// with embedded dashes
	static String CardNumberSequenceRegex = "\\b(?:\\d[-]*?){13,16}\\b";
	static Pattern CardNumberPattern = Pattern.compile(CardNumberSequenceRegex);


	static String CardPartialSequenceRegex = "\\b(AMEX|VISA|MC)-\\d{4}\\b";
	static Pattern CardPartialPattern = Pattern.compile(CardPartialSequenceRegex, Pattern.CASE_INSENSITIVE);

	private StringBuilder psbResults;

	private int pLastMatchStart;
	private int pLastMatchEnd;
	private String pLastMatchDescription;

	public PCIScan(){
		super();
		this.psbResults = new StringBuilder();
		psbResults.append("\n");
	}

	public int getLastMatchStart(){
		return pLastMatchStart;
	}

	public int getLastMatchEnd(){
		return pLastMatchEnd;
	}

	public String getLastMatchDescription(){
		return pLastMatchDescription;
	}

	public int getConfidenceLevelMatch(String DatabaseRow ) {
		int result = 0;
		
		Matcher CardNumberSequenceMatcher = CardNumberPattern.matcher( DatabaseRow );

		// If we find what looks like a card sequence, make the confidence 75
		if (CardNumberSequenceMatcher.find()) {

			result += 75;   // Assign a confidence Level of at least 75%
			// If the match passes LuhnsTest, Boost Confidence to 100
			pLastMatchDescription = "Card Regular Expression";
			if (LuhnTest.Validate(DatabaseRow.substring(CardNumberSequenceMatcher.start(), CardNumberSequenceMatcher.end())))
				result += 25;
				pLastMatchDescription = pLastMatchDescription + "<BR>Luhn's Test";
				pLastMatchStart = CardNumberSequenceMatcher.start();
				pLastMatchEnd = CardNumberSequenceMatcher.end();

		}

		if ( result == 0 ){
			Matcher CardPartialSequenceMatcher = CardPartialPattern.matcher( DatabaseRow );
			if ( CardPartialSequenceMatcher.find() ) {
				result = 100;
				pLastMatchStart = CardPartialSequenceMatcher.start();
				pLastMatchEnd = CardPartialSequenceMatcher.end();

				pLastMatchDescription = "Card Type Plus Last 4";
			}
		}	

		return result;
	}

}