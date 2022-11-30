package RoundTabler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Class used for doing PCIScan of fields for protected cards
*/

public class PCIScan extends GenericScan {

	// Match potential card numbers by any sequence of digits
	// between 13 and 16 characters in length with embedded dashes
	static final String CardNumberSequenceRegex = "\\b(?:\\d[-]*?){13,16}\\b";
	static Pattern CardNumberPattern = Pattern.compile(CardNumberSequenceRegex);

	// Partial card matching (e.g. VISA-XXXX)
	static final String CardPartialSequenceRegex = "\\b(AMEX|VISA|MC)-\\d{4}\\b";
	static Pattern CardPartialPattern = Pattern.compile(CardPartialSequenceRegex, Pattern.CASE_INSENSITIVE);

	private StringBuilder psbResults;

	private int pLastMatchStart;
	private int pLastMatchEnd;
	private String pLastMatchDescription;

	// We will not scan any rows less than PCIScanMinLength
	private final int PCIScanMinLength = 8;

	public enum CardType {
		Amex, Discover, MasterCard, Visa, Undetermined
	}

	public PCIScan() {
		super();
		this.psbResults = new StringBuilder();
		psbResults.append("\n");
		this.scanType = "PCIDSS";
	}

	public int getLastMatchStart() {
		return pLastMatchStart;
	}

	public int getLastMatchEnd() {
		return pLastMatchEnd;
	}

	public String getLastMatchDescription() {
		return pLastMatchDescription;
	}


	/*
	 * Increate confidence level by applying additional 
	 * card number rules to determine exact card type
	 * Based on BIN and length
	 * 
	 *  Card Type			Prefix			Card Number Length
	 *
	 *  AMEX				34, 37			15
	 *  DISCOVER			6				16
	 *  MASTER CARD         51 to 55		16
	 *  VISA				4				13 or 16
	*/
    private CardType GetCardType( String matchString ) {
        CardType Result;
        int CardLength;
        Result = CardType.Undetermined;
        CardLength = matchString.length();

        String firstChar;
        firstChar = matchString.substring(0,1);

        switch (firstChar){
            case "3": 
                String firstTwo;
                firstTwo = matchString.substring(0,2);
                if ( CardLength == 15 ) {
                    switch (firstTwo) {
                        case "34": return CardType.Amex;
                        case "37": return CardType.Amex;
                    }
                }
				break;
            case "4": 
				if ( CardLength == 13 || CardLength == 16 ) return CardType.Visa;
				break;
            case "5": 
				if ( CardLength == 16 ) return CardType.MasterCard;
				break;
            case "6": 
				if ( CardLength == 16 ) return CardType.Discover;
				break;
        }

    	return Result;
    }

	/*
	* Confidence level rules:
	* CardNumberSequenceMatcher: 50%
	* --------------------------------------------------------
	* 50 % <--  Matches string of digits of appropriate length
	* 75 % <-- 50% + Passes Luhn's test
	* 100% <-- 50% + 75% + We can identify the card type
	*
	* CardPartialSequenceMatcher: 100%
	*/
	public int getConfidenceLevelMatch( String DatabaseRow ) {
		int result = 0;
		
		if ( DatabaseRow.length() < PCIScanMinLength ) {
				return 0;
		}

		Matcher CardNumberSequenceMatcher = CardNumberPattern.matcher( DatabaseRow );

		// If we find what looks like a card sequence
		if ( CardNumberSequenceMatcher.find() ) {
			// Assign a confidence Level of at least 50%
			result += 50;   
			pLastMatchStart = CardNumberSequenceMatcher.start();
			pLastMatchEnd = CardNumberSequenceMatcher.end();
			pLastMatchDescription = "Card Regular Expression";
			
			
			if ( LuhnTest.Validate( DatabaseRow.substring( CardNumberSequenceMatcher.start(), CardNumberSequenceMatcher.end())  ) )
				// If the match passes LuhnsTest, Boost Confidence to 75%
				result += 25;
				pLastMatchStart = CardNumberSequenceMatcher.start();
				pLastMatchEnd = CardNumberSequenceMatcher.end();
				pLastMatchDescription = pLastMatchDescription + "<BR>Luhn's Test";

				CardType thisCard;
				thisCard = GetCardType( DatabaseRow.substring( CardNumberSequenceMatcher.start(), CardNumberSequenceMatcher.end() ) );

				if ( thisCard != CardType.Undetermined ) result += 25;

				switch ( thisCard ){
					case Amex:
						pLastMatchDescription = pLastMatchDescription + "<BR>American Express Card Number";
						break;
					case Discover:
						pLastMatchDescription = pLastMatchDescription + "<BR>Discover Card Number";
						break;
					case MasterCard:
						pLastMatchDescription = pLastMatchDescription + "<BR>MasterCard Card Number";
						break;
					case Visa:
						pLastMatchDescription = pLastMatchDescription + "<BR>Visa Card Number";
						break;
					case Undetermined:
						// No card-type could be identified
						break;
				}
		}

		// Partial matching if full match yielded nothing
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