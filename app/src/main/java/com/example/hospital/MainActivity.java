package com.example.hospital;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void GoOnLoginActivity(View view){
        startActivity(new Intent(MainActivity.this,Intern_login.class));
    }
    public void GoOnInfoActivity(View view){
        startActivity(new Intent(MainActivity.this,ApplicatiionInformation.class));
    }

}