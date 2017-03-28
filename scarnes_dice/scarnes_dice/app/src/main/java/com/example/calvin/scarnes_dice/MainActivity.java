package com.example.calvin.scarnes_dice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.TextView;
import android.view.ButtonView;
import android.view.ImageView;
import android.util.Random
import android.content.Context;


public class MainActivity extends AppCompatActivity {

    public Random random = new Random();
    private static int u_overall_score = 0;
    private static int u_turn_score = 0;
    private static int c_overall_score = 0;
    private static int c_turn_score = 0;

    private static HashMap<int,int> dice_resources = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dice_resources.put(1,R.drawable.dice1);
        dice_resources.put(2,R.drawable.dice1);
        dice_resources.put(3,R.drawable.dice1);
        dice_resources.put(4,R.drawable.dice1);
        dice_resources.put(5,R.drawable.dice1);
        dice_resources.put(6,R.drawable.dice1);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        //Do something in response to button
    }

    /** Called when the user taps the ROLL button*/
    public void roll(View view) {
        int roll_val = this.roll_helper();
        if(score > 1)
            this.u_turn_score += roll_val;
        else {
            this.u_turn_score = 0;
            this.update_scoreboard();
            this.computerTurn(); //user ends turn, now its computer turn
        }
        this.update_scoreboard();
    }

    /** Rolls the dice(update the image), returns the value from the roll*/
    public int roll_helper() {
        int rndNum = random.nextInt(6)+1;
        Drawable new_dice = Context.getResources().getDrawable(this.dice_resources.get(rndNum));
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(new_dice);
        return rndNum;
    }

    /** Called when the user taps the REST button*/
    public void reset(View view) {
        this.u_overall_score = 0;
        this.u_turn_score = 0;
        this.c_overall_score = 0;
        this.c_turn_score = 0;
        //update the scoreboard
        this.update_scoreboard();
    }

    /** Called when the user taps the HOLD button */
    public void hold(View view) {
        this.u_turn_score = 0;
        //update the scoreboard
        this.update_scoreboard();

        //computer turn
        this.computerTurn();
    }

    public void update_scoreboard() {
        TextView scoreboard = (TextView) findViewById(R.id.textview2);
        String message = "Your score: " + this.u_overall_score + " Computer Score: " + this.c_overall_score + " Turn Score:" + this.u_turn_score;
        scoreboard.setText(message);
    }

    /** Called when user presses HOLD turn */
    public void computerTurn() {
        ButtonView roll_butt = (ButtonView) findViewById(R.id.button4);
        ButtonView hold_butt = (ButtonView) findViewById(R.id.button5);
        roll_butt.setEnabled(false);
        hold_butt.setEnabled(false);
        while( c_turn_score < 20 ) {
            int roll_val = this.roll_helper();
            if(roll_val > 1)
                c_turn_score += roll_val;
            else {
                c_turn_score = 0;
                this.update_scoreboard();
                break; //end turn when roll value is 1
            }
            this.update_scoreboard();
        }
    }

}
