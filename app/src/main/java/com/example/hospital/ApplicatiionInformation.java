package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ApplicatiionInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicatiion_information);
        getSupportFragmentManager().beginTransaction().add(R.id.infoFrame, CreatorInfo.class,null).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.Creator){
                    getSupportFragmentManager().beginTransaction().replace(R.id.infoFrame, CreatorInfo.class,null).commit();
                    return true;
                }
                else if (item.getItemId()==R.id.AppInfo){
                    getSupportFragmentManager().beginTransaction().replace(R.id.infoFrame, AppInformation.class,null).commit();
                    return true;
                }

                return false;
            }
        });
    }
}