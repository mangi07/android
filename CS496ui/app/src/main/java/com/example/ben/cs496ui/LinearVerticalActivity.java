package com.example.ben.cs496ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class LinearVerticalActivity extends AppCompatActivity {

    private void setRadioListener(final int buttonResource, final int pictureResource){
        RadioButton radioButton = (RadioButton) findViewById(buttonResource);
        radioButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView pic = (ImageView) findViewById(R.id.chosen_image);
                pic.setImageResource(pictureResource);
                String name = v.getResources().getResourceName(pictureResource);
                TextView picName = (TextView) findViewById(R.id.image_text);
                picName.setText(name);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_vertical);

        setRadioListener(R.id.pic1button, R.drawable.waterfall);
        setRadioListener(R.id.pic2button, R.drawable.sunset);
        setRadioListener(R.id.pic3button, R.drawable.storm);
        setRadioListener(R.id.pic4button, R.drawable.forest_fire);
        setRadioListener(R.id.pic5button, R.drawable.clouds);
    }

}
