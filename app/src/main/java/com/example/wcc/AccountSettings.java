package com.example.wcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AccountSettings extends AppCompatActivity {
    private ImageView image;
    private Toolbar toolbar;
    private EditText setting_name;
    private ImageButton edit_name;
    private ImageButton edit_image_button;
    private ImageButton edit_status;
    private EditText setting_status;
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
        setting_status=findViewById(R.id.setting_status);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        String uid=firebaseUser.getUid();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=database.getReference().child("Users").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();
                setting_name.setText(name);
                setting_status.setText(status);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        edit_name=findViewById(R.id.edit_name_button);
        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDisplayName();
            }
        });
        edit_status=findViewById(R.id.edit_status_button);
        edit_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditStatus();
            }
        });



    }

    private void EditStatus() {
        DialogFragment dialogFragment=new StatusFragment();
        dialogFragment.show(getSupportFragmentManager(),"Edit Bio");
    }

    private void EditDisplayName() {
        DialogFragment dialogue=new Dialogue();
        FragmentManager fragmentManager=getSupportFragmentManager();
        dialogue.show(fragmentManager ,"Edit name");

    }
}
