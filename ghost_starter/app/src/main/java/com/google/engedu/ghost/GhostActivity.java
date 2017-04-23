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

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            dictionary = new FastDictionary(assetManager.open("words.txt"));
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        userTurn = false;
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView frag = (TextView) findViewById(R.id.ghostText);
        if(frag.getText().length() >= 4 && dictionary.isWord((String)frag.getText())) {
            label.setText("Computer Victory");
            return;
        }
        String str_frag = (String) frag.getText();
        String new_word = this.dictionary.getAnyWordStartingWith(str_frag);
        if(new_word == null) {
            //word does not exist, challenge the user
            label.setText("You Lost");
        } else {
            //The word exsists
            new_word = new_word.substring(str_frag.length());
            String updateFrag = (String) frag.getText();
            updateFrag += new_word.charAt(0);
            frag.setText(updateFrag);
        }
        userTurn = true;
        label.setText(USER_TURN);
    }

    private void challengeUser() {
        TextView status = (TextView) findViewById(R.id.gameStatus);
        status.setText("Player has entered an invalid word, Computer Wins!");
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        char pressedKey = (char)event.getUnicodeChar();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        if(alphabet.indexOf(pressedKey)==-1)
            return super.onKeyUp(keyCode, event);
        TextView text = (TextView) findViewById(R.id.ghostText);
        TextView status = (TextView) findViewById(R.id.gameStatus);
        String newFrag = (String)text.getText();
        newFrag += pressedKey;
        text.setText(newFrag);
        if(this.dictionary.isWord(newFrag)) {
            status.setText("Valid Word");
        } else {
            status.setText("Invalid Word");
        }
        this.userTurn=false;
        this.computerTurn();
        return true;
    }

    public void challenge(View view) {
        TextView frag = (TextView) findViewById(R.id.ghostText);
        TextView status = (TextView) findViewById(R.id.gameStatus);
        if(frag.getText().length() > 4 && dictionary.isWord((String)frag.getText())){
            status.setText("User Wins!");
        }
        String word = dictionary.getAnyWordStartingWith((String)frag.getText());
        if(word != null)
            status.setText("Computer Wins");
        else
            status.setText("User Wins");

    }
}
