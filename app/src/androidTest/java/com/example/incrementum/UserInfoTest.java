package com.example.incrementum;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.Block;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UserInfoTest  {

    String habitID = "5e6861323620b2a02aa265fb";
    String userID = "5e6b032f1c9d440000c842e5";
    String email = "hi@hi.hi";
    String username = "username123";
    String path = "/storage/emulated/0/Pictures/pp.jpg";


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
     * @given a matching email
     * @when requesting your email from userInfo
     * @then the correct email is returned
     */
    @Test
    public void getEmail_validEmail_Test() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UserInfo userData = (UserInfo) appContext.getApplicationContext();
        userData.setEmail(email);
        //assert the emails match
        assertEquals(email, userData.getEmail());
    }

    /**
     * @given a unique email to set
     * @when requesting your email from userInfo
     * @then the returned email does not match original email
     */
    @Test
    public void getEmail_invalidEmail_Test() {
        String fakeEmail = "j@bloo.gob";
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UserInfo userData = (UserInfo) appContext.getApplicationContext();
        userData.setEmail(fakeEmail);
        //assert the emails do not match
        assertNotEquals(email, userData.getEmail());
    }

    /**
     * @given a matching username
     * @when requesting your username from userInfo
     * @then the correct username is returned
     */
    @Test
    public void getUsername_validUsername_Test() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UserInfo userData = (UserInfo) appContext.getApplicationContext();
        userData.setUserName(username);
        //assert the usernames match
        assertEquals(username, userData.getUserName());
    }

    /**
     * @given a unique username to set
     * @when requesting your username from userInfo
     * @then the returned username does not match original username
     */
    @Test
    public void getUsername_invalidUsername_Test() {
        String fakeUN = "username";
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UserInfo userData = (UserInfo) appContext.getApplicationContext();
        userData.setUserName(fakeUN);
        //assert the usernames do not match
        assertNotEquals(username, userData.getUserName());
    }

    /**
     * @given a matching path
     * @when requesting your path from userInfo
     * @then the correct path is returned
     */
    @Test
    public void getPath_validPath_Test() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UserInfo userData = (UserInfo) appContext.getApplicationContext();
        userData.setPicturePath(path);
        //assert the path's match
        assertEquals(path, userData.getPicturePath());
    }

    /**
     * @given a unique path to set
     * @when requesting your path from userInfo
     * @then the returned path does not match original path
     */
    @Test
    public void getPath_invalidPath_Test() {
        String fakePath = "/storage/emulated/0/Pictures/fake.jpg";
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UserInfo userData = (UserInfo) appContext.getApplicationContext();
        userData.setPicturePath(fakePath);
        //assert that the paths do not match
        assertNotEquals(path, userData.getPicturePath());
    }

    /**
     * @given a matching habit id
     * @when requesting your habit id from userInfo
     * @then the correct habit id is returned
     */
    @Test
    public void getHabitID_validHabitID_Test() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UserInfo userData = (UserInfo) appContext.getApplicationContext();
        userData.setHabitId(habitID);
        //assert that the habit id's match
        assertEquals(habitID, userData.getHabitId());
    }

    /**
     * @given a unique habit id to set
     * @when requesting your habit id from userInfo
     * @then the returned habit id does not match original habit id
     */
    @Test
    public void getHabitID_invalidHabitID_Test() {
        String fakeHabitID = "12638484746469289284";
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UserInfo userData = (UserInfo) appContext.getApplicationContext();
        userData.setHabitId(fakeHabitID);
        //assert that the habit id's do not match
        assertNotEquals(habitID, userData.getHabitId());
    }

    /**
     * @given a matching user id
     * @when requesting your user id from userInfo
     * @then the correct user id is returned
     */
    @Test
    public void getUserID_validUserID_Test() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UserInfo userData = (UserInfo) appContext.getApplicationContext();
        userData.setUserId(userID);
        //assert that the user id's match
        assertEquals(userID, userData.getUserId());
    }

    /**
     * @given a unique user id to set
     * @when requesting your user id from userInfo
     * @then the returned user id does not match original user id
     */
    @Test
    public void getUserID_invalidUserID_Test() {
        String fakeUserID = "8274874874810840104804";
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        UserInfo userData = (UserInfo) appContext.getApplicationContext();
        userData.setUserId(fakeUserID);
        //assert that the user id's do not match
        assertNotEquals(userID, userData.getUserId());
    }



}