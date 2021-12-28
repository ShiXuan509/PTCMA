package com.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class TeacherNotices extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    EditText txtNotices, teacherName2, date2;
    Button sendNotices;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_notices);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.Notices);
        txtNotices = findViewById(R.id.txtNotices);
        sendNotices = findViewById(R.id.sendNotices);
        teacherName2 = findViewById(R.id.teacherName2);
        date2 = findViewById(R.id.txtDate2);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Notices");
        firebaseStorage = FirebaseStorage.getInstance();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.News:
                        startActivity(new Intent(getApplicationContext(), teachersendnews.class));
                        return true;

                    case R.id.Notices:
                        return true;

                    case  R.id.Result:
                        startActivity(new Intent(getApplicationContext(), TeacherResult.class));
                        return true;
                }
                return false;
            }
        });

        sendNotices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fn = txtNotices.getText().toString().trim();
                String ln = teacherName2.getText().toString().trim();
                String yn = date2.getText().toString().trim();

                if (fn.isEmpty()){
                    Toast.makeText(TeacherNotices.this, "Please complete the field", Toast.LENGTH_SHORT).show();
                }else {
                    String requestId = databaseReference.push().getKey();
                    NoticesModel noticesModel = new NoticesModel();
                    noticesModel.setNotices(fn);
                    noticesModel.setTeacherName2(ln);
                    noticesModel.setDate2(yn);
                    noticesModel.setId(requestId);

                    databaseReference.child(requestId).setValue(noticesModel, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null){
                                Toast.makeText(TeacherNotices.this, "Notices is Update Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TeacherNotices.this, ParentNotices.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(TeacherNotices.this, "Failed Update...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    clearAll();
                }
            }
        });
    }

    private void clearAll() {

        txtNotices.setText(" ");
        teacherName2.setText(" ");
        date2.setText(" ");
    }
}