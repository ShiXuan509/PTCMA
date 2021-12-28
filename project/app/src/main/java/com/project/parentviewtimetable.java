package com.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class parentviewtimetable extends AppCompatActivity {

    //retrieve data here

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    RecyclerView recyclerView;
    TimeTableAdapter timeTableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentviewtimetable);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Time Table");
        firebaseStorage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recyclerview_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<TimeTableModel> options =
                new FirebaseRecyclerOptions.Builder<TimeTableModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Time Table"),TimeTableModel.class)
                        .build();

        timeTableAdapter = new TimeTableAdapter(options);
        recyclerView.setAdapter(timeTableAdapter);
    }

    protected void onStart () {
        super.onStart();
        timeTableAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        timeTableAdapter.stopListening();
    }
}