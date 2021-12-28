package com.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class parentviewnews extends AppCompatActivity {

    //retrieve data here

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentviewnews);

        bottomNavigationView = findViewById(R.id.bottom_navigator_parent_news);
        bottomNavigationView.setSelectedItemId(R.id.News);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("News");
        firebaseStorage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recyclerviewparentnews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.News:
                        return true;
                    case R.id.Notices:
                        startActivity(new Intent(getApplicationContext(), ParentNotices.class));
                        return true;
                    case R.id.Result:
                        startActivity(new Intent(getApplicationContext(), ParentResult.class));
                        return  true;
                }
                return false;
            }
        });

        FirebaseRecyclerOptions<NewsModel> options =
                new FirebaseRecyclerOptions.Builder<NewsModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("News"),NewsModel.class)
                        .build();

        newsAdapter = new NewsAdapter(options);
        recyclerView.setAdapter(newsAdapter);


    }

    protected void onStart () {
        super.onStart();
        newsAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        newsAdapter.stopListening();
    }
}