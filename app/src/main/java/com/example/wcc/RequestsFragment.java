package com.example.wcc;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {
    private String current_uid;
    private View friend_view;
    private RecyclerView friends_list;
    private DatabaseReference friends_database;
    private String userId;
    private Query query;
    private DatabaseReference friend_ref;
    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        friend_view = inflater.inflate(R.layout.fragment_requests, container, false);
        friends_list = friend_view.findViewById(R.id.friend_fragment);
        friends_database = FirebaseDatabase.getInstance().getReference().child("Friends");
        friend_ref=FirebaseDatabase.getInstance().getReference().child("Users");
        userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        query = friends_database.child(userId).limitToLast(50);
        friends_list.setHasFixedSize(true);
        friends_list.setLayoutManager(new LinearLayoutManager(friend_view.getContext()));

        return friend_view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Friends> recyclerOptions = new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(query, Friends.class).setLifecycleOwner(RequestsFragment.this)
                .build();
        FirebaseRecyclerAdapter<Friends,FriendViewHolder> adapter=new FirebaseRecyclerAdapter<Friends, FriendViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final FriendViewHolder holder, int position, @NonNull Friends model) {
                holder.setName(model.getName());
                holder.setBio(model.getStatus());
                holder.setThumb(model.getThumb());
                final String uid=getRef(position).getKey();
                friend_ref.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name= dataSnapshot.child("name").getValue().toString();
                        String thumb= dataSnapshot.child("thumb").getValue().toString();
                        String status=dataSnapshot.child("status").getValue().toString();
                        holder.setName(name);
                        holder.setThumb(thumb);
                        holder.setBio(status);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(friend_view.getContext(),UserProfileActivity.class);
                        intent.putExtra("uid",uid);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_userlayout,parent,false);
                return new FriendViewHolder(view);
            }
        };
        adapter.startListening();
        friends_list.setAdapter(adapter);

    }


    public static class FriendViewHolder extends RecyclerView.ViewHolder {
        View mview;
        public FriendViewHolder(@NonNull View itemView) {
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
            Picasso.get().load(thumb).placeholder(R.drawable.ic_person_24px).into(civ);

        }
    }
}