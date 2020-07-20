package com.example.wcc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> message_list;

    public MessageAdapter(List<Messages> message_list){
        this.message_list = message_list;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatboxlayout,parent,false);

        return new MessageViewHolder(v);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView messageText;
        public TextView time;

        public MessageViewHolder(View view){
            super(view);

            messageText = view.findViewById(R.id.Chatbubble);
            time = view.findViewById(R.id.message_time);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String current_user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Messages m = message_list.get(position);
        String from_user = m.getFrom();
        if(from_user.equals(current_user)){
            holder.messageText.setGravity(View.FOCUS_RIGHT);
        }
        holder.messageText.setText(m.getMessage());
        holder.time.setText(Long.toString(m.getTime()));


    }

    @Override
    public int getItemCount() {
        return message_list.size();
    }
}
