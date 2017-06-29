import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Stephen Hildebrand
 *
 * Test class for Dictionary.java.
 */
public class DictionaryTest {
    /** Dictionary hash table of Words */
    private Dictionary d;
    /** Sample word input */
    private String input[] = {"add", "abounds", "crunch", "founder", "igloo", "searched", "words"};
    /** Array of keys from words */
    private int keys[] = new int[7];
    /** The number of words in the default dictionary */
    private static final int DICTIONARY_SIZE = 25144;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
        d = new Dictionary(DICTIONARY_SIZE);
        for (int i = 0; i < 7; i++) {
            keys[i] = d.hashCode(input[i]);
        }
	}

    /** Test method for {@link Dictionary#Dictionary(int)}. */
    @Test
    public void testDictionary() {
        assertNotNull(d);
    }

    /** Test method for {@link Dictionary#put(int, java.lang.String)}. */
    @Test
    public void put() throws Exception {
        for (int i = 0; i < 7; i++) {
            d.put(d.hashCode(input[i]), input[i]);
        }
        assertEquals(7, d.size());
    }

    /** Test method for {@link Dictionary#get(int)}. */
	@Test
	public void get() throws Exception {
        for (int i = 0; i < 7; i++) {
            String expect = input[i];
            int key = d.hashCode(expect);
            assertEquals(keys[i], key);

            d.put(key, expect);
        }
        for (int i = 0; i < 7; i++) {
            String expect = input[i];
            int key = d.hashCode(expect);

            assertEquals(expect, d.get(key));
        }
	}

    /** Test method for {@link Dictionary#remove(int)}. */
	@Test
	public void remove() throws Exception {
        assertTrue(d.size() == 0);

        for (int i = 0; i < 7; i++) {
            String expect = input[i];
            int key = d.hashCode(expect);
            assertEquals(keys[i], key);

            d.put(key, expect);
        }

        assertTrue(d.size() > 0);

        for (int i = 0; i < 7; i++) {
            String expect = input[i];
            int key = d.hashCode(expect);

            assertEquals(expect, d.remove(key));
        }

        assertTrue(d.size() == 0);
	}
}
