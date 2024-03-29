package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Adapter.MessageAdapter;
import com.example.chatapp.Model.Chat;
import com.example.chatapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference;


    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
/*
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//This method sets the toolbar as the app bar for the activity.
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //above line adds back button on topleft side of toolbar
        //below function starts when we press back button on toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
*/

        //See this on internet
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        //stack wala is like first old data will come in bottom then new in bottom
        recyclerView.setLayoutManager(linearLayoutManager);


        profile_image=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        btn_send=findViewById(R.id.btn_send);
        text_send=findViewById(R.id.text_send);

        intent=getIntent();
        //getting object from another activity
        String userid=intent.getStringExtra("userId");
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=text_send.getText().toString();
                if(!msg.equals(""))
                {
                    sendMessage(fuser.getUid(),userid,msg);
                }
                else{
                    Toast.makeText(MessageActivity.this,"Yoy Can't send empty messsage ",Toast.LENGTH_SHORT).show();

                }

                text_send.setText("");
            }
        });


        reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

        //Classes implementing this interface can be used to receive
        // events about data changes at a location
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //This method will be called with a snapshot of the data at this location.

                User user=snapshot.getValue(User.class);
                username.setText(user.getUsername());
                //display username

                //display image
                if(user.getImageURL()!=null && user.getImageURL().equals("default"))
                {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);

                }

                readMessages(fuser.getUid(),userid,user.getImageURL());
            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String sender,String receiver,String message)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        //creates a Chat folder in firebase realtime database and saves the
        //message sent, and sender's userId and receiver's UserID
        reference.child("Chats").push().setValue(hashMap);

    }

    private void readMessages(String myid,String userid,String imageurl)
    {
        mchat=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mchat.clear();
                for(DataSnapshot shot:snapshot.getChildren())
                {
                    Chat chat= shot.getValue(Chat.class);
                    if(chat.getReceiver()!=null && chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver()!=null && chat.getReceiver().equals(userid) && chat.getSender().equals(myid) )
                    {
                        mchat.add(chat);
                    }


                }

                messageAdapter=new MessageAdapter(MessageActivity.this,mchat,imageurl);
                recyclerView.setAdapter(messageAdapter);



            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}