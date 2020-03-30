package com.example.incrementum;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.android.core.auth.providers.userpassword.UserPasswordAuthProviderClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
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
public class SignupActivityTest {

    // Declare rule to be used to access activity
    @Rule
    public ActivityTestRule<SignupActivity> mActivityRule =
            new ActivityTestRule(SignupActivity.class);

    private SignupActivity activity;

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
     * @given a valid email and a valid passsword
     * @when registering as a user
     * @then registration succeeds
     */
    @Test
    public void validSignupTest() {
        String email = "hi@hi.hi";
        String password = "hihihi";

        UserPasswordAuthProviderClient emailPassClient = Stitch.getDefaultAppClient().getAuth().getProviderClient(
                UserPasswordAuthProviderClient.factory
        );
        emailPassClient.registerWithEmail(email, password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
                        //Assert that the registration is successful
                            assert (task.isSuccessful());
                        }

                });
    }

    /**
     * @given an invalid email and an invalid passsword
     * @when registering as a user
     * @then registration fails
     */
    @Test
    public void invalidSignup_invalidPasswordAndEmail_Test() {
        String email = "hoeofjfjf";
        String password = "hihi";

        UserPasswordAuthProviderClient emailPassClient = Stitch.getDefaultAppClient().getAuth().getProviderClient(
                UserPasswordAuthProviderClient.factory
        );
        emailPassClient.registerWithEmail(email, password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
                        //Assert that the signup failed
                        assert (!task.isSuccessful());
                    }

                });
    }

    /**
     * @given a valid email and an invalid passsword
     * @when registering as a user
     * @then registration succeeds
     */
    @Test
    public void invalidSignup_Invalidpassword_validEmail_Test() {
        String email = "hi@hi.hi";
        String password = "hihi";

        UserPasswordAuthProviderClient emailPassClient = Stitch.getDefaultAppClient().getAuth().getProviderClient(
                UserPasswordAuthProviderClient.factory
        );
        emailPassClient.registerWithEmail(email, password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
                        //Assert that the signup failed
                        assert (!task.isSuccessful());
                    }

                });
    }

    /**
     * @given an invalid email and a valid passsword
     * @when registering as a user
     * @then registration succeeds
     */
    @Test
    public void invalidSignup_Invalidemail_validPassword_Test() {
        String email = "jabaloopadoob";
        String password = "hihihi";

        UserPasswordAuthProviderClient emailPassClient = Stitch.getDefaultAppClient().getAuth().getProviderClient(
                UserPasswordAuthProviderClient.factory
        );
        emailPassClient.registerWithEmail(email, password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull final Task<Void> task) {
                        //Assert that the signup failed
                        assert (!task.isSuccessful());
                    }

                });
    }
}