package com.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class adminmenu extends AppCompatActivity {

    private Button AddDeleteParent, AddDeleteTchr, SchoolLocationManagement;
    private Button Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminmenu);

        AddDeleteParent = (Button) findViewById(R.id.btnAddDeleteParent);
        AddDeleteTchr = (Button) findViewById(R.id.btnAddDeleteTeacher);
        SchoolLocationManagement = (Button) findViewById(R.id.btnSchoolLocation);
        Logout = (Button) findViewById(R.id.btnAdminLogout);

        AddDeleteTchr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminmenu.this, admindeleteteacher.class);
                startActivity(intent);
            }
        });

        AddDeleteParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminmenu.this, admindeleteparent.class);
                startActivity(intent);
            }
        });

        SchoolLocationManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminmenu.this, adminLocationManagement.class);
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