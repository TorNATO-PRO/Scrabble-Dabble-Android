package tornato.dev.ScrabbleDabble;

/**
 * A Word object which stores data about a given word and
 * implements the Comparable interface.
 *
 * @author Nathan Waltz
 * @version 0.1
 * @since 2020-12-30
 */
public class Word implements Comparable<Word> {

    // fields storing the String representation
    // of the word, and the score
    private final String word;
    private final int score;

    /**
     * Constructor for the Word object
     *
     * @param word  The word which is inputted by
     *              the client as a String
     * @param score The score which corresponds to
     *              to the cumulative sum of all the character's scores
     *              in the word
     */
    public Word(String word, int score) {
        this.word = word;
        this.score = score;
    }

    /**
     * compareTo: Compares one word to another
     *
     * @param other Another Word object that we want to compare
     * @return An integer corresponding to the comparison between
     * the values of these two objects, namely their scores
     */
    @Override
    public int compareTo(Word other) {
        return other.score - this.score;
    }

    /**
     * getWord: Getter for the word
     *
     * @return A string representation of the word
     */
    public String getWord() {
        return word;
    }

    /**
     * getScore: Getter for the score
     *
     * @return An integer representation of the score
     */
    public int getScore() {
        return score;
    }
}
