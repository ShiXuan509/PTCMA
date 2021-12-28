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

public class ParentResult extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    RecyclerView recyclerView;
    ResultAdapter resultAdapter;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_result);

        bottomNavigationView = findViewById(R.id.bottom_navigator_parent_result);
        bottomNavigationView.setSelectedItemId(R.id.Result);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Result");
        firebaseStorage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recyclerviewparentresult);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.News:
                        startActivity(new Intent(getApplicationContext(), parentviewnews.class));
                        return true;
                    case R.id.Notices:
                        startActivity(new Intent(getApplicationContext(), ParentNotices.class));
                        return true;
                    case R.id.Result:
                        return true;
                }
                return false;
            }
        });

        FirebaseRecyclerOptions<ResultModel> options =
                new FirebaseRecyclerOptions.Builder<ResultModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Result"), ResultModel.class)
                        .build();

        resultAdapter = new ResultAdapter(options);
        recyclerView.setAdapter(resultAdapter);
    }

    protected void onStart () {
        super.onStart();
        resultAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        resultAdapter.stopListening();
    }
}