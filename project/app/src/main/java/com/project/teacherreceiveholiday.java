package com.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class teacherreceiveholiday extends AppCompatActivity {

    //retrieve data here
    RecyclerView recyclerView;
    HolidaysAdapter holidaysAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherreceiveholiday);

        recyclerView = findViewById(R.id.recyclerview_holidays);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<HolidaysModel> options =
                new FirebaseRecyclerOptions.Builder<HolidaysModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Holidays Request"),HolidaysModel.class)
                        .build();

        holidaysAdapter = new HolidaysAdapter(options);
        recyclerView.setAdapter(holidaysAdapter);
    }

    protected void onStart () {
        super.onStart();
        holidaysAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        holidaysAdapter.stopListening();
    }
}