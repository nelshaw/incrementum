package com.example.incrementum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.incrementum.QuotesDialog.*;


public class HabitEffectActivity extends AppCompatActivity {

    Button nButton;
    Button pButton;
    List<String> negQuotes;
    List<String> posQuotes;

    final RemoteMongoCollection<Document> coll =
            DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Quotes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_effect);

        // find button by id
        nButton = findViewById(R.id.negative_button);
        pButton = findViewById(R.id.positive_button);
        negQuotes = new ArrayList<>();
        posQuotes = new ArrayList<>();

        getNegativeQuotes(negQuotes);
        getPositiveQuotes(posQuotes);

        // on click function negative button
        nButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Type type = Type.NEGATIVE;

                Random random = new Random();
                int rand = random.nextInt(negQuotes.size());
                for (String s : negQuotes) {
                  Log.d("hed", s);
                }

                openDialog(type, negQuotes.get(rand));
            }
        });

        // on click function positive button
        pButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Type type = Type.POSITIVE;

                Random random = new Random();
                int rand = random.nextInt(negQuotes.size());

                for (String s : posQuotes) {
                  Log.d("hed", s);
                }
                openDialog(type, posQuotes.get(rand));
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
    }

    public void getNegativeQuotes(List<String> negQuotes){

        Document filterDoc = new Document()
          .append("Type", "Negative");

        RemoteFindIterable results = coll.find(filterDoc)
          .projection(
            new Document()
            .append("Quote", 1)
          .append("_id", 0));

        results.forEach(item -> {

          String s = item.toString();
          String substring = s.substring(16, s.length() - 2);

          negQuotes.add(substring);

          Log.d("---------ITEM------", substring);
        });

    }

  public void getPositiveQuotes(List<String> posQuotes){

    Document filterDoc = new Document()
      .append("Type", "Positive");

    RemoteFindIterable results = coll.find(filterDoc)
      .projection(
        new Document()
          .append("Quote", 1)
          .append("_id", 0));

    results.forEach(item -> {

      String s = item.toString();
      String substring = s.substring(16, s.length() - 2);

      posQuotes.add(substring);

      Log.d("---------ITEM------", substring);
    });

  }


}



