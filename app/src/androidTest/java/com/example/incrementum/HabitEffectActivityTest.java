package com.example.incrementum;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;

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
public class HabitEffectActivityTest {

  // Declare rule to be used to access activity
  @Rule
  public ActivityTestRule<HabitEffectActivity> mActivityRule =
    new ActivityTestRule(HabitEffectActivity.class);

  private HabitEffectActivity activity;

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
  public void getPositiveQuotes_validList_returnFiveQuotes() throws InterruptedException {

    List<String> posQuotes = new ArrayList<>();

    // Test that posQuotes is empty
    assertEquals(0, posQuotes.size());

    activity.getPositiveQuotes(posQuotes);

    // wait for database call to be completed
    Thread.sleep(2500);

    // Test that posQuotes is not empty and has at least one entry
    assertEquals(5, posQuotes.size());
  }

  /**
   * @given a negative quotes list
   * @when requesting to get all negative quotes
   * @then return 5 negative quotes
   */
  @Test
  public void getNegativeQuotes_validList_returnFiveQuotes() throws InterruptedException {

    List<String> negQuotes = new ArrayList<>();

    // Test that posQuotes is empty
    assertEquals(0, negQuotes.size());

    activity.getNegativeQuotes(negQuotes);

    // wait for database call to be completed
    Thread.sleep(2500);

    // Test that posQuotes is not empty and has at least one entry
    assertEquals(5, negQuotes.size());
  }

  @Test
  public void getPositiveQuotes_nullList_doesNotReturnFiveQuotes() throws InterruptedException {
    List<String> posQuotes = null;

    activity.getPositiveQuotes(posQuotes);

    // wait for database call to be completed
    Thread.sleep(2500);

    // Test that posQuotes is null
    assertNull(posQuotes);
  }

  @Test
  public void getNegativeQuotes_nullList_doesNotReturnFiveQuotes() throws InterruptedException {
    List<String> negQuotes = null;

    activity.getPositiveQuotes(negQuotes);

    // wait for database call to be completed
    Thread.sleep(2500);

    // Test that posQuotes is null
    assertNull(negQuotes);
  }

}
