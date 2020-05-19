package com.example.gemseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

/** This activity class sets up the main menu for the game. **/

public class MainMenuActivity extends AppCompatActivity {

    private Button gameOpButton;
    private Button gameHelpButton;
    private Button newGameButton;
    Handler reset_background = new Handler();
    Runnable interact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        newGameButton = (Button) findViewById(R.id.newGame);
        gameOpButton = (Button) findViewById(R.id.gameOptions);
        gameHelpButton = (Button) findViewById(R.id.gameHelp);

        /** Runnable to reset button colors to pre-pressed colours
         * when user returns back to main menu after exiting one of
         * the below child activities. **/

        interact = new Runnable() {
            public void run() {
                newGameButton.setBackgroundResource(R.drawable.rounded_button);
                gameOpButton.setBackgroundResource(R.drawable.rounded_button);
                gameHelpButton.setBackgroundResource(R.drawable.rounded_button);
            }
        };

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGameButton.setBackgroundResource(R.drawable.rounded_button_pressed);
                Intent mainMenuIntent = makeIntentForNewGame(MainMenuActivity.this);
                startActivity(mainMenuIntent);
                resetButton();
            }
        });

        gameOpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOpButton.setBackgroundResource(R.drawable.rounded_button_pressed);
                Intent mainMenuIntent = makeIntentForGameOptions(MainMenuActivity.this);
                startActivity(mainMenuIntent);
                resetButton();
            }
        });

        gameHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameHelpButton.setBackgroundResource(R.drawable.rounded_button_pressed);
                Intent mainMenuIntent = makeIntentForGameHelp(MainMenuActivity.this);
                startActivity(mainMenuIntent);
                resetButton();
            }
        });
    }


    public static Intent makeIntentForGameOptions (Context context) {
        return new Intent (context, GameOptions.class);
    }

    public static Intent makeIntentForGameHelp (Context context) {
        return new Intent (context, GameHelp.class);
    }

    public static Intent makeIntentForNewGame (Context context) {
        return new Intent (context, GameScreen.class);
    }

    /** Reset button to pre-pressed colour with 1 second delay.
     * This occurs after user has already entered next activity, so
     * they will not see the change as it occurs. **/

    public void resetButton() {
        reset_background.postDelayed(interact, 1000);
    }
}
