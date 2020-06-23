package com.example.wcc;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private Query query;
    private DatabaseReference chats;
    private RecyclerView recyclerView;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_chat, container, false);
        chats=FirebaseDatabase.getInstance().getReference().child("Chats");
        query=FirebaseDatabase.getInstance().getReference().child("Chats").child(FirebaseAuth.getInstance().getUid()).limitToLast(50);
        recyclerView=rootView.findViewById(R.id.MainChatRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<ChatMessage> firebaseRecyclerOptions=new
                FirebaseRecyclerOptions.Builder<ChatMessage>().setQuery(query,ChatMessage.class).setLifecycleOwner(this).build();
        FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder> firebaseRecyclerAdapter=new
                FirebaseRecyclerAdapter<ChatMessage, ChatViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull ChatMessage model) {
                holder.setName(model.getUser());
                holder.setMessage(model.getText());
                holder.setThumb(model.getThumb());
                String uid=getRef(position).getKey();
                holder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),ChatActivity.class);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.main_page_mini_user, parent, false);

                return new ChatViewHolder(view);

            }
        };
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private static class ChatViewHolder extends RecyclerView.ViewHolder{
        View mview;

        private ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mview=itemView;
        }
        public void setName(String name) {
            TextView muser=mview.findViewById(R.id.user_name_main_page);
            muser.setText(name);
        }

        public void setMessage(String message) {
            TextView mLastmessage=mview.findViewById(R.id.last_message);
            mLastmessage.setText(message);
        }

        public void setThumb(String thumb) {
            CircleImageView civ=mview.findViewById(R.id.profile_pic_chat_convo_page);
            Picasso.get().load(thumb).placeholder(R.drawable.ic_person_24px).into(civ);

        }
    }
}
