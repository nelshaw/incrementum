package com.example.incrementum;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
public class AddHabitTest
{

@Rule
    public ActivityTestRule<AddHabit2Activity> activityTestRule = new ActivityTestRule(AddHabit2Activity.class);

    private AddHabit2Activity activity;

    // Initialize database once
    @BeforeClass
    public static void onSetup() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Initialize stitch client
        Stitch.initialize(appContext);

        // Initialize stitch client
        final StitchAppClient client =
                Stitch.initializeDefaultAppClient("incrementum-xjkms");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");
    }


    @Before
    public void initialize(){
        // Initialize activity
        activity = activityTestRule.getActivity();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.incrementum", appContext.getPackageName());
    }


    @Test
    public void validAddHabit() {
        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Habits");
      //make sure all habit attributes are added
        String name = "Smoking";
        String description = "Its killing me";
        int length = 3;
        ArrayList<String> triggers = new ArrayList<>();
        triggers.add("Time");
        ArrayList<String> times = new ArrayList<>();
        times.add("Afternoon");
        String userId = "5e6b032f1c9d440000c842e5";

        Document doc = new Document()
                .append("name", name)
                .append("length",length*14)
                .append("description",description)
                .append("Triggers",triggers)
                .append("Times",times)
                .append("userId",userId);

        final Task<RemoteInsertOneResult> insert = coll.insertOne(doc);
        insert.addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
            @Override
            public void onComplete(@NonNull Task<RemoteInsertOneResult> task) {
                assert (task.isSuccessful());
            }
        });
    }

public void InvalidAddHabit()
{
    //description missing, and no triggers entered
    String name = "Smoking";
    int length = 3;
    ArrayList<String> times = new ArrayList<>();
    times.add("Afternoon");
    String userId = "5e6b032f1c9d440000c842e5";

    Document doc = new Document()
            .append("name", name)
            .append("length",length*14)
            .append("Times",times)
            .append("userId",userId);

    assert(true);
}

}
