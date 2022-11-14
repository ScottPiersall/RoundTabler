package RoundTabler.test;

import RoundTabler.LuhnTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LuhnJUnitTest {

    @Test
    public void validateNegativeTests() {
        // Make sure we do not certify blank strings
        boolean blank = LuhnTest.Validate("");
        assertFalse(blank);

        // No valid digits should "reduce" to the result of a blank string
        boolean noValidDigits = LuhnTest.Validate("asdjfh-kasjdhf-kjashd-kfjhas-kdjfh-kasjdhf");
        assertFalse(noValidDigits);

        // This is a card number which will fail Luhn's RoundTabler.test
        boolean badCard = LuhnTest.Validate("70198888888481821");
        assertFalse(badCard);
    }

    @Test
    public void validatePositiveTests() {

        // This list of valid cards was obtained from:
        //https://www.paypalobjects.com/en_GB/vhelp/paypalmanager_help/credit_card_numbers.htm
        //

        boolean amex1 = LuhnTest.Validate("378282246310005");
        assertTrue(amex1);

        boolean amex2 = LuhnTest.Validate("371449635398431");
        assertTrue(amex2);

        boolean discover1 = LuhnTest.Validate("6011111111111117");
        assertTrue(discover1);

        boolean discover2 = LuhnTest.Validate("6011000990139424");
        assertTrue(discover2);

        boolean visa1 = LuhnTest.Validate("4111111111111111");
        assertTrue(visa1);

        boolean visa2 = LuhnTest.Validate("4012888888881881");
        assertTrue(visa2);

    }
}
