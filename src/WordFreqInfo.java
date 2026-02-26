import java.util.*;

public class WordFreqInfo {

    public WordFreqInfo(String word, int count) {
        this.word = word;
        this.occurCount = count;
        this.followList = new ArrayList<Frequency>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Word :" + word+":");
        sb.append(" (" + occurCount + ") : ");
        for (Frequency f : followList) {
            sb.append(f.toString());
        }

        return sb.toString();
    }

    public void updateFollows(String follow) {
        this.occurCount++;
        boolean updated = false;
        for (Frequency f : followList) {
            if (follow.compareTo(f.follow) == 0) {
                f.followCount++;
                updated = true;
            }
        }
        if (!updated) {
            followList.add(new Frequency(follow, 1));
        }
    }

    public int getOccurCount() {
        return this.occurCount;
    }

    /**
     * Create a categorical probability distribution based on the occurrence counts of each of the following words.
     * Given a number in the distribution ("count" parameter), output the category it lies in (the following word).
     *
     * @param count A number (from 0 to "occurCount").
     * @return The following word.
     * */
    public String getFollowWord(int count) {
        int currentCount = 0;
        for (Frequency f : followList) {
            if (count < f.followCount + currentCount) {
                return f.follow;
            }
            currentCount += f.followCount;
        }
        System.out.println("Warning: 'count' is too high! Defaulting to last word in 'followList'");
        return followList.getLast().follow;
    }

    private String word;
    private int occurCount;  // occurrences of "word" in the document
    private ArrayList<Frequency> followList;  // A list of words that follow "word" and how many times they followed it.

    private static class Frequency {
        String follow;
        int followCount;

        public Frequency(String follow, int ct) {
            this.follow = follow;
            this.followCount = ct;
        }

        @Override
        public String toString() {
            return follow + " [" + followCount + "] ";
        }

        @Override
        public boolean equals(Object f2) {
            return this.follow.equals(((Frequency)f2).follow);
        }
    }
}
