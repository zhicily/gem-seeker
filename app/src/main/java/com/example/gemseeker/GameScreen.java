package com.example.gemseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gemseeker.model.MsgFragment;
import com.example.gemseeker.model.GameLogicManager;
import com.example.gemseeker.model.UserOptions;

/** This activity class sets up the UI for the game screen, and passes on
 * and updates data to GameLogic class as user performs moves in-game. **/

public class GameScreen extends AppCompatActivity {

    private GameLogicManager manager;
    private UserOptions options;

    private int[][] seeker;
    private int[][] isGem;
    private Button[][] grid;
    private int[][] clicked;

    private TextView gemsFound;
    private TextView scansUsed;

    private int totalGems;
    private int countGemsFound;
    private int countScans;
    private int trackRow;
    private int trackCol;
    private int isScanning = 0;
    private int returnedFromScanning = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        manager = manager.getInstance();
        options = options.getInstance();

        if (manager.getInitialGameFlag() == 0) {
            setPreferences();
        }

        manager.setInitialGameFlag();

        countScans = manager.getCountScans();
        countGemsFound = manager.getCountGemsFound();
        totalGems = manager.getGameGems();

        seeker = manager.getSeeker();
        isGem = manager.getIsGem();
        grid = manager.getGrid();
        clicked = manager.getClicked();

        gemsFound = (TextView) findViewById(R.id.gemsFound);
        scansUsed = (TextView) findViewById(R.id.scansUsed);

        gemsFound.setText(getString(R.string.found_gems, countGemsFound, totalGems));
        scansUsed.setText(getString(R.string.scans_used, countScans));

        populateGrid(manager);

        /** Re-display buttons clicked and gems found if user exits back to main menu
         * during game, and re-enters game. **/

