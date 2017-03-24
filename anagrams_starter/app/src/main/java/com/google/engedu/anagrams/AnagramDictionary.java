/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static ArrayList<String> wordList = new ArrayList();
    private static HashSet<String> wordSet = new HashSet();
    private static HashMap<String, ArrayList<String>> lettersToWords = new HashMap();
    private static HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap();
    private static int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String sortedWord = sortLetters(word);
            wordSet.add(word);
            wordList.add(word);
            if(sizeToWords.containsKey(word.length())) {
                ArrayList<String> wList = sizeToWords.get(word.length());
                wList.add(word);
            }
            else

                sizeToWords.put(word.length(), new ArrayList<String>());
            if ( !lettersToWords.containsKey(sortedWord) )
                lettersToWords.put(sortedWord, new ArrayList<String>());
            else {
                ArrayList<String> anagramList = lettersToWords.get(sortedWord);
                anagramList.add(word);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if( !wordSet.contains(word) && word.contains(base) )
            return false;
        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        String sortedWord = sortLetters(targetWord);
        if( lettersToWords.containsKey(sortedWord) ) 
            return lettersToWords.get(sortedWord);
        return null;
    }

    private String sortLetters(String targetWord) {
        char[] charArray = targetWord.toCharArray();
        Arrays.sort(charArray);
        String sortedWord = new String(charArray);
        return sortedWord;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for ( int i = 0; i < 25; i++ ) {
            String newWord = word + alphabet.charAt(i);
            String sortedKey = sortLetters(newWord);
            if(lettersToWords.containsKey(sortedKey))
                result.addAll(lettersToWords.get(sortedKey));
        }
        return result;
    }

    public String pickGoodStarterWord() {
        String result = "";
        ArrayList<String> wList = sizeToWords.get(this.wordLength);
        this.wordLength++;
        result = wList.get(random.nextInt(wList.size())+1);
        while ( getAnagramsWithOneMoreLetter(result).size() < this.MIN_NUM_ANAGRAMS )
            result = wList.get(random.nextInt(wList.size())+1);
        return result;
    }
}
