package RoundTabler;
/*
 * Generic scan for NACHAScan and PCIScan to extend
 * Allows for easier usage in CommonScan
*/

abstract public class GenericScan {
    public String scanType;

    abstract public int getLastMatchStart();
    abstract public int getLastMatchEnd();
    abstract public String getLastMatchDescription();
    abstract public int getConfidenceLevelMatch( String DatabaseRow );
}
