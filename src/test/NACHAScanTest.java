package RoundTabler.test;

import RoundTabler.NACHAScan;
import org.junit.Test;


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
}