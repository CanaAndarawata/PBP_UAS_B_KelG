package com.example.tubes_uas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tubes_uas.UserCRUD.CreateUserActivity;
import com.example.tubes_uas.UserCRUD.ShowListUserActivity;

public class MainActivity extends AppCompatActivity {
    private CardView cvCreateUser, cvShowListUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cvCreateUser = findViewById(R.id.cvCreateUser);
        cvShowListUser = findViewById(R.id.cvShowListUser);

        cvCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CreateUserActivity.class);
                startActivity(i);
            }
        });

        cvShowListUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ShowListUserActivity.class);
                startActivity(i);
            }
        });
    }
}