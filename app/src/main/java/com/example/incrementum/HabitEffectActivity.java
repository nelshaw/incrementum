package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;


public class HabitEffectActivity extends AppCompatActivity {

    Button nButton;
    Button pButton;
    Button dialogButton;
    String[] quotes;

    public enum Type{
        NEGATIVE,
        POSITIVE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_effect);

        // find button by id
        nButton = findViewById(R.id.negative_button);
        pButton = findViewById(R.id.positive_button);
        dialogButton = findViewById(R.id.btn_show);

        // on click function negative button
        nButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getNegativeQuotes();
                String quote = "neg quote";
                Type type = Type.NEGATIVE;
                openDialog(type, quote);
                //openJournalActivity();
            }
        });

        // on click function positive button
        pButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String quote = "pos quote";
                Type type = Type.POSITIVE;
                openDialog(type, quote);
                //openHabitActivity();
            }
        });

        dialogButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //openDialog();
            }
        });

    }

    // open journal
    public void openJournalActivity(){
        Intent intent = new Intent(this, AddJournalActivity.class);
        startActivity(intent);
    }

    // open habit
    public void openHabitActivity(){
        Intent intent = new Intent(this, ViewHabitActivity.class);
        startActivity(intent);
    }

    public void openDialog(Type type, String quote){
        QuotesDialog dialog = new QuotesDialog(type, quote);
        dialog.show(getSupportFragmentManager(), "exampleDialog");
        dialog.setCancelable(false);
//        final Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            public void run() {
//                dialog.dismiss();
//                t.cancel();
//                if(type == Type.NEGATIVE)
//                    openJournalActivity();
//                else
//                    openHabitActivity();
//            }
//        }, 5000);
    }

    public void getNegativeQuotes(){
        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Quotes");

        Document filterDoc = new Document();
//      .append("entry", new Document().append("$eq", true));

        RemoteFindIterable results = coll.find(filterDoc);

        Log.d("sjnd-------------", "start");

        results.forEach(item -> {
            Log.d("---------ITEM------", item.toString());

        });

        Log.d("sjnd-------------", "end");

    }

}



