package com.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class teachermenu extends AppCompatActivity {

    private Button Receive_Holiday;
    private Button Manage_Time_Table;
    private Button Send_News;
    private Button Reply;
    private Button Logout;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachermenu);

        Receive_Holiday = (Button) findViewById(R.id.btnReceiveHoliday);
        Manage_Time_Table = (Button) findViewById(R.id.btnManageTimeTable);
        Send_News = (Button) findViewById(R.id.btnSendNews);
        Reply = (Button) findViewById(R.id.btnReply);
        Logout = (Button) findViewById(R.id.btnLogout);
        firebaseAuth = FirebaseAuth.getInstance();

        Receive_Holiday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teachermenu.this, teacherreceiveholiday.class);
                startActivity(intent);
            }
        });

        Send_News.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teachermenu.this, teachersendnews.class);
                startActivity(intent);
            }
        });

        Manage_Time_Table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teachermenu.this, teachermanagetimetable.class);
                startActivity(intent);
            }
        });

        Reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(teachermenu.this, teacherreplyrequest.class);
                startActivity(intent);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }
}