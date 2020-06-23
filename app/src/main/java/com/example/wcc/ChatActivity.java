package com.example.wcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DatabaseReference Users,root;
    private TextView user_name;
    private TextView last_seen;
    private String user_name1;
    private CircleImageView profile_pic;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converstation_chat);
        toolbar = findViewById(R.id.chat_convo_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        root = FirebaseDatabase.getInstance().getReference();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action = layoutInflater.inflate(R.layout.chat_app_bar,null);
        actionBar.setCustomView(action);
        last_seen = findViewById(R.id.last_seen);

        final String uid=getIntent().getStringExtra("uid");
        profile_pic = findViewById(R.id.profile_pic_chat_convo_page);
        user_name= findViewById(R.id.chat_name);
        Users = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        actionBar.setDisplayShowTitleEnabled(false);

        Users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user_name2 = dataSnapshot.child("name").getValue().toString();
                user_name.setText(user_name2);
                image = dataSnapshot.child("image").getValue().toString();
                Picasso.get().load(image).placeholder(R.drawable.ic_person_24px).into(profile_pic);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        root.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String online = dataSnapshot.child("online").getValue().toString();
                if (online.equals("true")) {
                    last_seen.setText("Online");
                } else {
                    TimeSince timeSince = new TimeSince();
                    long prev = Long.parseLong(online);
                    String now = timeSince.getTimeAgo(prev);
                    last_seen.setText("last seen "+now);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
