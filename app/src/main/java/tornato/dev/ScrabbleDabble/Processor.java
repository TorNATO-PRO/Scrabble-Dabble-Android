/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020. Nathan Waltz
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tornato.dev.ScrabbleDabble;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.IntStream;

/**
 * A main method for my Scrabble program. Basically, what this program allows you
 * to do is look up if a given word is a valid scrabble word, or cheat based on the
 * current characters that you hold in your deck!
 *
 * @author Nathan Waltz
 * @version 0.1
 * @since 2020-12-30
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class Processor {

    /**
     * A dictionary of all the words that are available in scrabble
     */
    public Map<String, Word> wordMap;
    public Queue<Word> wordList;
    private Map<Character, Integer> characterValues;

    // loads data from files
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Processor(InputStream dictionary, InputStream characterValues) {
        try {
            this.wordList = new PriorityQueue<>();
            this.characterValues = readCharacterValues(characterValues);
            this.wordMap = readDictionary(dictionary);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * readDictionary method: Takes in a filename and reads the words quickly
     * using a BufferedReader, and afterwards stores the words in a
     * dictionary for O(1) access time
     *
     * @return A HashMap containing a dictionary which consists of the word as a
     * String and the corresponding Word object
     * @throws IOException If something goes wrong with reading the file, it throws
     *                     this exception
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, Word> readDictionary(InputStream dictionary)
            throws IOException {
        BufferedReader br;
        Map<String, Word> wordMap = new HashMap<>();
        try {
            br = new BufferedReader(new InputStreamReader(dictionary));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                validateWordListLine(currentLine, wordMap);
            }
            br.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        return wordMap;
    }

    /**
     * A helper method to verify that the input from the
     * wordlist is indeed a valid word that fits within the
     * constraints of this program
     *
     * @param line    A line that is provided by the client that they wish
     *                to be validated
     * @param wordMap A word map that acts as a dictionary-like data structure
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void validateWordListLine(String line, Map<String, Word> wordMap) {
        String toUpperCase = line.toUpperCase();
        // fields for storing the maximum possible input, a wordlist, and character values
        // size of scrabble board
        int MAX_INPUT = 15;
        if (toUpperCase.length() <= MAX_INPUT && isAlphabetic(toUpperCase)) {
            Word w = new Word(toUpperCase, score(toUpperCase));
            wordList.add(w);
            wordMap.put(toUpperCase, w);
        }
    }

    /**
     * readDictionary method: Takes in a filename and reads the characters quickly
     * using a BufferedReader, and afterwards stores the words in a
     * dictionary for O(1) access time
     *
     * @return A HashMap containing a dictionary which consists of a character as
     * well as score of that character
     * @throws IOException If something goes wrong with reading the file, it throws
     *                     this exception
     */
    public Map<Character, Integer> readCharacterValues(
            InputStream characterValues
    ) throws IOException {
        BufferedReader br;
        Map<Character, Integer> valueOfCharacter = new HashMap<>();
        try {
            br = new BufferedReader(new InputStreamReader(characterValues));
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                parseCharacterValues(currentLine, valueOfCharacter);
            }
            br.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        return valueOfCharacter;
    }

    /**
     * Parses the character file to obtain the scores for those characters
     *
     * @param line             The line passed by a client which is intended to be parsed
     * @param valueOfCharacter A dictionary-like data structure containing all of the
     *                         characters in the file as well as their corresponding integer
     *                         value
     */
    private void parseCharacterValues(
            String line,
            Map<Character, Integer> valueOfCharacter
    ) {
        String[] values = line.split(",");
        Character character = values[0].trim().charAt(0);
        Integer val = Integer.parseInt(values[1].trim());
        valueOfCharacter.put(character, val);
    }

    /**
     * Cheat method: Allows the user to cheat given a sequence
     * of characters
     *
     * @param input A scanner parameter which allows
     *              for the user to enter a character sequence
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Word> cheat(String input) {
        String upperCaseInput = input.toUpperCase();
        List<Word> list = new ArrayList<>();
        if (input.length() <= 8) {
            Queue<Word> combos = genCombos(upperCaseInput.toCharArray());
            while (!combos.isEmpty()) {
                list.add(combos.remove());
            }
        }
        return list;
    }

    /**
     * Lookup method: Allows the user to see if a given String
     * exists
     *
     * @param input A string parameter which allows
     *              for the user to enter a word
     */
    public boolean lookup(String input) {
        String upperCaseInput = input.toUpperCase();
        return wordMap.containsKey(upperCaseInput);
    }

    /**
     * Checks to see if all of the character in a String
     * are alphabetic
     *
     * @param input A given String provided by the client
     * @return Boolean representing whether or not all of the
     * characters in the input are alphabetic
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isAlphabetic(String input) {
        char[] charArray = input.toCharArray();
        for (char c : charArray) {
            if (!Character.isAlphabetic(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates a combinations of characters from a given character
     * array
     *
     * @param word A character array which contains characters for which
     *             we wish to find combinations
     * @return Combination of all the characters that are
     * contained in the passed word character array
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Queue<Word> genCombos(char[] word) {
        Queue<Word> subsets = new PriorityQueue<>();
        if (word.length == 0) {
            return subsets;
        }
        int[] characterCount = characterCount(word);
        genCombos(word, subsets, new Stack<>(), new HashSet<>(), characterCount, new int[26]);
        return subsets;
    }

    private int[] characterCount(char[] sequence) {
        int[] count = new int[26];
        for (char c : sequence) {
            count[characterToInt(c)] += 1;
        }
        return count;
    }

    /**
     * A helper method which does the heavy lifting and generates the combinations
     * of the power set
     *
     * @param word    A character array which contains characters for which
     *                we wish to find a combination
     * @param subsets A set of subsets of the word parameter
     * @param temp    A stack of characters which stores values obtained throughout
     *                the execution of this method
     * @param words   A set which contains the Strings generated through the duration
     *                of this method's recursion
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void genCombos(
            char[] word,
            Queue<Word> subsets,
            Stack<Character> temp,
            Set<String> words,
            int[] wordCharacterCount,
            int[] tempCharacterCount
    ) {
        // Uses hashset to maintain O(1) time in checking validity
        // NOTE: I cannot simply use a TreeSet, becomes I am comparing
        // objects based on score
        String result = toString(new ArrayList<>(temp));
        if (wordMap.containsKey(result) && !words.contains(result)) {
            subsets.add(wordMap.get(result));
            words.add(result);
        }

        // loops through the word and recursively backtracks
        // in a functional paradigm manner
        IntStream.range(0, word.length).forEach(i -> {
            char c = word[i];
            if (wordCharacterCount[characterToInt(c)] > tempCharacterCount[characterToInt(c)]) {
                temp.push(c);
                tempCharacterCount[characterToInt(c)] += 1;
                genCombos(word, subsets, temp, words, wordCharacterCount, tempCharacterCount);
                tempCharacterCount[characterToInt(temp.pop())] -= 1;
            }
        });
    }

    /**
     * Converts a list of characters to a String
     *
     * @param array A list of characters which will be converted to
     *              a string
     * @return A string representation of the passed array parameter
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String toString(List<Character> array) {
        if (array.isEmpty()) {
            return "";
        }
        StringBuilder temp = new StringBuilder();
        array.forEach(temp::append);
        return temp.toString();
    }

    /**
     * Finds a score for a given word
     *
     * @param word A word passed by the client
     * @return A score corresponding to the cumulative
     * score of that word
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int score(String word) {
        return IntStream
                .range(0, word.length())
                .map(i -> characterValues.get(word.charAt(i)))
                .sum();
    }

    private int characterToInt(char c) {
        if (64 < c && c < 91) {
            return c - 65;
        }
        return 0;
    }
}
