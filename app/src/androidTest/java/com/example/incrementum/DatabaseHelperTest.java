package com.example.incrementum;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseHelperTest {

  private String user_id = "5e6b032f1c9d440000c842e5";
  private String habit_id = "5e6861323620b2a02aa265fb";

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
   * @given a valid user id and a valid habit id
   * @when requesting all journals
   * @then journals list is not empty and contains at least one journal
   */
  @Test
  public void getAllJournals_validUserIdAndValidHabitId_returnsJournals() throws InterruptedException {

    List<String> journalsList = new ArrayList<>();
    HashMap<Integer, Date> entriesInformation = new HashMap<>();

    // Test that journalsList is initially empty
    assertEquals(0, journalsList.size());

    DatabaseHelper.getAllJournals(journalsList, entriesInformation, user_id, habit_id);

    // wait for database call to be completed
    Thread.sleep(2500);

    // Test that journalsList is not empty and has at least one entry
    assertTrue(journalsList.size() > 0);
  }

  /**
   * @given an invalid user id and a valid habit id
   * @when requesting all journals
   * @then journals list is empty and returns zero journals
   */
  @Test
  public void getAllJournals_invalidUserId_doesNotReturnJournals() throws InterruptedException {

    List<String> journalsList = new ArrayList<>();
    HashMap<Integer, Date> entriesInformation = new HashMap<>();
    String invalidUserId = "1234567890";

    // Test that journalsList is initially empty
    assertEquals(0, journalsList.size());

    DatabaseHelper.getAllJournals(journalsList, entriesInformation, invalidUserId, habit_id);

    // wait for database call to be completed
    Thread.sleep(2500);

    // Test that journalsList is empty and has 0 entries
    assertEquals(0, journalsList.size());
  }

  /**
   * @given a valid user id and an invalid habit id
   * @when requesting all journals
   * @then journals list is empty and returns zero journals
   */
  @Test
  public void getAllJournals_invalidHabitId_doesNotReturnJournals() throws InterruptedException {

    List<String> journalsList = new ArrayList<>();
    HashMap<Integer, Date> entriesInformation = new HashMap<>();
    String invalidHabitId = "123456789";

    // Test that journalsList is initially empty
    assertEquals(0, journalsList.size());

    DatabaseHelper.getAllJournals(journalsList, entriesInformation, user_id, invalidHabitId);

    // wait for database call to be completed
    Thread.sleep(2500);

    // Test that journalsList is empty and has 0 entries
    assertEquals(0, journalsList.size());
  }

  /**
   * @given an empty user id and a valid habit id
   * @when requesting all journals
   * @then journals list is empty and returns zero journals
   */
  @Test
  public void getAllJournals_emptyUserId_doesNotReturnJournals() throws InterruptedException {

    List<String> journalsList = new ArrayList<>();
    HashMap<Integer, Date> entriesInformation = new HashMap<>();

    // Test that journalsList is initially empty
    assertEquals(0, journalsList.size());

    DatabaseHelper.getAllJournals(journalsList, entriesInformation, "", habit_id);

    // wait for database call to be completed
    Thread.sleep(2500);

    // Test that journalsList is empty and has 0 entries
    assertEquals(0, journalsList.size());
  }

  /**
   * @given a valid user id and an empty habit id
   * @when requesting all journals
   * @then journals list is empty and returns zero journals
   */
  @Test
  public void getAllJournals_emptyHabitId_doesNotReturnJournals() throws InterruptedException {

    List<String> journalsList = new ArrayList<>();
    HashMap<Integer, Date> entriesInformation = new HashMap<>();

    // Test that journalsList is initially empty
    assertEquals(0, journalsList.size());

    DatabaseHelper.getAllJournals(journalsList, entriesInformation, user_id, "");

    // wait for database call to be completed
    Thread.sleep(2500);

    // Test that journalsList is empty and has 0 entries
    assertEquals(0, journalsList.size());
  }

  /**
   * @given a null user id and a valid habit id
   * @when requesting all journals
   * @then journals list is empty and returns zero journals
   */
  @Test
  public void getAllJournals_nullUserId_doesNotReturnJournals() throws InterruptedException {

    List<String> journalsList = new ArrayList<>();
    HashMap<Integer, Date> entriesInformation = new HashMap<>();

    // Test that journalsList is initially empty
    assertEquals(0, journalsList.size());

    DatabaseHelper.getAllJournals(journalsList, entriesInformation, null, habit_id);

    // wait for database call to be completed
    Thread.sleep(2500);

    // Test that journalsList is empty and has 0 entries
    assertEquals(0, journalsList.size());
  }

  /**
   * @given a valid user id and a null habit id
   * @when requesting all journals
   * @then journals list is empty and returns zero journals
   */
  @Test
  public void getAllJournals_nullHabitId_doesNotReturnJournals() throws InterruptedException {

    List<String> journalsList = new ArrayList<>();
    HashMap<Integer, Date> entriesInformation = new HashMap<>();

    // Test that journalsList is initially empty
    assertEquals(0, journalsList.size());

    DatabaseHelper.getAllJournals(journalsList, entriesInformation, user_id, null);

    // wait for database call to be completed
    Thread.sleep(2500);

    // Test that journalsList is empty and has 0 entries
    assertEquals(0, journalsList.size());
  }

  /**
   * @given a valid entries HashMap and a valid position
   * @when updating a journal
   * @then new journal is updated
   */
  @Test
  public void updateJournal_validInformation_updatesJournal() throws InterruptedException {

    HashMap<Integer, Date> entriesInformation = new HashMap<>();
    int position = 1;
    String updatedText = "updated entry";
    String originalText = "original entry";

    // Insert entry
    // Update entry by calling function
    // Check updated entry is the same

  }

}
