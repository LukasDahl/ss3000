package com.cdio.ss3000;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;


public class HelpActivity extends AppCompatActivity {

    TextView helptext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


        helptext = findViewById(R.id.brodtextTV);
       helptext.setMovementMethod(new ScrollingMovementMethod());
    }
}
