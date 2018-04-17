package com.example.ben.cs496ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/*
* All picture resources borrowed from:
* https://wpclipart.com
* */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView listViewItem1 = (TextView) findViewById(R.id.Layout1);
        listViewItem1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LinearVerticalActivity.class);
                startActivity(intent);
            }
        });

        TextView listViewItem2 = (TextView) findViewById(R.id.Layout2);
        listViewItem2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LinearHorizontalActivity.class);
                startActivity(intent);
            }
        });

        TextView listViewItem3 = (TextView) findViewById(R.id.Layout3);
        listViewItem3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Grid.class);
                startActivity(intent);
            }
        });


        TextView listViewItem4 = (TextView) findViewById(R.id.Layout4);
        listViewItem4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RelativeLayout.class);
                startActivity(intent);
            }
        });


        TextView listViewItem5 = (TextView) findViewById(R.id.Layout5);
        listViewItem5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BetterLayout.class);
                startActivity(intent);
            }
        });

    }
}
