import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;

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

                    if (str.isEmpty()) {
                        continue;
                    }

                    str = str.toLowerCase();

                    char lastChar = str.charAt(str.length() - 1);

                    // If the last character is punctuation and the string is more than 1 char long,
                    // separate the string into the word and punctuation
                    if (isPunctuation(lastChar) && str.length() > 1) {
                        String word = str.substring(0, str.length() - 1);
                        String punctuation = Character.toString(lastChar);
                        words.add(word);
                        words.add(punctuation);
                    } else {
                        words.add(str);
                    }
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("The file " + file + " was not found. Please try again.");
            return null;
        }

        // Create the hash table
        var hashTable = new HashTable<String, WordFreqInfo>();

        // Loop through the array list two at a time.
        for (int i = 0; i < words.size() - 1; i += 1) {
            String firstWord = words.get(i);
            String secondWord = words.get(i+1);

            // If the first word is in the hash table already, update its info.
            WordFreqInfo info = hashTable.find(firstWord);
            if (info != null) {
                info.updateFollows(secondWord);
            }

            // Else, insert the first word into the hash table and update its info.
            else {
                var newInfo = new WordFreqInfo(firstWord, 0);
                newInfo.updateFollows(secondWord);
                hashTable.insert(firstWord, newInfo);
            }

        }

        if (!hashTable.contains(startWord)) {
            System.out.println("Error: startWord is not in the hash table.");
            return null;
        }

        // Randomly get sequence of words based on probability that a word will follow.
        var poem = new ArrayList<String>();
        poem.add(startWord);
        var rnd = new Random();
        String currentWord = startWord;
        for (int i = 0; i < length; i++) {
            WordFreqInfo info = hashTable.find(currentWord);
            if (info == null) {
                System.out.println("Error: could not find " + currentWord);
                return null;
            }
            currentWord = info.getFollowWord(rnd.nextInt(info.getOccurCount()));
            poem.add(currentWord);
        }

        // Generate a nice, printable string containing the poem
        var result = new StringBuilder();
        for (int i = 0; i < poem.size() - 1; i++) {
            String word = poem.get(i);
            String nextWord = poem.get(i+1);
            result.append(word);

            // If the word is punctuation, then add a new line
            if (isPunctuation(word)) {
                result.append("\n");
            }
            // Otherwise, if the next word is not punctuation, add a space
            else if (!isPunctuation(nextWord)) {
                result.append(" ");
            }

        }

        // Add the last word, and add a period if the last word is not punctuation.
        String lastWord = poem.getLast();
        result.append(lastWord);
        if (!isPunctuation(lastWord)) {
            result.append(".");
        }

        if (printHashtable) {
            System.out.println(hashTable.toString(1000));
        }

        return result.toString();

    }

    private static boolean isPunctuation(String s) {
        return s.length() == 1 && Pattern.matches("\\p{IsPunctuation}", s);
    }

    private static boolean isPunctuation(char c) {
        String s = String.valueOf(c);
        return Pattern.matches("\\p{IsPunctuation}", s);
    }

}
