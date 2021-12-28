package com.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class parentmenu extends AppCompatActivity {

    private Button Request_Holiday_For_Kids;
    private Button Time_Table;
    private Button Send_Request;
    private Button Logout;
    private Button News;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentmenu);

        Request_Holiday_For_Kids = (Button) findViewById(R.id.btnRequestHolidayForKids);
        Time_Table = (Button) findViewById(R.id.btnTimeTable);
        Send_Request = (Button) findViewById(R.id.btnSendRequestAndInquiry);
        Logout = (Button) findViewById(R.id.btnLogout);
        News = (Button) findViewById(R.id.btnNews);
        firebaseAuth = FirebaseAuth.getInstance();

        String[] textMail = firebaseAuth.getCurrentUser().getEmail().split("@");
        String id = textMail[0];

        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = databaseReference.child("Users").child("Parent").child(id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                    name = snapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Request_Holiday_For_Kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parentmenu.this, parentreceiveholiday.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });


        Time_Table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parentmenu.this,parentviewtimetable.class);
                startActivity(intent);
            }
        });

        Send_Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(parentmenu.this, parentsendrequest.class);
                startActivity(intent);
            }
        });

        News.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(parentmenu.this, parentviewnews.class);
                startActivity(intent);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }
}