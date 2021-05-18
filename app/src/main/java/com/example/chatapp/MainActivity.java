package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.bumptech.glide.Glide;
import com.example.chatapp.Fragments.ChatsFragment;
import com.example.chatapp.Fragments.UsersFragment;
import com.example.chatapp.Model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getSupportActionBar().setTitle("Register");

/*
A Reference represents a specific location in your Database
and can be used for reading or writing data to that Database location.
 */
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        //below is used to set username and profile_pic

        /*
        A DataSnapshot instance contains data from a Firebase Database location.
         Any time you read Database data, you receive the data as a DataSnapshot.
         */
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                //Glide is an Image Loader Library
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MainActivity.this).load(user.getImageURL()).into(profile_image);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        TabLayout tabLayout=findViewById(R.id.tab_layout);
        ViewPager viewPager=findViewById(R.id.view_pager);
        //above use??
        /*
        You can create swipe views using AndroidX's ViewPager widget.
         To use ViewPager and tabs,
        you need to add a dependency on ViewPager and on Material Components to your project.

        We use fragments to create individual page sliders

         */
        //Fragmnets are like different componensts on a single page
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ChatsFragment(),"Chats");//create the view by inflating
        viewPagerAdapter.addFragment(new UsersFragment(),"Users");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }


        public boolean onCreateOptionsMenu(Menu menu)
        {
            //inflator is used basicaly to get the XML content into java variable
            getMenuInflater().inflate(R.menu.menu,menu);
            return true;
        }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,StartActivity.class));
                finish();
                return true;
        }

        return false;

    }
//To insert child views that represent each page, you need to hook this layout to a PagerAdapter.
   /*
   FragmentPagerAdapter -
    Use this when navigating between a fixed, small number of sibling screens.
    */
    class ViewPagerAdapter extends FragmentPagerAdapter{

        @NonNull

//List of fragments and theri titles
        private ArrayList<Fragment> fragments;
        private ArrayList<String>titles;

        ViewPagerAdapter(FragmentManager fn)
        {
            super(fn);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }

        //returns the fragment
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        //returns count of fragments
        @Override
        public int getCount() {
            return fragments.size();
        }

        //add fragments
        public void addFragment(Fragment fragment,String title)
        {
            fragments.add(fragment);
            titles.add(title);
        }

        public CharSequence getPageTitle(int position)
        {
            return titles.get(position);
        }



    }

}
