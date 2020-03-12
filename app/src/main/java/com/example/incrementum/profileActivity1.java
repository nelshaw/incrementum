package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import butterknife.InjectView;

public class profileActivity1 extends AppCompatActivity {

    TextView _emailText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        _emailText = findViewById(R.id.emailText);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        Toast.makeText(getBaseContext(),email, Toast.LENGTH_LONG).show();





        
        _emailText.setText(email);
    }
}
