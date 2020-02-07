package com.example.wcc;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class Profile_picture extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        toolbar=(Toolbar)findViewById(R.id.profile_pic_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile Photo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView=findViewById(R.id.full_profile_pic);
        imageView.setImageResource(R.drawable.profile_pic);
    }
}