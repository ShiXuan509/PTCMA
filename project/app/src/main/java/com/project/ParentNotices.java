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

public class ParentNotices extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    RecyclerView recyclerView;
    NoticesAdapter noticesAdapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_notices);

        bottomNavigationView = findViewById(R.id.bottom_navigator_parent_notices);
        bottomNavigationView.setSelectedItemId(R.id.Notices);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Notices");
        firebaseStorage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recyclerviewparentnotices);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.News:
                        startActivity(new Intent(getApplicationContext(), ParentNotices.class));
                        return true;
                    case R.id.Notices:
                        return true;
                    case R.id.Result:
                        startActivity(new Intent(getApplicationContext(),ParentResult.class));
                        return true;
                }
                return false;
            }
        });

        FirebaseRecyclerOptions<NoticesModel> options =
                new FirebaseRecyclerOptions.Builder<NoticesModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Notices"),NoticesModel.class)
                        .build();

        noticesAdapter = new NoticesAdapter(options);
        recyclerView.setAdapter(noticesAdapter);
    }

    protected void onStart () {
        super.onStart();
        noticesAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        noticesAdapter.stopListening();
    }
}