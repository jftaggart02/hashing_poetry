import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;

public class WritePoetry {

    /**
     * Generate a random poem based on the provided poetry example.
     *
     * @param file The document to read
     * @param startWord The first word of the poem to generate
     * @param length How many words (including punctuation) to generate
     * @param printHashtable If true, then print the hash table to the console
     *
     * @return The generated poem.
     * */
    public String writePoem(String file, String startWord, int length, boolean printHashtable) {

        // Read the file and create an array list of strings, where each string is a word or punctuation mark.
        var words = new ArrayList<String>();
        try (var scanner = new Scanner(new File(file))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Loop through each space-separated string in the line
                for (String str : line.split(" ")) {

                    String strLower = str.toLowerCase();

                    char lastChar = strLower.charAt(strLower.length() - 1);

                    // If the last character is not a letter or digit (punctuation) and the string is more than 1 char long,
                    // separate the string into the word and punctuation
                    if (!Character.isLetterOrDigit(lastChar) && strLower.length() > 1) {
                        String word = strLower.substring(0, strLower.length() - 1);
                        String punctuation = Character.toString(lastChar);
                        words.add(word);
                        words.add(punctuation);
                    } else {
                        words.add(strLower);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Create the hash table
        var hashTable = new HashTable<String, WordFreqInfo>();

        // Loop through the array list two at a time.
        for (int i = 0; i < words.size() - 1; i += 2) {
            // Indicate that the second word follows the first word and update the count accordingly.
            String firstWord = words.get(i);
            String secondWord = words.get(i+1);

            // If the first word is in the hash table already, update its WordFreqInfo.
            WordFreqInfo info = hashTable.find(firstWord);
            if (info != null) {
                info.updateFollows(secondWord);
            }

            // Else, insert the word into the hash table
            else {
                var newInfo = new WordFreqInfo(firstWord, 0);
                newInfo.updateFollows(secondWord);
                hashTable.insert(firstWord, newInfo);
            }

        }

        // Generate the poem
        var rnd = new Random();
        for (int i = 0; i < length; i++) {

        }

    }

}
