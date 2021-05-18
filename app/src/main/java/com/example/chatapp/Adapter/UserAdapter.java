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
import com.example.chatapp.Model.User;
import com.example.chatapp.R;

import java.util.List ;
/*
    1. Adapter manages collection and UI operations
    1.1 Operates on entire collection
    1.2 Gets a layout that represents the current row, passes it to ViewHolder
    for population.
    1.3 Binds data to ViewHolder
    1.4 Creates views for items and replace content of some views with new data items
    1.5 provide a count of items

 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContext;//COntext used to get state of system, pass objects from one activity to another
    private List<User> mUsers;

    public UserAdapter(Context mContext,List<User>mUsers)
    {
        this.mContext=mContext;
        this.mUsers=mUsers;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
       /*
       *    Converting XML appearance definition into View objects in code is called
       *       inflation.
       *  2. Take an XML view,create its java object, set vaues for all attributes and recursively
       * do it for its child
        */
        View view= LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
                                                //Takes XML to convert to java object
        //parent a layout that you would use a parent for v in viewheirachy
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull UserAdapter.ViewHolder holder, int position) {
        User user=mUsers.get(position);
        holder.username.setText(user.getUsername());

        if(user.getImageURL().equals("default"))
        {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userId",user.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
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
        public TextView username;
        public ImageView profile_image;

        public ViewHolder(View itemView)
        {
            super(itemView);
            username=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_image);

        }

    }

}
