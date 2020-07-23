package com.example.wcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private Toolbar toolbar;
    private DatabaseReference firebaseDatabase;



    private Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        toolbar=findViewById(R.id.usersToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseDatabase=FirebaseDatabase.getInstance().getReference().child("Users");
        query= FirebaseDatabase.getInstance().getReference().child("Users").limitToLast(50);

        recyclerView=findViewById(R.id.recycleUsers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class).setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter<Users,UserViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Users, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Users model) {
                holder.setName(model.getName());
                holder.setBio(model.getStatus());
                holder.setThumb(model.getThumb());
                final String uid=getRef(position).getKey();

                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(AllUsersActivity.this,UserProfileActivity.class);
                        intent.putExtra("uid",uid);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.mini_userlayout, parent, false);

                return new UserViewHolder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class UserViewHolder extends RecyclerView.ViewHolder{
        View mview;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mview=itemView;
        }

        public void setName(String name) {
            TextView muser=mview.findViewById(R.id.user_name1);
            muser.setText(name);
        }

        public void setBio(String status) {
            TextView mBio=mview.findViewById(R.id.user_bio);
            mBio.setText(status);
        }

        public void setThumb(String thumb) {
            CircleImageView civ=mview.findViewById(R.id.profile_pic);
            Log.d("Thumb",thumb);
            Picasso.get().load(thumb).placeholder(R.drawable.ic_person_24px).into(civ);

        }
    }
}
