package com.example.calvinquach.scarnes_dice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.Random;
import java.util.HashMap;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import java.util.logging.LogRecord;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    public Random random = new Random();
    private static int u_overall_score = 0;
    private static int u_turn_score = 0;
    private static int c_overall_score = 0;
    private static int c_turn_score = 0;
    private static HashMap<Integer, Integer> dice_resources = new HashMap();
    private static int test = 0;

    public final Handler h = new Handler();
    public Runnable r = new Runnable() {
        @Override
        public void run() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dice_resources.put(1,R.drawable.dice1);
        dice_resources.put(2,R.drawable.dice2);
        dice_resources.put(3,R.drawable.dice3);
        dice_resources.put(4,R.drawable.dice4);
        dice_resources.put(5,R.drawable.dice5);
        dice_resources.put(6,R.drawable.dice6);

    }

    /** Called when the user taps the ROLL button*/
    public void roll(View view) {
        int roll_val = this.roll_helper();
        if(roll_val > 1)
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
        ImageView image = (ImageView) findViewById(R.id.imageView);
        image.setImageResource(this.dice_resources.get(rndNum));
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
        this.u_overall_score += this.u_turn_score;
        this.u_turn_score = 0;
        //update the scoreboard
        this.update_scoreboard();

        //computer turn
        r = new Runnable() {
            @Override
            public void run() {
                if(computerTurn()){
                    h.postDelayed(this,500);
                }
                else {
                    c_turn_score = 0;
                    h.removeCallbacks(r);
                }
            }
        };
        h.postDelayed(r,0);

    }

    public void update_scoreboard() {
        TextView scoreboard = (TextView) findViewById(R.id.scoreboard);
        String message = "Your score: " + this.u_overall_score + " Computer Score: " + this.c_overall_score + " Turn Score:" + this.u_turn_score + " C Turn Score: " + this.c_turn_score;
        scoreboard.setText(message);
    }


    /** Called when user presses HOLD turn */
    public boolean computerTurn() {
        Button roll_butt = (Button) findViewById(R.id.rollbutton);
        Button hold_butt = (Button) findViewById(R.id.holdbutton);
        roll_butt.setEnabled(false);
        hold_butt.setEnabled(false);
        if (this.c_turn_score < 20) {
            int roll_val = this.roll_helper();
            if (roll_val > 1) {
                this.c_turn_score += roll_val;
            }
            else {
                this.c_turn_score = 0;
                this.computerTurnHelper();
                this.update_scoreboard();
                return false;
            }
            this.computerTurnHelper();
            this.update_scoreboard();
            return true;
        }
        this.c_turn_score = 0;
        this.computerTurnHelper();
        this.update_scoreboard();
        return false;
    }

    /** Helps clean up computer turn. Releases the buttons and adds score up to overall for computer/*/
    public void computerTurnHelper() {
        Button roll_butt = (Button) findViewById(R.id.rollbutton);
        Button hold_butt = (Button) findViewById(R.id.holdbutton);
        roll_butt.setEnabled(true);
        hold_butt.setEnabled(true);
        this.c_overall_score += this.c_turn_score;
    }
}

