package com.example.incrementum;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ToggleButton;

import androidx.test.annotation.UiThreadTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.Task;
import com.mongodb.Block;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

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
  private Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
  private AddJournalActivity activity;
  private String user_id = "FAKEUSERID";
  private String habit_id = "FAKEHABITID";

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

  /**
   * @given a valid user id and a valid habit id
   * @when adding a journal entry
   * @then journal entry is added successfully
   */
  @Test
  public void addEntry_validUserIDAndHabitId_addsJournal() throws InterruptedException {

    List<String> journalsList = new ArrayList<>();

    // Get all journals
    DatabaseHelper.getAllJournals(journalsList, new HashMap<>(), user_id, habit_id);
    // wait for database call to be completed
    Thread.sleep(2500);
    // Verify original size
    int size = journalsList.size();

    // Set test conditions
    activity.setEntry("new test entry");
    activity.setUserId(user_id);
    activity.setHabitId(habit_id);
    // Call function
    activity.addEntry();

    // Clear list to avoid counting repeated journals
    journalsList.clear();
    // Get all journals again
    DatabaseHelper.getAllJournals(journalsList, new HashMap<>(), user_id, habit_id);
    // wait for query to be completed
    Thread.sleep(2500);

    // current size should be original size + 1
    assertEquals(size + 1, journalsList.size());
  }

  /**
   * @given an invalid user id and a valid habit id
   * @when adding a journal entry
   * @then journal entry is not added successfully
   */
  @Test
  public void addEntry_invalidUserID_doesNotAddJournal() throws InterruptedException {

    List<String> journalsList = new ArrayList<>();

    String invalidUserId = "";

    // Get all journals
    DatabaseHelper.getAllJournals(journalsList, new HashMap<>(), user_id, habit_id);
    // wait for database call to be completed
    Thread.sleep(2500);
    // Verify original size
    int size = journalsList.size();

    // Set test conditions
    activity.setEntry("new test entry");
    activity.setUserId(invalidUserId);
    activity.setHabitId(habit_id);
    // Call function
    activity.addEntry();

    // Clear list to avoid counting repeated journals
    journalsList.clear();
    // Get all journals again
    DatabaseHelper.getAllJournals(journalsList, new HashMap<>(), user_id, habit_id);
    // wait for query to be completed
    Thread.sleep(2500);

    // current size should be original size since insert failed
    assertEquals(size, journalsList.size());
  }

  /**
   * @given a valid user id and an invalid habit id
   * @when adding a journal entry
   * @then journal entry is not added successfully
   */
  @Test
  public void addEntry_invalidHabitID_doesNotAddJournal() throws InterruptedException {

    List<String> journalsList = new ArrayList<>();

    String invalidHabitId = "";

    // Get all journals
    DatabaseHelper.getAllJournals(journalsList, new HashMap<>(), user_id, habit_id);
    // wait for database call to be completed
    Thread.sleep(2500);
    // Verify original size
    int size = journalsList.size();

    // Set test conditions
    activity.setEntry("new test entry");
    activity.setUserId(user_id);
    activity.setHabitId(invalidHabitId);
    // Call function
    activity.addEntry();

    // Clear list to avoid counting repeated journals
    journalsList.clear();
    // Get all journals again
    DatabaseHelper.getAllJournals(journalsList, new HashMap<>(), user_id, habit_id);
    // wait for query to be completed
    Thread.sleep(2500);

    // current size should be original size since insert failed
    assertEquals(size, journalsList.size());
  }

  /**
   * @given a valid user id and an valid habit id and newTriggerCheck is true
   * @when adding a journal entry
   * @then addHabit is called
   */
  @Test
  public void addEntry_newTriggerCheckIsTrue_addHabitIsCalled() throws InterruptedException {

    List<String> triggersList, timesList;
    triggersList = new ArrayList<>();
    timesList = new ArrayList<>();

    // Set triggerCheck to True
    activity.setNewTriggerCheck(true);
    // Set Emotional State and Morning to true
    activity.setTriggers(true);
    activity.setUserId(user_id);
    activity.setHabitId("5e8156271c9d440000185f47");
    // Call function
    activity.addEntry();

    getHabits(triggersList, timesList);

    Thread.sleep(2500);

    assertEquals("[[Emotional State]]", triggersList.toString());
    assertEquals("[[Morning]]", timesList.toString());
  }

  /**
   * @given a valid user id and an valid habit id and newTriggerCheck is false
   * @when adding a journal entry
   * @then addHabit is not called
   */
  @Test
  public void addEntry_newTriggerCheckIsFalse_addHabitIsCalled() throws InterruptedException {

    List<String> triggersList, timesList;
    triggersList = new ArrayList<>();
    timesList = new ArrayList<>();

    // Get Habits before adding the triggers/times
    getHabits(triggersList, timesList);
    // Wait for database to query
    Thread.sleep(2500);

    // Ensure triggers/times is already there
    assertEquals("[[Emotional State]]", triggersList.toString());
    assertEquals("[[Morning]]", timesList.toString());

    // Set triggerCheck to false
    activity.setNewTriggerCheck(false);
    // Set Emotional State and Morning to false
    activity.setTriggers(false);
    activity.setUserId(user_id);
    activity.setHabitId("5e8156271c9d440000185f47");
    // Call function
    activity.addEntry();

    // Clear lists
    triggersList.clear();
    timesList.clear();
    // Query database again
    getHabits(triggersList, timesList);
    // Wait for database to query
    Thread.sleep(2500);

    // Test that list is the same as before
    assertEquals("[[Emotional State]]", triggersList.toString());
    assertEquals("[[Morning]]", timesList.toString());
  }

  // THIS FUNCTION FAILS TO QUERY DATABASE SOMETIMES WHICH CAUSES THE TESTS TO FAIL
  // IF THIS HAPPENS, RERUN THE TESTS AND THEY WILL PASS
  private void getHabits(List<String> triggersList, List<String> timesList){
    final RemoteMongoCollection<Document> collection =
      DatabaseHelper.mongoClient.getDatabase("Incrementum").getCollection("Habits");

    // Only get journal entries from current user who is logged in
    Document filterDoc = new Document()
      .append("userId", user_id)
      .append("_id", new ObjectId("5e8156271c9d440000185f47"));

    // Get all entries with the criteria from filterDoc
    RemoteFindIterable results = collection.find(filterDoc);

    results.forEach(item -> {
        Document doc = (Document) item;

        triggersList.add(doc.get("Triggers").toString());
        timesList.add(doc.get("Times").toString());
      }
    );
  }
}
