package com.example.assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

public class startActivity extends AppCompatActivity {

    private Button service_button;
    private Button client_button;

    private TextView welcome_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        service_button = findViewById(R.id.service_button);
        client_button = findViewById(R.id.client_button);

        welcome_text = findViewById(R.id.welcome_textView);

        service_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(startActivity.this, ProviderActivity.class));
            }
        });

        client_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(startActivity.this, RequesterActivity.class));
            }
        });
    }


}
