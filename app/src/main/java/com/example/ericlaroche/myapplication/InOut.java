package com.example.ericlaroche.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *
 * Display in out table data in activity
 */

public class InOut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inoutscreen);
        Intent intent =getIntent();
        ArrayList <String>inOut = (ArrayList<String>)intent.getSerializableExtra("inouts");
        TextView text =  (TextView)findViewById(R.id.InOutText);
        for (int i =0 ;i < inOut.size(); i++) {
            text.append(inOut.get(i));
        }
    }
}
