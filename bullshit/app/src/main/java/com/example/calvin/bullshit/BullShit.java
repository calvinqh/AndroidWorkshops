package com.example.calvin.bullshit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import java.util.Random;
import java.util.ArrayList;

public class BullShit extends AppCompatActivity {

    private static Stack<Integer> deck = new Stack();
    private static ArrayList<Integer> user = new ArrayList();
    private static ArrayList<Integer> computer = new ArrayList();
    private static Stack<Integer> pile = new Stack();
    //user turn == 1, computer turn == 1
    private static int turnCounter = 0;
    private static int nextNumber = 1;
    private static Random random;

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
        for(int i = 0; i < 26; i++) {
            user.add(deck.pop());
            computer.add(deck.pop());
        }
        nextNumber = 1;

        //update the UI
        updateScreen();
    }

    public void reset(View view) {
        setUpGame();
    }

    public boolean submitCard(View view) {
        EditText card = (EditText) findViewById(R.id.entered_card);

        //check if "card" entered is valid (is number and exist in user hand)
        String input = card.getText().toString();
        String numbers = "0123456789";
        for(int i = 0; i < input.length(); i++) {
            if(numbers.indexOf(input.charAt(i)) == -1) {
                updateStatus("Invalid input");
                return false;
            }
        }
        if(!user.contains(Integer.parseInt(input))) {
            updateStatus("You don't have that number");
            return false;
        }


        //remove from hand, put into pile
        user.remove(user.indexOf(Integer.parseInt(input)));
        pile.push(Integer.parseInt(input));
        proceedToNextNum();
        updateScreen();

        //computer turn
        computerTurn();

        //check winner
        if(checkWinner() == 1)
            updateStatus("You Won!");
        else if (checkWinner() == -1)
            updateStatus("You Lost!");
        return true;
    }

    public void updateStatus(String stat) {
        TextView status = (TextView) findViewById(R.id.status);
        status.setText("Next Number: " + nextNumber + "  Pile Size: " + pile.size() + "    " + stat);
    }

    public void updateScreen() {
        TextView user_hand = (TextView) findViewById(R.id.user_cards);
        String user_cards = "";
        ListIterator<Integer> it = user.listIterator();
        user_cards += it.next();
        while(it.hasNext()) {
            user_cards = user_cards + ", " + it.next();
        }
        user_hand.setText(user_cards);
        updateStatus("");
    }

    public void proceedToNextNum() {
        nextNumber = (nextNumber+1)%53;
    }

    public void computerTurn() {
        //call bluff if user places card that you own
        if(computer.contains(nextNumber-1)) {
            while(!pile.isEmpty()) {
                user.add(pile.pop());
            }
        }
        //if users deck is empty, computer loses
        if(user.isEmpty()) {
            updateStatus("You Lost!");
            return;
        }


        /*
        int score = 0, maxScore = -1;
        int bestMove = -1;
        ListIterator<Integer> it = computer.listIterator();
        while(it.hasNext()) {
            pile.push(it.next());
            it.remove();
            score = MinMax(1);
            if(maxScore < score) {
                maxScore = score;
                bestMove = pile.peek();
            }
            it.add(pile.pop());

        }
        Log.i("Status","Best Move " + bestMove + " Max Score " + maxScore);
        computer.remove(bestMove);
        pile.push(bestMove);
        */

        //choose best card to put down
        if(computer.contains(nextNumber)) {
            pile.push(nextNumber);
            computer.remove(nextNumber);
        } else {
            pile.push((Integer)computer.toArray()[0]);
            computer.remove((Integer) computer.toArray()[0]);
        }

        //update board
        proceedToNextNum();
        updateScreen();
    }


    public int MinMax(int player) {
        if(checkWinner()!=0)
            return checkWinner() * player;

        int maxScore = 0;
        int score = 0;
        ListIterator<Integer> it;
        if(player == -1) {
            //computerTurn find max of min max
            it = computer.listIterator();
            while(it.hasNext()) {
                pile.push(it.next());
                it.remove();
                score = MinMax(1);
                if(maxScore < score)
                    maxScore = score;
                it.add(pile.peek());
                pile.pop();
            }
        } else {
            //userTurn find min of min max
            it = user.listIterator();
            while (it.hasNext()) {
                pile.push(it.next());
                it.remove();
                score = MinMax(-1);
                if (maxScore > score)
                    maxScore = score;
                it.add(pile.peek());
                pile.pop();
            }
        }
        return maxScore;
    }

    //user == 1, computer == -1, no winner == 0
    public int checkWinner() {
        if(computer.isEmpty())
            return -1;
        else if (user.isEmpty())
            return 1;
        return 0;
    }

    //challenge the computer
    public void challenge(View view) {
        if(pile.isEmpty())
            return;

        //if the placed card is actually the card
        if(pile.peek() == nextNumber-1){
            //user takes all the cards
            while(!pile.isEmpty())
                user.add(pile.pop());
        } else {
            while (!pile.isEmpty())
                computer.add(pile.pop());
        }
        //update user hand
        updateScreen();

    }

    public void printUser(){
        ListIterator<Integer> it = user.listIterator();
        Log.i("","--User--");
        while(it.hasNext()) {
            Log.i("",""+it.next());
        }
    }
    public void printComp(){
        ListIterator<Integer> it = computer.listIterator();
        Log.i("","--Computer--");
        while(it.hasNext()) {
            Log.i("Tag: ",""+it.next());
        }
    }
}
