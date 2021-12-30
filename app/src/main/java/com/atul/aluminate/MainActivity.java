package com.atul.aluminate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.iammert.library.readablebottombar.ReadableBottomBar;
import com.atul.aluminate.Fragment.AddPostFragment;
import com.atul.aluminate.Fragment.HomeFragment;
import com.atul.aluminate.Fragment.NotificationFragment;
import com.atul.aluminate.Fragment.Profile2Fragment;
import com.atul.aluminate.Fragment.SearchFragment;
import com.atul.aluminate.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    
    ActivityMainBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Setting Custom toolbar
        setSupportActionBar(binding.toolbar);
        //Set title in toolbar
        MainActivity.this.setTitle("My Profile");

        //Show Home fragment by default
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        binding.toolbar.setVisibility(View.GONE);
        transaction.replace(R.id.content, new HomeFragment());
        transaction.commit();


        //Bottom Navigation
        binding.readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {

                //Replace Fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case 0:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.content, new HomeFragment());
                        break;
                    case 1:
                        binding.toolbar.setVisibility(View.GONE);
                        transaction.replace(R.id.content, new NotificationFragment());
                        break;
                    case 2:
                        transaction.replace(R.id.content, new AddPostFragment());
                        binding.toolbar.setVisibility(View.GONE);
                        break;
                    case 3:
                        transaction.replace(R.id.content, new SearchFragment());
                        binding.toolbar.setVisibility(View.GONE);
                        break;
                    case 4:
                        transaction.replace(R.id.content, new Profile2Fragment());
                        binding.toolbar.setVisibility(View.VISIBLE);
                        break;

                }
                transaction.commit();
            }
        });

    }


    //Set option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    //Option menu On click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}