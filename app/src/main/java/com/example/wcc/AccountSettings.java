package com.example.wcc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountSettings extends AppCompatActivity {
    private ImageView image;
    private Toolbar toolbar;
    private EditText setting_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        image= findViewById(R.id.profile_image);
        toolbar=(Toolbar)findViewById(R.id.settings_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AccountSettings.this,Profile_picture.class);
                startActivity(intent);
            }
        });

        setting_name=findViewById(R.id.setting_name);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        String name=firebaseUser.getDisplayName();
        setting_name.setText(name);



    }
}
