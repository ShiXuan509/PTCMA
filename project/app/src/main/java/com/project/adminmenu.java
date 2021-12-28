package com.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class adminmenu extends AppCompatActivity {

    private Button AddDeleteParent, AddDeleteTchr;
    private Button Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminmenu);

        AddDeleteParent = (Button) findViewById(R.id.btnAddDeleteParent);
        AddDeleteTchr = (Button) findViewById(R.id.btnAddDeleteTeacher);
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

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }
}