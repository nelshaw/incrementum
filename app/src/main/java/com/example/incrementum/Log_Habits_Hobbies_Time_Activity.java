package com.example.incrementum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteUpdateResult;

import org.bson.Document;

import java.security.Key;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static com.example.incrementum.DatabaseHelper.mongoClient;

public class Log_Habits_Hobbies_Time_Activity extends AppCompatActivity {

    private static final String ALGORITHM = "AES";
    private static final String KEY = "1Hbfh667adfDEJ78";

    Button EnterButton;
    EditText HobbyText;
    EditText startTimeText;
    EditText endTimeText;

    //from signup
    String email;
    String username;
    String password;
    ArrayList<String> empty = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__habits__hobbies__time_);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");


        EnterButton = findViewById(R.id.addHabitsHobbiesTimeButton);
        HobbyText = findViewById(R.id.editText5);
        startTimeText = findViewById(R.id.start);
        endTimeText = findViewById(R.id.end);
        EnterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openHabitActivity();
            }
        });
    }

    public void openHabitActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        Toast.makeText(this.getBaseContext(),"Please verify the email " + email, Toast.LENGTH_LONG).show();
        insertUser(username, email, password);
        startActivity(intent);
    }

    public void insertUser(String user_name, String e_mail, String pass_word) {

      // Connect to MongoDB client
        final RemoteMongoCollection<Document> coll =
                mongoClient.getDatabase("Incrementum").getCollection("Users");



        try {
            pass_word = encrypt(pass_word);
        } catch (Exception e) {
            e.printStackTrace();
        }

        empty.add(HobbyText.getText().toString());

        Document doc = new Document()
                .append("username", user_name)
                .append("email", e_mail)
                .append("password", pass_word)
                .append("hobbies", empty)
                .append("sensitive_time_start",startTimeText.getText().toString())
                .append("sensitive_time_end",endTimeText.getText().toString());

        final Task<RemoteInsertOneResult> insert = coll.insertOne(doc);

        insert.addOnCompleteListener(new OnCompleteListener<RemoteInsertOneResult>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<RemoteInsertOneResult> task) {
                if (task.isSuccessful()) {
                    Log.i("STITCH", String.format("success inserting: %s",
                            task.getResult().getInsertedId()));
                    UserInfo user = (UserInfo) getApplication();
                    user.setUserId(task.getResult().getInsertedId().toString().split("=")[1].replace('}',' ').trim());
                    Log.d("**S*DS*J*SJD*",user.getUserId());
                }
            }
        });
    }

    public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(Log_Habits_Hobbies_Time_Activity.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;
    }

    /*
    public static String decrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;

    }
    */


    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(Log_Habits_Hobbies_Time_Activity.KEY.getBytes(),Log_Habits_Hobbies_Time_Activity.ALGORITHM);
        return key;
    }


}
