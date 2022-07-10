package com.example.animations_and_views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.animations_and_views.animation.AnimatorActivity;
import com.example.animations_and_views.custom_view.DrawPointsActivity;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button animatorButton = findViewById(R.id.animator);
        animatorButton.setOnClickListener(view -> {
             intent = new Intent(MainActivity.this, AnimatorActivity.class);
             startActivity(intent);
        });

        Button drawButton = findViewById(R.id.custom_view_activity);
        drawButton.setOnClickListener(view -> {
            intent = new Intent(MainActivity.this, DrawPointsActivity.class);
            startActivity(intent);
        });
    }
}