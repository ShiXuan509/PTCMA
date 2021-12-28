package com.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class teacherreplyrequest extends AppCompatActivity {

    SearchView searchView1;
    ListView userListView;
    ArrayAdapter arrayAdapter;
    ArrayList<User> Users = new ArrayList<>();
    ArrayList<String> username = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherreplyrequest);

        userListView = findViewById(R.id.userListListView);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        searchView1 = findViewById(R.id.search_name1);

        databaseReference.child("Users").child("Parent").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if (!dataSnapshot.child("name").getValue().toString().equals(firebaseAuth.getCurrentUser().getDisplayName())){
                            User user = dataSnapshot.getValue(User.class);
                            Users.add(user);

                            username.add(dataSnapshot.child("name").getValue().toString());
                        }
                    }
                    arrayAdapter = new ArrayAdapter(teacherreplyrequest.this, android.R.layout.simple_list_item_1, username);
                    userListView.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(teacherreplyrequest.this,"Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(teacherreplyrequest.this, teacherrequestchat.class);
                intent.putExtra("receiveruid", Users.get(i).getUid());
                intent.putExtra("name", Users.get(i).getName());
                startActivity(intent);
            }
        });

        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                teacherreplyrequest.this.arrayAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                teacherreplyrequest.this.arrayAdapter.getFilter().filter(s);
                return false;
            }
        });
    }
}