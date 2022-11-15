package RoundTabler.test;

import RoundTabler.RoundTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoundTableTest {

    @Test
    public void invalidTypeTest(){

        System.out.println("invalidTypeTest:");

        String[] args = {"--type=test", "--dbtype=mysql", "--server=localhost", "--user=username", "--password=1234", "--database=DBToTest",};

        assertEquals(-1, RoundTable.round(args));

        System.out.println();

    }

    @Test
    public void invalidDbTypeTest(){

        System.out.println("invalidDbTypeTest:");

        String[] args = {"--type=all", "--dbtype=java", "--server=localhost", "--user=username", "--password=1234", "--database=DBToTest",};

        assertEquals(-1, RoundTable.round(args));

        System.out.println();

    }

    @Test
    public void catchEmptyType(){

        System.out.println("catchEmptyType:");

        String[] args = {"--type=", "--dbtype=mysql", "--server=localhost", "--user=username", "--password=1234", "--database=DBToTest",};

        assertEquals(-1, RoundTable.round(args));

        System.out.println();

    }

    @Test
    public void catchEmptyDbType(){

        System.out.println("catchEmptyDbType:");

        String[] args = {"--type=all", "--dbtype=", "--server=localhost", "--user=username", "--password=1234", "--database=DBToTest",};

        assertEquals(-1, RoundTable.round(args));

        System.out.println();

    }

    @Test
    public void catchEmptyServer(){

        System.out.println("catchEmptyServer:");

        String[] args = {"--type=all", "--dbtype=mysql", "--server=", "--user=username", "--password=1234", "--database=DBToTest",};

        assertEquals(-1, RoundTable.round(args));

        System.out.println();

    }

    @Test
    public void catchEmptyUser(){

        System.out.println("catchEmptyUser:");

        String[] args = {"--type=all", "--dbtype=mysql", "--server=localhost", "--user=", "--password=1234", "--database=DBToTest",};

        assertEquals(-1, RoundTable.round(args));

        System.out.println();

    }

    @Test
    public void catchEmptyPassword(){

        System.out.println("catchEmptyPassword:");

        String[] args = {"--type=all", "--dbtype=mysql", "--server=localhost", "--user=username", "--password=", "--database=DBToTest",};

        assertEquals(-1, RoundTable.round(args));

        System.out.println();

    }

    @Test
    public void catchEmptyDatabase(){

        System.out.println("catchEmptyDatabase:");

        String[] args = {"--type=all", "--dbtype=mysql", "--server=localhost", "--user=username", "--password=1234", "--database=",};

        assertEquals(-1, RoundTable.round(args));

        System.out.println();

    }

    @Test
    public void catchHelpCommand(){

        System.out.println("catchHelpCommand:");

        String[] args = {"--help"};

        assertEquals(-1, RoundTable.round(args));

        System.out.println();

    }

    /*@Test
    public void allValidParameters(){

        System.out.println("allValidParameters:");

        String[] args = {"--type=all", "--dbtype=mysql", "--server=localhost", "--user=username", "--password=1234", "--database=DBToTest",};

        assertEquals(0, RoundTable.main(args));

        System.out.println();

    }*/

}
