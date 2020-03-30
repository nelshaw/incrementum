package com.example.incrementum;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CalendarActivityTest {

    private String user_id = "5e80f4ff3b6a9aa093cddfb4";
    private String habit_id = "5e80f5423b6a9aa093cddfb5";

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
     * @when requesting calendar
     * @then days lists are not empty and contain at least one entry
     */
    @Test
    public void getCalendar_validUserIdAndValidHabitId_returnsCalendar() throws InterruptedException {

        HashSet<CalendarDay> didDoHabitDates = new HashSet<>();
        HashSet<CalendarDay> didNotDoHabitDates = new HashSet<>();

        // Test that lists are initially empty
        assertEquals(0, didDoHabitDates.size());
        assertEquals(0, didNotDoHabitDates.size());

        CalendarActivity.getAllCalendarEntries(user_id, habit_id, didDoHabitDates, didNotDoHabitDates);

        // wait for database call to be completed
        Thread.sleep(2500);

        // Test that journalsList is not empty and has at least one entry
        assertTrue((didDoHabitDates.size() > 0) || (didNotDoHabitDates.size() > 0));
    }

    /**
     * @given an invalid user id and a valid habit id
     * @when requesting calendar
     * @then days lists are empty and returns zero entries
     */
    @Test
    public void getCalendar_invalidUserId_doesNotReturnCalendar() throws InterruptedException {

        HashSet<CalendarDay> didDoHabitDates = new HashSet<>();
        HashSet<CalendarDay> didNotDoHabitDates = new HashSet<>();
        String invalidUserId = "1234";

        // Test that lists are initially empty
        assertEquals(0, didDoHabitDates.size());
        assertEquals(0, didNotDoHabitDates.size());

        CalendarActivity.getAllCalendarEntries(invalidUserId, habit_id, didDoHabitDates, didNotDoHabitDates);

        // wait for database call to be completed
        Thread.sleep(2500);

        // Test that lists are empty and have 0 entries
        assertEquals(0, didDoHabitDates.size());
        assertEquals(0, didNotDoHabitDates.size());
    }

    /**
     * @given a valid user id and an invalid habit id
     * @when requesting calendar
     * @then days lists are empty and return zero entries
     */
    @Test
    public void getCalendar_invalidHabitId_doesNotReturnCalendar() throws InterruptedException {

        HashSet<CalendarDay> didDoHabitDates = new HashSet<>();
        HashSet<CalendarDay> didNotDoHabitDates = new HashSet<>();
        String invalidHabitId = "1234";

        // Test that lists are initially empty
        assertEquals(0, didDoHabitDates.size());
        assertEquals(0, didNotDoHabitDates.size());

        CalendarActivity.getAllCalendarEntries(user_id, invalidHabitId, didDoHabitDates, didNotDoHabitDates);

        // wait for database call to be completed
        Thread.sleep(2500);

        // Test that lists are empty and have 0 entries
        assertEquals(0, didDoHabitDates.size());
        assertEquals(0, didNotDoHabitDates.size());
    }

    /**
     * @given an empty user id and a valid habit id
     * @when requesting calendar
     * @then days lists are empty and return zero entries
     */
    @Test
    public void getCalendar_emptyUserId_doesNotReturnCalendar() throws InterruptedException {

        HashSet<CalendarDay> didDoHabitDates = new HashSet<>();
        HashSet<CalendarDay> didNotDoHabitDates = new HashSet<>();
        String user = "";

        // Test that lists are initially empty
        assertEquals(0, didDoHabitDates.size());
        assertEquals(0, didNotDoHabitDates.size());

        CalendarActivity.getAllCalendarEntries(user, habit_id, didDoHabitDates, didNotDoHabitDates);

        // wait for database call to be completed
        Thread.sleep(2500);

        // Test that lists are empty and have 0 entries
        assertEquals(0, didDoHabitDates.size());
        assertEquals(0, didNotDoHabitDates.size());
    }

    /**
     * @given a valid user id and an empty habit id
     * @when requesting calendar
     * @then days lists are empty and returns zero entries
     */
    @Test
    public void getAllJournals_emptyHabitId_doesNotReturnJournals() throws InterruptedException {

        HashSet<CalendarDay> didDoHabitDates = new HashSet<>();
        HashSet<CalendarDay> didNotDoHabitDates = new HashSet<>();
        String habit = "";

        // Test that lists are initially empty
        assertEquals(0, didDoHabitDates.size());
        assertEquals(0, didNotDoHabitDates.size());

        CalendarActivity.getAllCalendarEntries(user_id, habit, didDoHabitDates, didNotDoHabitDates);

        // wait for database call to be completed
        Thread.sleep(2500);

        // Test that lists are empty and have 0 entries
        assertEquals(0, didDoHabitDates.size());
        assertEquals(0, didNotDoHabitDates.size());
    }

    /**
     * @given a null user id and a valid habit id
     * @when requesting calendar
     * @then days lists are empty and return zero entries
     */
    @Test
    public void getCalendar_nullUserId_doesNotReturnCalendar() throws InterruptedException {

        HashSet<CalendarDay> didDoHabitDates = new HashSet<>();
        HashSet<CalendarDay> didNotDoHabitDates = new HashSet<>();
        String user = null;

        // Test that lists are initially empty
        assertEquals(0, didDoHabitDates.size());
        assertEquals(0, didNotDoHabitDates.size());

        CalendarActivity.getAllCalendarEntries(user, habit_id, didDoHabitDates, didNotDoHabitDates);

        // wait for database call to be completed
        Thread.sleep(2500);

        // Test that lists are empty and have 0 entries
        assertEquals(0, didDoHabitDates.size());
        assertEquals(0, didNotDoHabitDates.size());
    }

    /**
     * @given a valid user id and a null habit id
     * @when requesting calendar
     * @then days lists are empty and return zero entries
     */
    @Test
    public void getCalendar_nullHabitId_doesNotReturnCalendar() throws InterruptedException {

        HashSet<CalendarDay> didDoHabitDates = new HashSet<>();
        HashSet<CalendarDay> didNotDoHabitDates = new HashSet<>();
        String habit = null;

        // Test that lists are initially empty
        assertEquals(0, didDoHabitDates.size());
        assertEquals(0, didNotDoHabitDates.size());

        CalendarActivity.getAllCalendarEntries(user_id, habit, didDoHabitDates, didNotDoHabitDates);

        // wait for database call to be completed
        Thread.sleep(2500);

        // Test that lists are empty and have 0 entries
        assertEquals(0, didDoHabitDates.size());
        assertEquals(0, didNotDoHabitDates.size());
    }

}