        for (int row = 0; row < manager.getGameRow(); row++) {
            for (int col = 0; col < manager.getGameCol(); col++) {
                Button currButton = grid[row][col];
                if (clicked[row][col] != 0 && clicked[row][col] != 2) {
                    currButton.setText(Integer.toString(seeker[row][col]));
                }
                if (clicked[row][col] == 2|| clicked[row][col] == 3) {
                    setButtonBackground(currButton);
                }
            }
        }
    }

    private void populateGrid(GameLogicManager manager) {

        TableLayout table = (TableLayout) findViewById(R.id.gemGrid);

        for (int row = 0; row < manager.getGameRow(); row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            ));

            table.addView(tableRow);

            for (int col = 0; col < manager.getGameCol(); col++) {
                final Button dig = new Button(this);

                dig.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f
                ));

                grid[row][col] = dig;

                final int FINAL_ROW = row;
                final int FINAL_COL = col;

                tableRow.addView(dig);

                dig.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        digClicked(seeker, isGem, FINAL_ROW, FINAL_COL);

                        /** Do not change if button is clicked if function above returned
                         * from a failed scan launch (another scan is currently happening) **/

                        if (isGem[FINAL_ROW][FINAL_COL] != 1000 && clicked[FINAL_ROW][FINAL_COL] != 1
                            && returnedFromScanning != 1) {
                            clicked[FINAL_ROW][FINAL_COL] = 1;
                        }

                        returnedFromScanning = 0;
                    }
                });

            }
        }
    }

    /** This function helps to set the clicked value for each button.
     * 0 = not yet clicked
     * 1 = clicked on once, and is not a gem
     * 2 = clicked on once, and is a gem
     * 3 = clicked on twice, and is a gem (performed scan on gem)
     * These sets are performed by functions defined in GameLogic class
     *
     * Function also displays number of gems in row/col from scan, and
     * image of gem as they are found.
     *
     * When a gem is found, function foundGem() from GameLogicManager is called
     * which will update the number of gems left to be found for buttons in the
     * respective row/column.
     *
     * With implementation of scanning animation, only one scan can be performed
     * at a time. If a non successful dig is performed, that is, a gem is not found
     * at the location, and a scan is triggered, the function immediately returns.
     *
     * This is checked by the isScanning flag. **/

    private void digClicked(int[][] seeker, int[][] isGem, int currRow, int currCol) {
        if (clicked[currRow][currCol] == 1 || clicked[currRow][currCol] == 3) {
            return;
        }

        if (clicked[currRow][currCol] == 2) {
            manager.setClickedGem(currRow, currCol, 3);
        }

        Button button = grid[currRow][currCol];


        if (isGem[currRow][currCol] == 1000 && clicked[currRow][currCol] == 0) {
            manager.setClickedGem(currRow, currCol, 2);

            manager.incrementCountGemsFound();
            countGemsFound = manager.getCountGemsFound();

            gemsFound.setText(getString(R.string.found_gems, countGemsFound, totalGems));

            setButtonBackground(button);

            seeker[currRow][currCol] -= 1;

            manager.foundGem(currRow, currCol);

            for (int col = 0; col < manager.getGameCol(); col++) {
                Button currButton = grid[currRow][col];
                if (clicked[currRow][col] == 1 || clicked[currRow][col] == 3) {
                    currButton.setText(Integer.toString(seeker[currRow][col]));
                }
            }

            for (int row = 0; row < manager.getGameRow(); row++) {
                Button currButton = grid[row][currCol];
                if (clicked[row][currCol] == 1 || clicked[row][currCol] == 3) {
                    currButton.setText(Integer.toString(seeker[row][currCol]));
                }
            }

            if (countGemsFound == totalGems) {
                displayAll();
            }

            return;
        }

        /** Does not allow new scan to be performed when one is currently in session.
         * Otherwise conflicting animations alter the size of the buttons **/

        if (isScanning == 1) {
            Toast toast = Toast.makeText(this, R.string.scanning_msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL, 0, 0);
            toast.show();
            returnedFromScanning = 1;
            return;
        }

        /** Trigger row/col animation for scan **/

        if (clicked[currRow][currCol] == 0 || clicked[currRow][currCol] == 3) {
            animateScan(currRow, currCol);

            manager.incrementCountScans();
            countScans = manager.getCountScans();
            scansUsed.setText(getString(R.string.scans_used, countScans));
        }
    }

    /** Upon winning the game, all values (should be 0) are displayed for each
     * button on the board. Here, only buttons/gems that have not been scanned
     * need to be updated to display the text.
     *
     * Game logic state is reset.
     *
     * Message dialog displaying congratulatory message is displayed. **/

    private void displayAll() {
        for (int row = 0; row < manager.getGameRow(); row++) {
            for (int col = 0; col < manager.getGameCol(); col++) {
                Button currButton = grid[row][col];
                if (clicked[row][col] == 0) {
                    currButton.setText(Integer.toString(seeker[row][col]));
                }
                if (clicked[row][col] == 2) {
                    currButton.setText(Integer.toString(seeker[row][col]));
                }
            }
        }

        manager.resetState();

        FragmentManager fragManager = getSupportFragmentManager();
        MsgFragment dialog = new MsgFragment();
        dialog.show(fragManager, getString(R.string.msg_dialog));
    }

    /** Function for displaying gem image when gem is successfully found. **/

    private void setButtonBackground(Button button) {
        Bitmap origBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gemstone);
        int newHeight = button.getHeight() - 50;
        int newWidth = button.getWidth() - 50;

        if (manager.getGameRow() == 4 && manager.getGameCol() == 6) {
            newHeight -= 50;
            newWidth -= 50;
        }

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(origBitmap, newWidth, newHeight, true);
        Resources resource = getResources();
        button.setBackground(new BitmapDrawable(resource, scaledBitmap));
    }

    /** Function creates game using past user configurations (ie. in previous launches).
     * Used for when first game is started after app is launched. **/

    private void setPreferences() {
        SharedPreferences boardSizeSharedPref = getSharedPreferences(getString(R.string.game_board_prefs), MODE_PRIVATE);
        int selBoardSizePos = boardSizeSharedPref.getInt(getString(R.string.board_size_choice), -1);

        switch (selBoardSizePos) {
            case -1:
                break;
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

        SharedPreferences numGemsSharedPref = getSharedPreferences(getString(R.string.game_gem_prefs), MODE_PRIVATE);
        int selNumGemsPos = numGemsSharedPref.getInt(getString(R.string.num_gems_choice), -1);

        switch (selNumGemsPos) {
            case -1:
                break;
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

    /** Animation for when scan is performed. Works across row, then column.
     * When animation starts, isScanning flag is set to 1, so no concurrent
     * scan can occur.
     *
     * isScanning flag is set back to 0 when the last button in the column
     * is being animated, signalling to program than next scan can start. **/

    private void animateScan(int currRow,  int currCol) {
        final int FINAL_ROW = currRow;
        final int FINAL_COL = currCol;
        final int DELAY = 100;
        final Handler delayAnimHandler = new Handler();

        isScanning = 1;
        trackRow = 0;
        trackCol = 0;

        final Runnable colRunnable = new Runnable() {
            @Override
            public void run() {
                final Button currButton = grid[trackRow][FINAL_COL];

                if (trackRow == manager.getGameRow() - 1) {
                    shrinkExpandAnim(currButton);
                    Button clickedButton = grid[FINAL_ROW][FINAL_COL];

                    /** Display results of scanning (number of gems to be found in row/col
                     * after we begin scan of last button in col. **/

                    clickedButton.setText(Integer.toString(seeker[FINAL_ROW][FINAL_COL]));
                    clickedButton.setPadding(0, 0, 0, 0);

                    /** Tell program that scan has ended. **/

                    isScanning = 0;
                    return;
                }

                shrinkExpandAnim(currButton);
                delayAnimHandler.postDelayed(this, DELAY);

                trackRow += 1;
            }
        };

        Runnable rowRunnable = new Runnable() {
            @Override
            public void run() {
                final Button currButton = grid[FINAL_ROW][trackCol];

                if (trackCol == manager.getGameCol() - 1) {
                    shrinkExpandAnim(currButton);

                    /** Begin column animation when last button in row
                     * begins animation. **/

                    delayAnimHandler.postDelayed(colRunnable, DELAY + 150);
                    return;
                }

                shrinkExpandAnim(currButton);
                delayAnimHandler.postDelayed(this, DELAY);

                trackCol += 1;
            }
        };

        delayAnimHandler.postDelayed(rowRunnable, DELAY);
    }

    /** Animation that shrinks and expands button. **/

    public void shrinkExpandAnim(Button button) {
        int DURATION = 100;
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(button,
                PropertyValuesHolder.ofFloat(getString(R.string.scaleX), 0.90f),
                PropertyValuesHolder.ofFloat(getString(R.string.scaleY), 0.90f)
        );

        scaleDown.setDuration(DURATION);
        scaleDown.setRepeatMode(ValueAnimator.REVERSE);
        scaleDown.setRepeatCount(1);
        scaleDown.start();
    }
}
