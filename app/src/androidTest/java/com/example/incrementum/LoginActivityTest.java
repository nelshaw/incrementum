package com.example.incrementum;

import android.content.Context;
import android.util.Log;

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

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static com.example.incrementum.DatabaseHelper.mongoClient;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

  // Declare rule to be used to access activity
  @Rule
  public ActivityTestRule<LoginActivity> mActivityRule =
          new ActivityTestRule(LoginActivity.class);

  private LoginActivity activity;

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
  public void initialize() {
    // Initialize activity
    activity = mActivityRule.getActivity();
  }

  /**
   * Sample Test
   */
  @Test
  public void useAppContext() {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    assertEquals("com.example.incrementum", appContext.getPackageName());
  }

  /**
   * @given a positive quotes list
   * @when requesting to get all positive quotes
   * @then return 5 positive quotes
   */
  @Test
  public void validLoginTest() {
    String email = "hi@hi.hi";
    String password = "hihihi";

    UserPasswordCredential credential = new UserPasswordCredential(email, password);
    Stitch.getDefaultAppClient().getAuth().loginWithCredential(credential)
            .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                                     @Override
                                     public void onComplete(@NonNull final Task<StitchUser> task) {
                                       assert (task.isSuccessful());
                                     }
                                   }
            );
  }

  public void invalidLoginTest() {
    String email = "lemmetellusomething";
    String password = "GITdrnewton";

    UserPasswordCredential credential = new UserPasswordCredential(email, password);
    Stitch.getDefaultAppClient().getAuth().loginWithCredential(credential)
            .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                                     @Override
                                     public void onComplete(@NonNull final Task<StitchUser> task) {
                                       assert (!task.isSuccessful());
                                     }
                                   }
            );
  }
}