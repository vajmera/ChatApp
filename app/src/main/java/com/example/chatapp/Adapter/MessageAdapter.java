package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.MessageActivity;
import com.example.chatapp.Model.Chat;
import com.example.chatapp.Model.User;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;

    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }
    @NonNull
    @Override

    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        /*
         *    Converting XML appearance definition into View objects in code is called
         *       inflation.
         *  2. Take an XML view,create its java object, set vaues for all attributes and recursively
         * do it for its child
         */
        if(viewType==MSG_TYPE_RIGHT)
        {
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            //Takes XML to convert to java object
            //parent a layout that you would use a parent for v in viewheirachy
            return new MessageAdapter.ViewHolder(view);

        }
        else{
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            //Takes XML to convert to java object
            //parent a layout that you would use a parent for v in viewheirachy
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat=mChat.get(position);
        holder.show_message.setText(chat.getMessage());

        if(imageurl.equals("default"))
        {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    /*
    1. ViewHolder
    1.1 Operates on one row at a time
    1.2 Extends RecyclerView.ViewHolder
    1.2.1 Get view componenets inside the layout that repersents each row
    1.2.2 Populate these View Components with information that represents the
    current position of Adapter Collecction
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(View itemView)
        {
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);

        }

    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else return MSG_TYPE_LEFT;
    }
}
