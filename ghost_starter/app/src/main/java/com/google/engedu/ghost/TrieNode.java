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

import java.util.HashMap;
import java.util.Set;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        //full word is added
        if (s.length() == 0) {
            this.isWord = true;
            return;
        }

        TrieNode letter = children.get(s.substring(0, 1));

        //if a node for the letter does not exist
        if (letter == null) {
            children.put(s.substring(0, 1), new TrieNode());
        }
        letter = children.get(s.substring(0, 1));
        letter.add(s.substring(1));
    }

    public boolean isWord(String s) {
        if (s.length() == 0)
            return this.isWord;

        TrieNode letter = children.get(s.substring(0, 1));
        if (letter != null)
            return letter.isWord(s.substring(1));
        return false;
    }

    public TrieNode tranverseToPrefix(String s) {
        TrieNode next = null;
        if (s.length() == 0)
            return next;
        String letter = s.substring(0, 1);
        next = children.get(letter);
        return next.tranverseToPrefix(s.substring(1));
    }

    public String getAnyWordStartingWith(String s) {
        if (s.length() != 0) {
            String letter = s.substring(0, 1);
            TrieNode nextLet = children.get(letter);
            if (nextLet == null) //The frag is invalid
                return null;
            String suffix = nextLet.getAnyWordStartingWith(s.substring(1));
            if (suffix == null) //There is no suffix that exist for this prefix
                return null;
            return letter + suffix;
        } else {
            Set<String> keys = children.keySet();
            for (String key : keys) {
                if (children.get(key).isWord) {
                    return key;
                } else {
                    return getAnyWordStartingWith(key);
                }
            }
            return "";
        }
    }

    public String getGoodWordStartingWith(String s) {
        return null;
    }
}
