package com.example.gemseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.gemseeker.model.GameLogicManager;
import com.example.gemseeker.model.UserOptions;

import java.util.ArrayList;

/** This activity class displays the possible game configurations as saved in
 * the UserOptions class in two spinners: one for board size, and one for
 * the number of gems to be available for finding on the board.
 *
 * Options are auto saved upon selection, and new game board will be populated
 * when click 'Play Game' button on main menu.
 *
 * User's choice of configurations are saved between app launches using
 * SharedPreferences. **/

public class GameOptions extends AppCompatActivity {
    private GameLogicManager manager;
    private UserOptions options;
    private Spinner boardSpinner;
    private Spinner gemSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options);

        manager = manager.getInstance();
        options = options.getInstance();

        /** Set spinner for Board Sizes **/

        ArrayList<String> boardSizes = (ArrayList) options.getBoardSizes();

        boardSpinner = (Spinner) findViewById(R.id.boardSpinner);

        ArrayAdapter<String> boardSpinnerAdapter = new ArrayAdapter<> (
                this, R.layout.custom_spinner, boardSizes);
        boardSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner);
        boardSpinner.setAdapter(boardSpinnerAdapter);

        /** Store Board Spinner's choice and update max row and max col accordingly
         * within the GameLogicManager class. Doing so resets the current game board. **/

        boardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int selBoardSizePos = boardSpinner.getSelectedItemPosition();

                SharedPreferences boardSizeSharedPref = getSharedPreferences(getString(R.string.game_board_prefs), MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = boardSizeSharedPref.edit();
                prefEditor.putInt(getString(R.string.board_size_choice), selBoardSizePos);
                prefEditor.commit();

                switch (selBoardSizePos) {
                    case 0:
                        manager.setGameRowCol(4, 6);
                        break;
                    case 1:
                        manager.setGameRowCol(5, 10);
                        break;
                    case 2:
                        manager.setGameRowCol(6, 15);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        /** Store Board Size choice into SharedPreferences, and save selection in Spinner view
         * for next time app is launched and GameOptions is accessed again. **/

        SharedPreferences boardSizeSharedPref = getSharedPreferences(getString(R.string.game_board_prefs), MODE_PRIVATE);
        int boardSizeSpinnerVal = boardSizeSharedPref.getInt(getString(R.string.board_size_choice), -1);

        if (boardSizeSpinnerVal != -1) {
            boardSpinner.setSelection(boardSizeSpinnerVal);
        }

        /** Set spinner for Number of Gems **/

        ArrayList<String> numGems = (ArrayList) options.getNumGems();

        gemSpinner = (Spinner) findViewById(R.id.gemSpinner);

        ArrayAdapter<String> gemSpinnerAdapter = new ArrayAdapter<> (
                this, R.layout.custom_spinner, numGems);
        gemSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner);
        gemSpinner.setAdapter(gemSpinnerAdapter);

        /** Store Gem Spinner's choice and update number of gems to be found accordingly
         * within the GameLogicManager class. Doing so resets the current game board. **/

        gemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int selNumGemsPos = gemSpinner.getSelectedItemPosition();

                SharedPreferences numGemsSharedPref = getSharedPreferences(getString(R.string.game_gem_prefs), MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = numGemsSharedPref.edit();
                prefEditor.putInt(getString(R.string.num_gems_choice), selNumGemsPos);
                prefEditor.commit();

                switch (selNumGemsPos) {
                    case 0:
                        manager.setGameGems(6);
                        break;
                    case 1:
                        manager.setGameGems(10);
                        break;
                    case 2:
                        manager.setGameGems(15);
                        break;
                    case 3:
                        manager.setGameGems(20);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        /** Store Num Gems choice into SharedPreferences, and save selection in Spinner view
         * for next time app is launched and GameOptions is accessed again. **/

        SharedPreferences numGemsSharedPref = getSharedPreferences(getString(R.string.game_gem_prefs), MODE_PRIVATE);
        int numGemsSpinnerVal = numGemsSharedPref.getInt(getString(R.string.num_gems_choice), -1);

        if (numGemsSpinnerVal != -1) {
            gemSpinner.setSelection(numGemsSpinnerVal);
        }
    }
}
