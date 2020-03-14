package com.example.incrementum;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.text.TextUtils;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.lang.NonNull;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.auth.StitchUser;
import com.mongodb.stitch.core.auth.providers.userpassword.UserPasswordCredential;
import com.mongodb.stitch.android.core.auth.providers.userpassword.UserPasswordAuthProviderClient;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    boolean loginAttempted;
    // Static variable that stores user who logs in
    public static String user_id;


    @InjectView(R.id.input_email) EditText emailInput;
    @InjectView(R.id.input_password) EditText passwordInput;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;
    @InjectView(R.id.reset_password) TextView _resetLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        loginAttempted = false;

        _resetLink.setVisibility(View.INVISIBLE);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        _resetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoverPassword();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        UserPasswordCredential credential = new UserPasswordCredential(email, password);
        Stitch.getDefaultAppClient().getAuth().loginWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<StitchUser>() {
                                           @Override
                                           public void onComplete(@NonNull final Task<StitchUser> task) {
                                               if (task.isSuccessful()) {
                                                   Log.d("stitch", "Successfully logged in as user " + task.getResult().getId());
                                                   onLoginSuccess(email);
                                                   user_id = task.getResult().getId();
                                               } else {
                                                   Log.e("stitch", "Error logging in with email/password auth:", task.getException());
                                                   progressDialog.dismiss();
                                                   passwordInput.setText("");
                                                   onLoginFailed();
                                               }
                                           }
                                       }
                );
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess();
//                        // onLoginFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String email) {
        _loginButton.setEnabled(true);
        openProfileActivity();
        sendData(email);
        //openViewHabitActivity(); - where we are supposed to go
        finish();
    }

    public void sendData(String email)
    {
        Intent intent = new Intent(getApplicationContext(), profileActivity1.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);

        if(loginAttempted == false) {

            _resetLink.setVisibility(View.VISIBLE);
            _resetLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recoverPassword();
                }
            });
            loginAttempted = true;
        }
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

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


    private void recoverPassword() {

        String email = emailInput.getText().toString();

        UserPasswordAuthProviderClient emailPassClient = Stitch.getDefaultAppClient().getAuth().getProviderClient(UserPasswordAuthProviderClient.factory);

        if (TextUtils.isEmpty(email)) {

            emailInput.setError("error! field is required!");

        } else {
            emailPassClient.sendResetPasswordEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull final Task<Void> task) {
                                                   if (task.isSuccessful()) {
                                                       Log.d("stitch-auth", "Successfully sent password reset email");
                                                       Toast.makeText(LoginActivity.this, "Password reset mail sent.", Toast.LENGTH_SHORT).show();
                                                   } else {
                                                       Log.e("stitch-auth", "Error sending password reset email:", task.getException());
                                                       Toast.makeText(LoginActivity.this, "Account not found!", Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           }
                    );
        }
    }





    public void openProfileActivity() {
        Intent intent = new Intent(this, profileActivity1.class);
        startActivity(intent);
    }


    public void openViewHabitActivity(){
        Intent intent = new Intent(this, ViewHabitActivity.class);
        startActivity(intent);
    }
}

