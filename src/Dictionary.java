import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * @author Stephen Hildebrand
 * @version 4/22/2016
 *          This class implements a Dictionary of Word objects as a hash table that maps
 *          keys to values.
 */
public class Dictionary {
    /** The number of dictionary entries in the table. */
    private int n = 0;
    /** The number of buckets in the hash table. */
    private int capacity;
    /** Prime factor. */
    private int prime;
    /** Shift and scaling factors. */
    private long scale, shift;
    /** Array of LinkedList word buckets. */
    private LinkedList<Word>[] table;
    /** Total number of probes made during checking. */
    private int probeCount = 0;
    /** Count of lookup operations. */
    private int lookupCount = 0;


    /** Creates a hash table with the given capacity and prime factor (31957). */
    @SuppressWarnings({"unchecked"})
    public Dictionary(int capacity, int prime) {
        this.capacity = capacity;
        this.prime = prime;
        Random rand = new Random();
        scale = rand.nextInt(prime - 1) + 1;
        shift = rand.nextInt(prime);
        table = (LinkedList<Word>[]) new LinkedList[capacity];
    }

    /** Creates a hash table with given capacity and prime factor 109345121. */
    public Dictionary(int capacity) {
        this(capacity, 109345121);
    }  // Default prime.

    /**
     * Implementation of a 5-bit cyclic-shift to compute the hash key for a
     * string word.
     *
     * @param word the word to look up
     * @return the calculated key
     */
    int hashCode(String word) {
        int k = 0;
        for (int i = 0; i < word.length(); i++) {
            k = (k << 5) | (k >>> 27);      // 5-bit cyclic shift of the running sum.
            k += (int) word.charAt(i);      // Add in the next character.
        }
        return k;
    }

    /**
     * Returns the value associated with the specified key, or null if no such entry exists.
     *
     * @param key the key whose associated value is to be returned
     * @return the associated value, or null if no such entry exists
     */
    String get(int key) {
        lookupCount++;
        return bucketGet(hashValue(key), key);
    }

    /**
     * Removes the entry with the specified key, if present, and returns
     * its associated value. Otherwise does nothing and returns null.
     *
     * @param key the key whose entry is to be removed from the map
     * @return the previous value associated with the removed key, or null if no such entry exists
     */
    String remove(int key) {
        return bucketRemove(hashValue(key), key);
    }

    /**
     * Associates the given value with the given key. If an entry with
     * the key was already in the map, this replaces the previous value
     * with the new one and returns the old value. Otherwise, a new
     * entry is added and null is returned.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with the key (or null, if no such entry)
     */
    String put(int key, String value) {
        return bucketPut(hashValue(key), key, value);
    }

    // private utilities

    /** Hash function applying MAD method to default hash code. */
    private int hashValue(int key) {
        return (int) ((Math.abs(key * scale + shift) % prime) % capacity);
    }

    /**
     * Returns the number of words currently in the dictionary.
     *
     * @return the number of dictionary entries
     */
    int size() {
        return n;
    }


    /**
     * Returns value associated with key k in bucket with hash value h.
     * If no such entry exists, returns null.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @return associate value (or null, if no such entry)
     */
    private String bucketGet(int h, int k) {
        LinkedList<Word> bucket = table[h];
        if (bucket == null) return null;
        for (Word w : bucket) {
            if (w != null)          // Increment probe count when compared to a word.
                probeCount++;
            if (w != null && k == w.getKey()) return w.getValue();
        }
        return null;
    }

    /**
     * Associates key k with value v in bucket with hash value h, returning
     * the previously associated value, if any.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @param v the value to be associated
     * @return previous value associated with k (or null, if no such entry)
     */
    private String bucketPut(int h, int k, String v) {
        LinkedList<Word> bucket = table[h];
        if (bucket == null)
            bucket = table[h] = new LinkedList<>();
        int oldSize = bucket.size();
        for (Word w : bucket) {
            if (k == w.getKey()) {
                String old = w.getValue();
                bucket.add(new Word(k, v));
                return old;
            }
        }
        bucket.add(new Word(k, v));
        n += (bucket.size() - oldSize);   // Size may have increased.
        return null;
    }

    /**
     * Removes entry having key k from bucket with hash value h, returning
     * the previously associated value, if found.
     *
     * @param h the hash value of the relevant bucket
     * @param k the key of interest
     * @return previous value associated with k (or null, if no such entry)
     */
    private String bucketRemove(int h, int k) {
        LinkedList<Word> bucket = table[h];
        if (bucket == null) return null;
        int oldSize = bucket.size();
        String answer = null;
        for (Word w : bucket) {
            if (k == w.getKey()) {
                answer = w.getValue();
                bucket.remove(w);
            }
        }
        n -= (oldSize - bucket.size());   // Size may have decreased.
        return answer;
    }

    /**
     * Returns an iterable collection of all key-value entries of the map.
     *
     * @return iterable collection of the map's entries
     */
    public Iterable<Word> wordSet() {
        ArrayList<Word> buffer = new ArrayList<>();
        for (int h = 0; h < capacity; h++)
            if (table[h] != null)
                for (Word w : buffer)
                    buffer.add(w);
        return buffer;
    }

    /**
     * Returns the total number of times a text word is compared to a word
     * in the table.
     *
     * @return the number of times a text word is compared to a table word
     */
    int getProbeCount() {
        return probeCount;
    }

    /**
     * Returns the total number of times a lookup operation is performed.
     *
     * @return the number of times a text word is compared to a table word
     */
    int getLookupCount() {
        return lookupCount;
    }

    /**
     * A private nested class for a Word implemented as a key-value pair,
     * with integer keys and String values.
     */
    private static class Word implements Comparable<Word> {
        private int key;
        /** the key to be stored */
        private String value;       /* The hashed value associated with key. */

        /** Constructor for a new Word. */
        Word(int key, String value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Returns the key (word).
         *
         * @return the key for this Word
         */
        int getKey() {
            return key;
        }

        /**
         * Returns the associated word as a string.
         *
         * @return the associated word
         */
        String getValue() {
            return value;
        }

        /**
         * Sets the key (word).
         *
         * @param key the key to set for this Word
         */
        public void setKey(int key) {
            this.key = key;
        }

        /**
         * Sets the associated value, then returns the previously associated value.
         *
         * @param v the string for this Word
         * @return the previously associated value (may be null)
         */
        public String setValue(String v) {
            String old = this.value;
            this.value = v;
            return old;
        }

        /** Returns a string representation of this Word. */
        public String toString() {
            return "<" + key + ", " + value + ">";
        }

        @Override
        public int compareTo(Word o) {
            return key - o.getKey();
        }
    }
}
