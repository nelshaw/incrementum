package com.example.incrementum;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatDialogFragment;

public class QuotesDialog extends AppCompatDialogFragment {

  public enum Type{
    NEGATIVE,
    POSITIVE,
    CALENDAR
  }

  private String quote;
  public Type type;
    //HabitEffectActivity habit;

    QuotesDialog(Type type, String quote){
        this.quote = quote;
        this.type = type;
    }

    // open journal
    public void openAddJournalActivity(){
        Intent intent = new Intent(getActivity(), AddJournalActivity.class);
        startActivity(intent);
    }

    // open habits
    public void openHabitActivity(){
        Intent intent = new Intent(getActivity(), ViewHabitActivity.class);
        startActivity(intent);
    }

  // open calendar
  public void openCalendarActivity() {
    Intent intent = new Intent(getActivity(), CalendarActivity.class);
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
                      switch(type){
                        case NEGATIVE:
                          openAddJournalActivity();
                          break;
                        case POSITIVE:
                          openHabitActivity();
                          break;
                        case CALENDAR:
                          Log.d("DIALOG", "calendar opened");
                          break;
                      }
                    }
                });

        return builder.create();
    }
}
