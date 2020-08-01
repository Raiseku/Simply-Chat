package com.example.raise.simplychatmancinelli;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    private Button regButton;
    private Button logButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        regButton = (Button) findViewById(R.id.regButton);
        logButton = (Button) findViewById(R.id.logButton);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(reg_intent);
            }
        });

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log_intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(log_intent);
            }
        });
    }
}
