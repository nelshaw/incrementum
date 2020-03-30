package com.example.incrementum;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ToggleButton;

import androidx.test.annotation.UiThreadTest;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AddJournalActivityTest {

  // Declare rule to be used to access activity
  @Rule
  public ActivityTestRule<AddJournalActivity> mActivityRule =
    new ActivityTestRule(AddJournalActivity.class);

  // Context of the app under test.
  Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
  private AddJournalActivity activity;

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
    assertEquals("com.example.incrementum", appContext.getPackageName());
  }

  /**
   * @given an empty entry
   * @when checking if entry is empty
   * @then value returns true
   */
  @Test
  public void isEntryEmpty_emptyEntry_returnsTrue() {

    String entry = "";

    boolean isEntryEmpty = activity.isEntryEmpty(entry);

    assertTrue(isEntryEmpty);
  }

  /**
   * @given a valid (non-empty) entry
   * @when checking if entry is empty
   * @then value returns false
   */
  @Test
  public void isEntryEmpty_validEntry_returnsFalse() {

    String entry = "example entry";

    boolean isEntryEmpty = activity.isEntryEmpty(entry);

    assertFalse(isEntryEmpty);
  }

  /**
   * @given a null entry
   * @when checking if entry is empty
   * @then function throws NullPointerException
   */
  @Test (expected = NullPointerException.class)
  public void isEntryEmpty_nullEntry_throwsException() {

    boolean isEntryEmpty = activity.isEntryEmpty(null);
  }

}
