package com.example.calvin.bullshit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Collections;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

public class BullShit extends AppCompatActivity {

    private static Stack<Integer> deck = new Stack();
    private static Set<Integer> user = new HashSet();
    private static Set<Integer> computer = new HashSet();
    private static Stack<Integer> pile = new Stack();
    //user turn are even; computer turn are odd
    private static int turnCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bull_shit);

        setUpGame();
    }

    public void shuffle(Stack s) {
        Collections.shuffle(s);
    }

    public void setUpGame() {
        //clear all ds
        while(!deck.empty())
            deck.pop();
        user.clear();
        computer.clear();
        while(!pile.empty())
            pile.pop();

        //create deck of cards and shuffle
        for(int i = 1; i <= 52 ; i++) {
            deck.push(i);
        }
        shuffle(deck);

        //give each player 5 cards each from the deck
        for(int i = 0; i < 5; i++) {
            user.add(deck.pop());
            computer.add(deck.pop());
        }

        //show user cards
        String user_cards = "";
        for(Object c: user.toArray()) {
            user_cards = user_cards + ", " + c.toString();
        }
        TextView user = (TextView) findViewById(R.id.user_cards);
        user.setText(user_cards);
    }


    public boolean submitCard(View view) {
        EditText card = (EditText) findViewById(R.id.entered_card);

        //check if "card" entered is valid (is number and exist in user hand

        //remove from hand, put into pile
        user.remove(Integer.parseInt(card.getText().toString()));
        pile.push(Integer.parseInt(card.getText().toString()));
        updateScreen();

        //computer turn
        return true;
    }

    public void updateScreen() {
        TextView user_hand = (TextView) findViewById(R.id.user_cards);
        TextView pile_size = (TextView) findViewById(R.id.pile_size);
        String user_cards = "";
        for(Object c: user.toArray()) {
            user_cards = user_cards + ", " + c.toString();
        }
        user_hand.setText(user_cards);
        pile_size.setText(""+pile.size());

    }

    public void computerTurn() {

    }

}
