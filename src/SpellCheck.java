import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author  Stephen Hildebrand
 * @version 4/22/2016
 *          <p>
 *          Project: Project4
 */
public class SpellCheck {
    private static Dictionary dict;                 /** dictionary hash table of words */
    private static final int DICT_SIZE = 25144;     /** number of words in the default dictionary */
    private static final int EXIT_FAILURE = 1;      /** error exit status code of 1 */

    /**
     * The main method. Begins by reading a list of edges from a text file.
     * Prompts for name of dictionary file containing the word list, the name
     * of the text file to be checked and the name of the output file.
     * @param args  The command-line arguments
     */
    public static void main(String[] args) {
        String txtFileName;                     /** name of the input text file */
        int dictWordCount = 0;                  /** count of words in the dictionary */
        int textWordCount = 0;                  /** count of words in the text */
        int misspelledCount = 0;                /** count of misspelled words */
        String outFileName = null;              /** name of the output file */
        PrintStream outfile = null;             /** stream to print to output file */

        if (args == null || args.length != 1) {
            System.out.printf("Error reading input file...\nUsage: SpellCheck <input-file.txt>\n");
            System.exit(EXIT_FAILURE);
        }

        //-------------------------------------
        //      Dictionary File Processing
        //-------------------------------------
        dict = new Dictionary(DICT_SIZE);
        // read/process dictionary file and fill hash table
        try {
            Scanner ds = new Scanner(new File("dict.txt"));
            while (ds.hasNextLine()) {
                Scanner ls = new Scanner(ds.nextLine());
                String word = ls.next();
                dict.put(dict.hashCode(word), word);    // add word to dictionary

                dictWordCount++;        // increment total dictionary word count
            }
        } catch (FileNotFoundException e) { // Catch file IO error and quit
            System.out.println("Error. Dictionary file <dict.txt> could not be found.");
            System.exit(EXIT_FAILURE);
        } // --------- End Dictionary File ----------


        //-------------------------------------
        //         Text File Processing
        //-------------------------------------
        // read and process input text file & check spelling of each word
        txtFileName = args[0];
        if (txtFileName != null && txtFileName.length() > 0) {
            try {
                Scanner ts = new Scanner(new File(txtFileName));
                outFileName = txtFileName.replace(".txt", "-out.txt");
                outfile = new PrintStream(new File(outFileName));
                outfile.printf("Possible misspelled words:\n");
                // for each line of the text file
                while (ts.hasNextLine()) {
                    String line = ts.nextLine();                    // next line of text
                    Pattern pat = Pattern.compile("[\\w']+");       // regex pattern
                    Matcher match = pat.matcher(line);              // matcher for pattern
                    while (match.find()) {
                        String word = line.substring(match.start(), match.end());
                        textWordCount++;
                        if (!checkSpelling(word)) {
                            outfile.printf(word + "\n");            // print to output file
                            misspelledCount++;
                        }
                    }
                }
            } catch (FileNotFoundException e) { // Catch file IO error and quit
                System.out.printf("Unable to read %s. Usage: SpellCheck <input-file.txt>\n", txtFileName);
                System.exit(EXIT_FAILURE);
            }
        } else {
            System.out.printf("File %s not found. Usage: SpellCheck <input-file.txt>\n", txtFileName);
            System.exit(EXIT_FAILURE);
        } // ------------- End Text File ---------------


        //-------------------------------------
        //        Output File Processing
        //-------------------------------------
        System.out.printf("Printing results to %s.\n", outFileName);
        int probeCount = dict.getProbeCount();
        int lookupCount = dict.getLookupCount();
        outfile.printf("\nDictionary word count: %d\n", dictWordCount);  // count of words in dictionary
        outfile.printf("Text word count: %d\n", textWordCount);          // count of words in the text
        outfile.printf("Misspelled word count: %d\n", misspelledCount);  // count of misspelled words
        outfile.printf("Total probe count: %d\n", probeCount);           // count of total probes

        // average number of probes per word
        double probesPerWord = probeCount / (double) textWordCount;
        outfile.printf("Average number of probes per word: %.2f\n", probesPerWord);

        // average number of probes per lookup operation
        double probesPerLookup = probeCount / (double) lookupCount;
        outfile.printf("Average number of probes per lookup: %.2f\n", probesPerLookup);
        // ---------- End Output File ------------
    }

    /**
     * Checks the spelling of each word in the text by searching for it in the
     * dictionary. Reads the user's text file word by word, checking the spelling.
     * Writes out any word that appears to be misspelled. If a word is not found
     * at first, several additional rules are followed (as specified below) to
     * strip common suffixes, and the search is then reattempted. Regards the
     * apostrophe as a letter (e.g. "don't"). The number of lookups is tracked in the
     * Dictionary class.
     * @param word  the word to check
     * @return true if the word is found in the dictionary, false if not
     */
    private static boolean checkSpelling(String word) {
        boolean found = false;              /** whether a match has been found */
//        String retry;                       /** altered word to retry */

        // first lookup for dictionary match
        int key = dict.hashCode(word);
        String ans = dict.get(key);
        if (ans != null)
            found = ans.equals(word);       // additional check to ensure correct word returned

        // ----------- Word Alterations ------------
        // if upper case first letter -> downshift to lower case
        if (!found && Character.isUpperCase(word.charAt(0))) {
            word = Character.toLowerCase(word.charAt(0)) + word.substring(1);
            key = dict.hashCode(word);
            found = word.equals(dict.get(key));
        }
        // suffix = "'s" drop
        if (!found && word.endsWith("'s")) {
            word = word.substring(0, word.length() - 2);
            key = dict.hashCode(word);
            found = word.equals(dict.get(key));
        }
        // suffix = "s" drop
        if (!found && word.endsWith("s")) {
            word = word.substring(0, word.length() - 1);
            key = dict.hashCode(word);
            found = word.equals(dict.get(key));
            // suffix = "es"
            if (!found && word.endsWith("es")) {
                word = word.substring(0, word.length() - 2);
                key = dict.hashCode(word);
                found = word.equals(dict.get(key));
            }
        }
        // suffix = "ed" drop
        if (!found && word.endsWith("ed")) {
            word = word.substring(0, word.length() - 2);
            key = dict.hashCode(word);
            found = word.equals(dict.get(key));
            // suffix "d" drop
            if (!found && word.endsWith("d")) {
                word = word.substring(0, word.length() - 1);
                key = dict.hashCode(word);
                found = word.equals(dict.get(key));
            }
        }
        // suffix = "er" drop
        if (!found && word.endsWith("er")) {
            word = word.substring(0, word.length() - 2);
            key = dict.hashCode(word);
            found = word.equals(dict.get(key));
            // suffix = "r" drop
            if (!found && word.endsWith("r")) {
                word = word.substring(0, word.length() - 1);
                key = dict.hashCode(word);
                found = word.equals(dict.get(key));
            }
        }
        // suffix = "ing" drop
        if (!found && word.endsWith("ing")) {
            word = word.substring(0, word.length() - 3);
            key = dict.hashCode(word);
            found = word.equals(dict.get(key));
            // replace "ing" with suffix = "e"
            if (!found) {
                word = word + "e";
                key = dict.hashCode(word);
                found = word.equals(dict.get(key));
            }
        }
        // suffix = "ly" drop
        if (!found && word.endsWith("ly")) {
            word = word.substring(0, word.length() - 2);
            key = dict.hashCode(word);
            found = word.equals(dict.get(key));
        }

        return found;       // true if any one of the above was successful
    }
}
