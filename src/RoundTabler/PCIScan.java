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


	public enum CardType {
			Amex, Discover, MasterCard, Visa, Undetermined
	}


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




	 //
	 // Increate confidence level by applying additional 
	 // card number rules to determine exact card type
	 // Based on BIN and length
	 // 
	 //  Card Type			Prefix			Card Number Length
	 //
	 // AMEX				34, 37			15
	 // DISCOVER			6				16
	 // MASTER CARD         51 to 55		16
	 // VISA				4				13 or 16
    private CardType GetCardType( String matchString ){
                CardType Result;
                int CardLength;
                Result = CardType.Undetermined;
                CardLength = matchString.length();

                String firstChar;
                firstChar = matchString.substring(0,1);

                switch (firstChar){

                        case "3" : {
                                String firstTwo;
                                firstTwo = matchString.substring(0,2);
                                if ( CardLength == 15 ) {
                                                switch (firstTwo ) {
                                                        case "34": return CardType.Amex;
                                                        case "37" : return CardType.Amex;
                                                }
                                }
                        }

                        case "4" : if (( CardLength == 13) || ( CardLength == 16 ) ) return CardType.Visa;
                        case "5" : if ( CardLength == 16 ) return CardType.MasterCard;
                        case "6" : if ( CardLength == 16 ) return CardType.Discover;
                }


                return Result;
         }


	// Confidence level rules:
	// CardNumberSequenceMatcher: 50%
	// --------------------------------------------------------
	// 50 % <--  Matches string of digits of appropriate length
	// 75 % <-- 50% + Passes Luhn's test
	// 100% <-- 50% + 75% + We can identify the card type
	//
	// CardPartialSequenceMatcher: 100%
	//
	public int getConfidenceLevelMatch(String DatabaseRow ) {
		int result = 0;
		
		Matcher CardNumberSequenceMatcher = CardNumberPattern.matcher( DatabaseRow );

		// If we find what looks like a card sequence, make the confidence 75
		if (CardNumberSequenceMatcher.find()) {

			result += 50;   // Assign a confidence Level of at least 75%
			pLastMatchStart = CardNumberSequenceMatcher.start();
			pLastMatchEnd = CardNumberSequenceMatcher.end();
			// If the match passes LuhnsTest, Boost Confidence to 100
			pLastMatchDescription = "Card Regular Expression";
			if (LuhnTest.Validate(DatabaseRow.substring(CardNumberSequenceMatcher.start(), CardNumberSequenceMatcher.end())))
				result += 25;
				pLastMatchDescription = pLastMatchDescription + "<BR>Luhn's Test";
				pLastMatchStart = CardNumberSequenceMatcher.start();
				pLastMatchEnd = CardNumberSequenceMatcher.end();

				CardType thisCard;
				thisCard = GetCardType( DatabaseRow.substring(CardNumberSequenceMatcher.start(), CardNumberSequenceMatcher.end( )));

				switch (thisCard ){

					case Amex: result += 25;
								pLastMatchDescription = pLastMatchDescription + "<BR>American Express Card Number";
								break;
					case Discover: result += 25;
								pLastMatchDescription = pLastMatchDescription + "<BR>Discover Card Number";
								break;
					case MasterCard: results += 25;
								pLastMatchDescription = pLastMatchDescription + "<BR>MasterCard Card Number";
								break;
					case Visa: results += 25;
								pLastMatchDescription = pLastMatchDescription + "<BR>Visa Card Number";
								break;
					
				}



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