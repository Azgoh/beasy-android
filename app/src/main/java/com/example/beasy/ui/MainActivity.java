package com.example.beasy.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Acts as a Routing Hub for now. Instantly routes to RegisterActivity.
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish(); // removes MainActivity from back stack
    }
}