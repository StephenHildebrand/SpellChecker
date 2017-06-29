import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * @author Stephen Hildebrand
 *
 * Test cases for the SpellCheck.java class.
 */
public class SpellCheckTest {
    /** Input text file to check the spelling of */
	private File in1 = new File("orig_01.txt");
    /** Input text file to check the spelling of */
    private File in2 = new File("orig_02.txt");
    /** Dictionary hash table of Words */
    private Dictionary d;
    /** Sample word input */
    private String input[] = {"add", "abounds", "crunch", "founder", "igloo", "searched", "words"};
    /** Array of keys from words */
    private int keys[] = new int[7];
    /** The number of words in the default dictionary */
    private static final int DICTIONARY_SIZE = 25144;

    @Test
	public void test() {
		fail("Not yet implemented");
	}

}
