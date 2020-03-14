package com.example.incrementum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.core.auth.providers.userpassword.UserPasswordAuthProviderClient;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @InjectView(R.id.signup_username) EditText nameInput;
    @InjectView(R.id.signup_email) EditText emailInput;
    @InjectView(R.id.signup_password) EditText passwordInput;
    @InjectView(R.id.btn_signup) Button signupButton;
//    @InjectView(R.id.back_to_login) Button loginLink;

    public static StitchAppClient cl;

//    @Override
//    protected void onStart(){
//        super.onStart();
//        if(cl == null){
//            cl = Stitch.initializeAppClient("incrementum-xjkms");
//        }
//
//        cl = Stitch.getAppClient("incrementum-xjkms");
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

//        loginLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        UserPasswordAuthProviderClient emailPassClient = Stitch.getDefaultAppClient().getAuth().getProviderClient(
                UserPasswordAuthProviderClient.factory
        );

        emailPassClient.registerWithEmail(email, password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull final Task<Void> task) {
                                               if (task.isSuccessful()) {
                                                   Log.i("stitch", "Successfully sent account confirmation email");
                                                   progressDialog.dismiss();
                                                   onSignupSuccess(name, email, password);

                                               } else {
                                                   Log.e("stitch", "Error registering new user:", task.getException());
                                                   progressDialog.dismiss();
                                                   onSignupFailed();
                                               }
                                           }
                                       }
                );
    }


    public void onSignupSuccess(String name, String email, String password) {

        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        openHabitActivity();
        sendData(name, email, password);
        finish();
    }

    public void openHabitActivity(){
        Intent intent = new Intent(this, Log_Habits_Hobbies_Time_Activity.class);
        startActivity(intent);
    }

    public void sendData(String name, String email, String password)
    {
        Intent intent = new Intent(getApplicationContext(),Log_Habits_Hobbies_Time_Activity.class);
        intent.putExtra("username", name);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    public void insertUser(String user_name, String e_mail, String pass_word) {

        final StitchAppClient client =
                Stitch.getAppClient("incrementum-xjkms");

        final RemoteMongoClient mongoClient =
                client.getServiceClient(RemoteMongoClient.factory, "mongodb-atlas");

        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Users");

        Document doc = new Document()
                .append("username", user_name)
                .append("email", e_mail)
                .append("password", pass_word);

        final Task<RemoteInsertOneResult> insert = coll.insertOne(doc);

        insert.addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<RemoteInsertOneResult> task) {
                if (task.isSuccessful()){
                    Log.i("STITCH", String.format("success inserting: %s",
                            task.getResult().getInsertedId()));
                }
            }
        });
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameInput.setError("must be least 3 characters");
            valid = false;
        } else {
            nameInput.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("enter a valid email address");
            valid = false;
        } else {
            emailInput.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordInput.setError("must be between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordInput.setError(null);
        }
        return valid;
    }
}


