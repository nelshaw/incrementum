package com.example.incrementum;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

public class QuotesDialog extends AppCompatDialogFragment {

    private String quote;
    HabitEffectActivity.Type type;
    //HabitEffectActivity habit;

    QuotesDialog(HabitEffectActivity.Type type, String quote){
        this.quote = quote;
        this.type = type;
    }

    // open journal
    public void openJournalActivity(){
        Intent intent = new Intent(getActivity(), AddJournalActivity.class);
        startActivity(intent);
    }

    // open habits
    public void openHabitActivity(){
        //Intent intent = new Intent(getActivity(), CalendarActivity.class);
        Intent intent = new Intent(getActivity(), ViewHabitActivity.class);
        startActivity(intent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //habit = new HabitEffectActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(this.quote)
//                .setOnDismissListener(new DialogInterface.OnDismissListener(){
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//
//                    }
//                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(type == HabitEffectActivity.Type.NEGATIVE)
                            openJournalActivity();
                        else
                            openHabitActivity();
                    }
                });

        return builder.create();
    }
}
