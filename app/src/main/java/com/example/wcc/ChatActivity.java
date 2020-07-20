package com.example.wcc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DatabaseReference Users,root;
    private TextView user_name;
    private TextView last_seen;
    private TextInputLayout send_message;
    private String user_name1;
    private FirebaseAuth auth;
    private CircleImageView profile_pic;
    private String image;
    private String current_user;
    private AppCompatButton send_button;
    private String message;
    private RecyclerView chat_recycle;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private DatabaseReference messageDatabase;

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
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action = layoutInflater.inflate(R.layout.chat_app_bar, null);
        actionBar.setCustomView(action);
        last_seen = findViewById(R.id.last_seen);
        send_message = findViewById(R.id.send_chat);
        send_button = findViewById(R.id.send_chat_button);

        messageAdapter =new MessageAdapter(messagesList);

        chat_recycle = findViewById(R.id.chat_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        chat_recycle.setHasFixedSize(true);
        chat_recycle.setLayoutManager(linearLayoutManager);
        chat_recycle.setAdapter(messageAdapter);


        current_user = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        final String chat_reciever = getIntent().getStringExtra("uid");
        loadmessage(chat_reciever);
        profile_pic = findViewById(R.id.profile_pic_chat_convo_page);
        user_name = findViewById(R.id.chat_name);
        Users = FirebaseDatabase.getInstance().getReference().child("Users").child(chat_reciever);
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

        root.child("Users").child(chat_reciever).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String online = dataSnapshot.child("online").getValue().toString();
                if (online.equals("true")) {
                    last_seen.setText("Online");
                } else {
                    TimeSince timeSince = new TimeSince();
                    long prev = Long.parseLong(online);
                    String now = timeSince.getTimeAgo(prev);
                    last_seen.setText("last seen " + now);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        root.child("Chats").child(current_user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(chat_reciever)) {
                    Map chatMap = new HashMap();
                    chatMap.put("seen", false);
                    chatMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUser = new HashMap();
                    chatUser.put("Chat/" + current_user + "/" + chat_reciever, chatMap);
                    chatUser.put("Chat/" + chat_reciever + "/" + current_user, chatMap);

                    root.updateChildren(chatUser, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_Message(chat_reciever);
            }
        });

    }

    private void loadmessage(String chat_reciever) {
        root.child("messages").child(current_user).child(chat_reciever).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages message = dataSnapshot.getValue(Messages.class);

                messagesList.add(message);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void send_Message(String chat_reciever) {
        message = send_message.getEditText().getText().toString();
        if(!TextUtils.isEmpty(message))
        {
            String current_user_ref = "messages/" + current_user + "/" + chat_reciever;
            String chat_user_ref = "messages/" + chat_reciever + "/" + current_user;
            DatabaseReference user_push = root.child("messages").child(current_user).child(chat_reciever).push();
            String push_id = user_push.getKey();
            Map messageMap = new HashMap();
            messageMap.put("message" ,message);
            messageMap.put("seen" , false);
            messageMap.put("type" , "text");
            messageMap.put("time" ,ServerValue.TIMESTAMP);
            messageMap.put("from",chat_reciever);

            Map userMap = new HashMap();
            userMap.put(current_user_ref +"/" + push_id, messageMap);
            userMap.put(chat_user_ref + "/" + push_id , messageMap);

            root.updateChildren(userMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if(databaseError != null)
                    {

                    }
                }
            });
        }
    }
}
