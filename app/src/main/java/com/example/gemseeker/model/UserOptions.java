package com.example.gemseeker.model;

import java.util.ArrayList;
import java.util.List;

/** This class manages the list of possible configurations that the user
 * can select to alter the game board on which they are playing on. **/

public class UserOptions {
    private static UserOptions INSTANCE = null;
    private ArrayList<String> boardSizes;
    private ArrayList<String> numGems;

    private UserOptions() {
        boardSizes = new ArrayList<>();
        numGems = new ArrayList<>();

        boardSizes.add("4 x 6");
        boardSizes.add("5 x 10");
        boardSizes.add("6 x 15");

        numGems.add("6 Gems");
        numGems.add("10 Gems");
        numGems.add("15 Gems");
        numGems.add("20 Gems");
    }

    public static UserOptions getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserOptions();
        }

        return INSTANCE;
    }

    public List<String> getBoardSizes() {
        return boardSizes;
    }

    public List<String> getNumGems() {
        return numGems;
    }
}
