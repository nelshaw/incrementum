package com.example.incrementum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.InjectView;

public class profileActivity1 extends AppCompatActivity {

    @InjectView(R.id.emailText) TextView _emailText;

    Intent intent = getIntent();
    String email = intent.getStringExtra("email");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        _emailText.setText(email);

    }
}
