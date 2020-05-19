package com.example.gemseeker.model;

import android.widget.Button;

import java.util.Random;

/** This class manages the logic behind the game.
 *
 * Keeps track of which positions on the board are gems/have been clicked/count of gems
 * in the respective row/col, as well as tracks the moves (# scans) and number of gems
 * found by the user during game play.
 *
 * Whenever game board settings are changed, or number of gems to be found are changed
 * in user settings, the manager updates these settings in the object, and resets the
 * current baord's state accordingly.
 *
 * These options data are stored in the Singleton class below. **/

public class GameLogicManager {
    private static GameLogicManager INSTANCE = null;
    private int gameRow;
    private int gameCol;
    private int gameGems;
    private int[][] seeker;
    private int[][] isGem;
    private Button[][] grid;
    private int[][] clicked;

    private int countGemsFound;
    private int countScans;

    private int initialGameFlag;

    private GameLogicManager() {
        gameRow = 4;
        gameCol = 6;
        gameGems = 6;

        seeker = new int[gameRow][gameCol];
        isGem = new int[gameRow][gameCol];

        grid = new Button[gameRow][gameCol];
        clicked = new int[gameRow][gameCol];

        for (int row = 0; row < gameRow; row++) {
            for (int col = 0; col < gameCol; col++) {
                seeker[row][col] = 0;
                isGem[row][col] = 0;
                clicked[row][col] = 0;
            }
        }

        generateGems();

        countGemsFound = 0;
        countScans = 0;
        initialGameFlag = 0;
    }

    public static GameLogicManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameLogicManager();
        }

        return INSTANCE;
    }

    public int getInitialGameFlag() {
        return initialGameFlag;
    }

    public void setInitialGameFlag() {
        initialGameFlag = 1;
    }

    public void setGameRowCol(int row, int col) {
        gameRow = row;
        gameCol = col;

        resetState();
    }

    public int getGameRow() {
        return gameRow;
    }


    public int getGameCol() {
        return gameCol;
    }

    public void setGameGems(int i) {
        gameGems = i;

        resetState();
    }

    public int getGameGems() {
        return gameGems;
    }

    public int getCountGemsFound() {
        return countGemsFound;
    }

    public void incrementCountGemsFound() {
        countGemsFound += 1;
    }

    public int getCountScans() {
        return countScans;
    }

    public void incrementCountScans() {
        countScans += 1;
    }

    public Button[][] getGrid() {
        return grid;
    }

    public int[][] getClicked() {
        return clicked;
    }

    public int[][] getSeeker() {
        return seeker;
    }

    public int[][] getIsGem() {
        return isGem;
    }

    public void generateGems() {
        int numDigs = gameCol * gameRow;

        for (int i = 0; i < gameGems; i++) {
            Random rand = new Random();

            int currRand = rand.nextInt(numDigs);
            int x = currRand / gameCol;
            int y = currRand % gameCol;

            while (isGem[x][y] == 1000) {
                currRand = rand.nextInt(numDigs);
                x = currRand / gameCol;
                y = currRand % gameCol;
            }

            isGem[x][y] = 1000;

            seeker[x][y] += 1;

            /** increment everything in row by 1 **/

            for (int col = 0; col < gameCol; col++) {
                if (col != y) {
                    seeker[x][col] += 1;
                }
            }

            /** increment everything in col by 1 **/

            for (int row = 0; row < gameRow; row++) {
                if (row != x) {
                    seeker[row][y] += 1;
                }
            }
        }
    }

    public void foundGem(int currRow, int currCol) {
        for (int col = 0; col < gameCol; col++) {
            if (col != currCol) {
                seeker[currRow][col] -= 1;
            }
        }

        for (int row = 0; row < gameRow; row++) {
            if (row != currRow) {
                seeker[row][currCol] -= 1;
            }
        }
    }

    public void setClickedGem(int currRow, int currCol, int i) {
        clicked[currRow][currCol] = i;
    }

    public void resetState() {
        seeker = new int[gameRow][gameCol];
        isGem = new int[gameRow][gameCol];

        grid = new Button[gameRow][gameCol];
        clicked = new int[gameRow][gameCol];

        for (int row = 0; row < gameRow; row++) {
            for (int col = 0; col < gameCol; col++) {
                seeker[row][col] = 0;
                isGem[row][col] = 0;
                clicked[row][col] = 0;
            }
        }

        countGemsFound = 0;
        countScans = 0;

        generateGems();
    }
}
