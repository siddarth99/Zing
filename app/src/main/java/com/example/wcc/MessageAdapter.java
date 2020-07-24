package com.example.wcc;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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
        public RelativeLayout chat_box_layout;
        public TextView time;
        public RelativeLayout.LayoutParams params;
        public RelativeLayout.LayoutParams params1;
        private ImageView imageView;
        public Context context;
        private TextView image_time;
        public MessageViewHolder(View view){
            super(view);

            imageView = view.findViewById(R.id.ImageView_image);
            image_time = view.findViewById(R.id.message_image_time);
            messageText = view.findViewById(R.id.Chatbubble);
            time = view.findViewById(R.id.message_time);
            chat_box_layout = view.findViewById(R.id.chatLinear);
            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params1.addRule(RelativeLayout.ALIGN_PARENT_START);
            context = view.getContext();
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String current_user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Messages m = message_list.get(position);
        String type = m.getType();
        int pos = getItemViewType(position);
        String from_user = m.getFrom();
        if(type.equals("text") && message_list.get(pos).getType().equals("text")) {
            holder.imageView.setImageDrawable(null);
            holder.imageView.setVisibility(View.GONE);
            holder.image_time.setVisibility(View.GONE);
            if (from_user.equals(current_user)) {
                holder.messageText.setBackground(ContextCompat.getDrawable(holder.context, R.drawable.chatbubbledraw1));
                holder.chat_box_layout.setLayoutParams(holder.params);
            } else {
                holder.messageText.setBackground(ContextCompat.getDrawable(holder.context, R.drawable.chatbubbledraw));
                holder.chat_box_layout.setLayoutParams(holder.params1);
            }
            holder.messageText.setText(m.getMessage());
            holder.time.setText(Long.toString(m.getTime()));
        }
        else{

            holder.imageView.setVisibility(View.VISIBLE);
            holder.messageText.setVisibility(View.INVISIBLE);
            holder.time.setVisibility(View.INVISIBLE);

            Log.d("Message image url", m.getMessage());
            Picasso.get().load(m.getMessage()).placeholder(R.drawable.ic_insert_photo_24px).into(holder.imageView);
            if (from_user.equals(current_user)) {
                holder.chat_box_layout.setBackgroundResource(R.drawable.imagebubble2);
                holder.image_time.setText(Long.toString(m.getTime()));
                holder.chat_box_layout.setLayoutParams(holder.params);
            } else {
                holder.chat_box_layout.setLayoutParams(holder.params1);
                holder.image_time.setText(Long.toString(m.getTime()));
                holder.chat_box_layout.setBackgroundResource(R.drawable.imagebubbledraw);
            }
        }
        if(position == message_list.size()-1){
            Log.d("seen","inside seen");
            if(m.getSeen()){
                if(m.getType().equals("text")){
                    DrawableCompat.setTint(holder.messageText.getBackground(), ContextCompat.getColor(holder.context,R.color.colorSecondary));
                }
                else{
                    Log.d("seen","inside seen");
                    DrawableCompat.setTint(holder.chat_box_layout.getBackground(),ContextCompat.getColor(holder.context,R.color.colorSecondary));
                }
            }
        }

    }


    @Override
    public int getItemCount() {
        return message_list.size();
    }
}
