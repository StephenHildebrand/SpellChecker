# SpellCheck
## Overview 
Application for checking word spelling implemented using a hash table. It is made up of two parts: 
  1. Building the hash table.
  2. Scanning the text for spelling errors using the hash table. 


### Description 
  * Studies have shown that the 25,000 most common English words account for more than 99% of all written text; indeed, this is true not just in English, but in other languages as well. 
  * A hash table is designed for an English spelling checker. When the program is run, it will first read in a file of the 25,144 most common English words.
  * These words will be entered into the hash table. Then, the user's document will be read word by word. Each word will be searched for in the hash table, and flagged as a possible spelling error if it is not found. 

### Input 
  * The word list of valid spellings is within the file: `dict.txt`.
  * Prompts the user for the name of the dictionary file containing the word list, the name of the text file to be spell checked, and the name of the output file.

## Hash Table Construction 
  * The dictionary contains 25,144 words. Even though the list is already in alphabetical order, using binary search to find a word would be quite slow, requiring 14-15 probes on average. With a hash table this should be reduced to fewer than 3. 
  * During the first phase, the program reads each word in the dictionary and stores it in a hash table.
  * The hash table is large enough to make searching fast, but not wastefully large. The size also obeys any constraints imposed by the collision resolution strategy. 

### Hash Function 
  * An appropriate hash function was selected to build and access the table.
  * Since the keys are strings of characters, a function operating on the word values of the characters is a good choice.
  * A good hash function is simple, fast, and keeps the number of collisions relatively small. 

### Hash Implementation Details
The hash table, implemented in `Dictionary.java`, was done as a table array of LinkedList buckets, with each entry containing a Word object implemented as a key-value pair. To handle collisions in the event that they occur, the buckets were implemented via separate chaining as a LinkedList where each `Word` in the LinkedList table points to the next in the event of collisions. Functions for getting, putting and removing were also implemented to allow for manipulation of the 'Dictionary' as needed. The program is run from SpellCheck.java and its associated class, which handles the processing of the dictionary file, input text file, and output file and contains an instance of the 'Dictionary' class for implementing these features. The process of actually checking the spelling of each text word and searching the dictionary for it, along with altering each word (such as adding/removing suffixes) to increase the likelihood of a successful dictionary is also handled within the `SpellCheck` class via a private method, `checkSpelling`.

To allow for easier, more straightforward access to the generated keys, the hash and compression functions were implemented separately with the compression function being private and internal to the `Dictionary` hash table class. Keys are compressed as they are entered during operations and are passed in along with the compressed hash value and the `String` dictionary value. Due to the keys being strings of characters, the hash function was implemented as a 5-bit cyclic-shift, as this was shown to be an ideal implementation in Data Structures and Algorithms in Java for both ensuring excellent performance and minimizing collisions. Further, the compression function was implemented by applying the Multiply, Add and Divide (MAD) method to the aforementioned hashcode function by dividing by a set prime number with the purpose of arriving at a load factor (*λ*) of approximately 0.75. Another `Dictionary` constructor was also provided that allows future client code to specify another prime factor to be used to calculate a reasonable capacity, *m*, based on the size of the dictionary, *n*.


### Collision Resolution
  An effective collision resolution method is also implemented, as mentioned above. To be completed...

## Checking the Text 
  * After reading the word file and building the hash table, the program will switch to its spelling-checking mode.
  * The program will read the user's text file word by word, checking the spelling of each word. If a word appears to be misspelled, the program will write it out. 
  * As the program searches for each text word, it counts the number of probes in the hash table. (For the purposes of counting, a probe occurs whenever a text word is compared to a word in the table, a probe does not occur when a text word is compared to an empty table entry.
  * The program prints out the total number of probes it made during the spelling-checking phase before it terminates. 
#### Note:
The dictionary does not contain every English word. As an example, it may contain confuse, but not confused or confusing. Certain common suffixes are stripped off and a match is attempted again if the word is not found in its original form. It always searches for the whole word first. If it is not found, then the following Match Rules apply.

### Match Rules  
  1. If the first letter of the word is capitalized, down-shift the first letter and try again; this helps to find words like The (e.g., words starting a new sentence).
  2. If the word ends in "'s", drop the "'s" and try again; this helps to find words like cook'ss.
  3. If the word ends in "s", drop the "s" and try again; this helps to find words like cakes. If it ends in "es", drop the "es" and search a third time; this helps to find dishes.
  4. If the word ends in "ed", drop the "ed" and try again (cooked); if the word is not found, then drop only the "d" and try a third time (baked).
  5. If the word ends in "er", drop the "er" and try again (cooker); if the word is not found, then drop only the "r" and try a third time (baker).
  6. If the word ends in "ing", drop the "ing" and try again (cooking); then replace "ing" with "e" and try a third time (baking).
  7. If the word ends in "ly", drop the "ly" and try again (deliciously).
  8. Finally, an apostrophe is regarded as a letter. This prevents words like "don't" from being broken into two pieces in either phase of the program. 

This list of rules is obviously not complete. You should expect some correctly spelled words to be flagged as misspelled by the program as well as some incorrectly spelled words.

## Output
The program will not generate any output when building the hash table. During the checking phase, it should output any word whose spelling it deems to be questionable. It should also report:
  1. The number of words in the dictionary.
  2. The number of words in the text to be spell-checked.
  3. The number of misspelled words in the text.
  4. The total number of probes during the checking phase.
  5. The average number of probes per word (of the original text file) checked.
  6. The average number of probes per lookup operation.

  Note that a single word may require multiple lookup operations, per the above rules.