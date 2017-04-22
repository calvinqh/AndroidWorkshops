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

package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;


public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private Random random = new Random();


    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix.length() == 0) {
            int r = this.random.nextInt(this.words.size());
            return this.words.get(r);
        }
        int newIndex = BinarySearch(prefix);
        if (newIndex >= 0)
            return this.words.get(newIndex);
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        return selected;
    }

    public int BinarySearch(String prefix) {
        int first = 0;
        int last = words.size()-1;
        while(first<=last) {
            int mid = (first+last)/2;
            String word = this.words.get(mid);
            String word_prefix = "";
            if(word.length() > prefix.length()) {
                word_prefix = word.substring(0, prefix.length());
                if (word_prefix.compareTo(prefix) == 0)
                    return mid;
            }
            else if (first == last)
                break;
            if(word.compareTo(prefix) < 0)
                first = mid + 1;
            else
                last = mid - 1;
        }
        return -1;
    }
}
