package com.example.chatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.Adapter.UserAdapter;
import com.example.chatapp.Model.Chat;
import com.example.chatapp.Model.User;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {


    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats,container,false);
        //getting ID of recycler View from its XML source file
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser= FirebaseAuth.getInstance().getCurrentUser();//getting current user

        usersList=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                usersList.clear();

                for(DataSnapshot shot: snapshot.getChildren())
                {
                    Chat chat=shot.getValue(Chat.class);

                    if(chat.getSender()!=null && chat.getSender().equals(fuser.getUid()))
                    {
                        usersList.add(chat.getReceiver());
                    }

                    if(chat.getReceiver()!=null && chat.getReceiver().equals(fuser.getUid()))
                    {
                        usersList.add(chat.getSender());
                    }

                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return view;
    }


    private void readChats()
    {
        mUsers=new ArrayList<>();

        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mUsers.clear();

                //below is used to  display one user from Chats
                for(DataSnapshot shot: snapshot.getChildren())
                {
                    User user=shot.getValue(User.class);

                    for(String id: usersList)
                    {
                        if(user.getId()!=null && user.getId().equals(id))
                        {
                            if(mUsers.size()!=0) {
                                for (User user1 : mUsers)
                                {
                                    if(!user.getId().equals((user1.getId())))
                                    {
                                        mUsers.add(user);
                                    }


                                }
                            }
                            else{
                                mUsers.add(user);
                            }
                        }
                    }
                }
                userAdapter=new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}