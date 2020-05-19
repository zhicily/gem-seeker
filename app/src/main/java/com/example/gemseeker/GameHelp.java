package com.example.gemseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/** This activity class holds the description for game play instructions
 * as well as details on the author/creator and necessary citations from
 * resources used to create this game. **/

public class GameHelp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_help);

        TextView helpDescription = (TextView) findViewById(R.id.helpDescrip);

        /** Set-up clickable links in description. **/
        helpDescription.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
