package com.example.gemseeker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/** This activity class sets up a welcome splash screen upon
 * launching the app. **/

public class MainActivity extends AppCompatActivity {

    private Button skipButton;
    private int skippedFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView gem;

        /** Animation for rotating gem image on welcome splash screen. **/

        gem = (ImageView) findViewById(R.id.gemPng);
        Animation rotateGem = AnimationUtils.loadAnimation(this, R.anim.rotate);
        gem.startAnimation(rotateGem);

        /** Set up skip button. When button is pressed, splash screen activity
         * is terminated. A flag is set to signify that the activity has been skipped. **/

        skipButton = (Button) findViewById(R.id.skipButton);

        skipButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               skipButton.setBackgroundResource(R.drawable.rounded_button_pressed);
               Intent mainMenuIntent = makeIntentForMainMenu(MainActivity.this);
               skippedFlag = 1;
               startActivity(mainMenuIntent);
               finish();
           }
        });

        /** Without skipping, welcome splash screen will automatically fade into
         * next activity after 5 seconds, and splash screen activity is terminated.
         *
         * If a skip has been flagged, main menu will not be relaunched again after
         * 5 seconds. **/

        int WELCOME_SCREEN_TIME_OUT = 5000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (skippedFlag == 0) {
                    Intent mainMenuIntent = makeIntentForMainMenu(MainActivity.this);
                    startActivity(mainMenuIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                finish();
            }
        }, WELCOME_SCREEN_TIME_OUT);
    }

    public static Intent makeIntentForMainMenu (Context context) {
        return new Intent (context, MainMenuActivity.class);
    }
}
