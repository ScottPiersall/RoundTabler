package RoundTabler.test;

import RoundTabler.PCIScan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PCIScanTest {

    @Test
    public void TestConfidence_100() {
        String databaseRow = "The customer's card number is 6011-1111-1111-1117 and they said on the phone we could sign them up for auto pay";
        int result = PCIScan.getConfidenceLevelMatch(databaseRow);
        int expected = 100;
        assertEquals(expected, result);
    }

    @Test
    public void TestConfidence_75() {
        String databaseRow = "The number is 6011121511111117, which looks like a card but fails Luhn's test";
        int result = PCIScan.getConfidenceLevelMatch(databaseRow);
        int expected = 75;
        assertEquals(expected, result);
    }

    @Test
    public void TestConfidence_0() {
        String databaseRow = "There is no card data in this field";
        int result = PCIScan.getConfidenceLevelMatch(databaseRow);
        int expected = 0;
        assertEquals(expected, result);

        String databaseRow2 = "";
        int result2 = PCIScan.getConfidenceLevelMatch(databaseRow2);
        assertEquals(expected, result2);
    }
}
