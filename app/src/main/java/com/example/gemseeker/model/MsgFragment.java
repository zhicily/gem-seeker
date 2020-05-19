package com.example.gemseeker.model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.gemseeker.R;

/** This class is for setting up the message dialog pop-up box
 * upon user completion of the game (all gems on board are found).
 *
 * The pop-up will display an OK and CANCEL button.
 * OK terminates current game activity, and sends user back to main menu. **/

public class MsgFragment extends AppCompatDialogFragment {

    private GameLogicManager manager;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        manager = manager.getInstance();

        View view;
        view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_msg_popup, null);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        getActivity().finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, listener)
                .create();

    }
}
