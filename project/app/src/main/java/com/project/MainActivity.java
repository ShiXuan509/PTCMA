package com.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference myRef;

    private Button Parent;
    private Button Teacher;
    private Button Admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parent = (Button) findViewById(R.id.btnParent);
        Teacher = (Button) findViewById(R.id.btnTeacher);
        Admin = (Button) findViewById(R.id.btnAdmin);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            String[] textMail = auth.getCurrentUser().getEmail().split("@");
            String id = textMail[0];

            myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher").child(id);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Teacher.setEnabled(true);
                        Parent.setEnabled(false);
                        Admin.setEnabled(false);
                    } else {
                        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Parent").child(id);
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Teacher.setEnabled(false);
                                    Parent.setEnabled(true);
                                    Admin.setEnabled(false);
                                } else {
                                    Teacher.setEnabled(false);
                                    Parent.setEnabled(false);
                                    Admin.setEnabled(true);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Teacher.setEnabled(true);
            Parent.setEnabled(true);
            Admin.setEnabled(true);
        }

        Parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, parent.class);
                startActivity(intent);
            }
        });


        Teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, teacher.class);
                startActivity(intent);
            }
        });


        Admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, admin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (auth.getCurrentUser() != null) {
            String[] textMail = auth.getCurrentUser().getEmail().split("@");
            String id = textMail[0];

            myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher").child(id);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Teacher.setEnabled(true);
                        Parent.setEnabled(false);
                        Admin.setEnabled(false);
                    } else {
                        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Parent").child(id);
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    Teacher.setEnabled(false);
                                    Parent.setEnabled(true);
                                    Admin.setEnabled(false);
                                } else {
                                    Teacher.setEnabled(false);
                                    Parent.setEnabled(false);
                                    Admin.setEnabled(true);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Teacher.setEnabled(true);
            Parent.setEnabled(true);
            Admin.setEnabled(true);
        }
    }
}