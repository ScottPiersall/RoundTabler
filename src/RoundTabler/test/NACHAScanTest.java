package RoundTabler.test;

import RoundTabler.NACHAScan;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NACHAScanTest {

    NACHAScan nachaScan = new NACHAScan();
    @Test
    public void checkForValidABANumberTest(){
        boolean result = nachaScan.checkForValidABANumber("111000025");
        assertTrue(result);

        boolean result2 = nachaScan.checkForValidABANumber("021000021");
        assertTrue(result2);

        boolean result3 = nachaScan.checkForValidABANumber("011401533");
        assertTrue(result3);
    }

    @Test
    public void checkForInvalidABANumberTest(){
        boolean result = nachaScan.checkForValidABANumber("");
        assertFalse(result);

        boolean result2 = nachaScan.checkForValidABANumber("testing");
        assertFalse(result2);

        boolean result3 = nachaScan.checkForValidABANumber("000000000");
        assertFalse(result3);
    }

    @Test
    public void getConfidenceLevelMatchTest100() {
        String databaseRow = "The ABA number of the customer is 111000025";
        int result = nachaScan.getConfidenceLevelMatch(databaseRow);
        int expected = 100;
        assertEquals(expected, result);
    }

    @Test
    public void getConfidenceLevelMatchTest0() {
        String databaseRow = "There was no ABA number of the customer given";
        int result = nachaScan.getConfidenceLevelMatch(databaseRow);
        int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void getConfidenceLevelMatchTest75() {
        String databaseRow = "The ABA number of the customer is 000000000";
        int result = nachaScan.getConfidenceLevelMatch(databaseRow);
        int expected = 75;
        assertEquals(expected, result);
    }
}