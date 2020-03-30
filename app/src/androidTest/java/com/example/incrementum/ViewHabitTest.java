package com.example.incrementum;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.client.model.Filters;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(AndroidJUnit4.class)
public class ViewHabitTest {

    @Rule
    public ActivityTestRule<ViewHabitActivity> activityTestRule = new ActivityTestRule(ViewHabitActivity.class);

    private ViewHabitActivity activity;


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
            public void GetHabitsValid()
    {
        String userID = "5e80f4ae1c9d440000932975";
        ArrayList<String> habits = new ArrayList<>();
        final RemoteMongoCollection<Document> coll =
                DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Habits");
        RemoteFindIterable<Document> results;
        results = coll.find(Filters.eq("userId", userID))
                .projection(
                        new Document());
        results.forEach(item ->{
            try{
                JSONObject obj = new JSONObject(item.toJson());
                String habit = obj.getString("name");
                String _id = obj.getJSONObject("_id").getString("$oid");
                habits.add(habit);
                assert(habits.size()>0);
            }
            catch(JSONException e){
                Log.d("JSON exception:",e.toString());
            }
        });


    }


    @Test
    public void GetHabitsNotValid()
    {
        String userID = "This is not a correct ID";
        ArrayList<String> habits = new ArrayList<>();
        final RemoteMongoCollection<Document> coll =
                DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Habits");
        RemoteFindIterable<Document> results;
        results = coll.find(Filters.eq("userId", userID))
                .projection(
                        new Document());
        results.forEach(item ->{
            try{
                JSONObject obj = new JSONObject(item.toJson());
                String habit = obj.getString("name");
                String _id = obj.getJSONObject("_id").getString("$oid");
                habits.add(habit);
                assert(habits.size()==0);
            }
            catch(JSONException e){
                Log.d("JSON exception:",e.toString());
            }
        });

    }
}
