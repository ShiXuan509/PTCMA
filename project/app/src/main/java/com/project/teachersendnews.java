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

public class teachersendnews extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    EditText txtNews, teacherName1, txtDate1;
    Button send;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachersendnews);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.News);
        txtNews = findViewById(R.id.txtNews);
        teacherName1 = findViewById(R.id.teacherName1);
        txtDate1 = findViewById(R.id.txtDate1);
        send = findViewById(R.id.Send);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("News");
        firebaseStorage = FirebaseStorage.getInstance();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.News:
                        return true;
                    case R.id.Notices:
                        startActivity(new Intent(getApplicationContext(), TeacherNotices.class));
                        return true;
                    case R.id.Result:
                        startActivity(new Intent(getApplicationContext(), TeacherResult.class));
                        return true;
                }
                return false;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fn = txtNews.getText().toString().trim();
                String ln = teacherName1.getText().toString().trim();
                String yn = txtDate1.getText().toString().trim();

                if (fn.isEmpty() || ln.isEmpty() || yn.isEmpty()) {
                    Toast.makeText(teachersendnews.this, "Please complete the field", Toast.LENGTH_SHORT).show();
                } else {
                    String requestId = databaseReference.push().getKey();
                    NewsModel newsModel = new NewsModel();
                    newsModel.setNews(fn);
                    newsModel.setTeacherName1(ln);
                    newsModel.setDate1(yn);
                    newsModel.setId(requestId);

                    databaseReference.child(requestId).setValue(newsModel, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                Toast.makeText(teachersendnews.this, "News is Update Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(teachersendnews.this, parentviewnews.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(teachersendnews.this, "Failed Update...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    clearAll();
                }
            }
        });

    }

    private void clearAll() {
        txtNews.setText(" ");
        teacherName1.setText(" ");
        txtDate1.setText(" ");
    }
}