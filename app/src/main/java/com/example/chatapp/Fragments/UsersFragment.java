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
import com.example.chatapp.Model.User;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*
1. Adapter takes a list of java objects and makes it visible in recycler view
2. Adapter creates view Holder and view Holder's job is to take individual list
elements and show in recycler view layout
3. Each component of RecyclerView layout is called Row layout.
4. List Elements to Adapter to ViewHolder to Row layout
5. Put thtat rowlayout in recycler view and do it for next as users scroll.
*/

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view=inflater.inflate(R.layout.fragment_users, container, false);
       recyclerView=view.findViewById(R.id.recycler_view);
       recyclerView.setHasFixedSize(true);//NOt to  move around
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


       mUsers=new ArrayList<>();
       readUsers();
        return view;
    }

    private void readUsers()
    {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
               mUsers.clear();
                for(DataSnapshot snap:snapshot.getChildren() )
                {
                    User user=snap.getValue(User.class);
                    assert user!=null;
                    assert firebaseUser!=null;

                    if(user.getId()!=null && !user.getId().equals(firebaseUser.getUid()))
                    {
                        mUsers.add(user);
                    }
                }

                userAdapter=new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }
}