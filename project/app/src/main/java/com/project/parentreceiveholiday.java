package com.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class parentreceiveholiday extends AppCompatActivity {

    //retrieve data here
    RecyclerView recyclerView;
    ParentHolidaysAdapter parentHolidaysAdapter;
    String name;
    FloatingActionButton addFab;
    TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentreceiveholiday);

        emptyText = findViewById(R.id.emptyText);
        addFab = findViewById(R.id.addFab);

        recyclerView = findViewById(R.id.recyclerview_holidays);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),parentrequestholiday.class));
                finish();
            }
        });

        String name = getIntent().getStringExtra("name");

        FirebaseRecyclerOptions<HolidaysModel> options =
                new FirebaseRecyclerOptions.Builder<HolidaysModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Parent Holidays Request").child(name),HolidaysModel.class)
                        .build();
        parentHolidaysAdapter = new ParentHolidaysAdapter(options, recyclerView, emptyText, name);
        recyclerView.setAdapter(parentHolidaysAdapter);

    }

    protected void onStart () {
        super.onStart();
        parentHolidaysAdapter.startListening();
    }

    protected void onStop() {
        super.onStop();
        parentHolidaysAdapter.stopListening();
    }
}