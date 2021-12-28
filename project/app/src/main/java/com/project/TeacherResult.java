package com.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class TeacherResult extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton imageButtonResult;
    EditText txtResult, teacherName3, txtDate3;
    Button sendResult;

    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private static final int Gallery_Code = 1;
    Uri imageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_result);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.Result);

        imageButtonResult = findViewById(R.id.imageButtonResult);
        txtResult = findViewById(R.id.txtResult);
        sendResult = findViewById(R.id.sendResult);
        teacherName3 = findViewById(R.id.teacherName3);
        txtDate3 = findViewById(R.id.txtDate3);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Result");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.News:
                        startActivity(new Intent(getApplicationContext(), teachersendnews.class));
                        return true;

                    case R.id.Notices:
                        startActivity(new Intent(getApplicationContext(), TeacherNotices.class));
                        return true;

                    case  R.id.Result:
                        return true;
                }
                return false;
            }
        });

        imageButtonResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Gallery_Code);
            }
        });
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, requestCode, data);

        if (requestCode == Gallery_Code && resultCode == RESULT_OK){
            imageUrl = data.getData();
            imageButtonResult.setImageURI(imageUrl);
        }

        sendResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fn = txtResult.getText().toString().trim();
                String ln = teacherName3.getText().toString().trim();
                String yn = txtDate3.getText().toString().trim();

                if (!(fn.isEmpty() && ln.isEmpty() && yn.isEmpty() && imageUrl != null)){
                    StorageReference filepath = firebaseStorage.getReference().child("imagePost").child(imageUrl.getLastPathSegment());
                    filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String t=task.getResult().toString();
                                    String requestId = databaseReference.push().getKey();

                                    DatabaseReference newPost = databaseReference.child(requestId);

                                    newPost.child("text").setValue(fn);
                                    newPost.child("teacherName").setValue(ln);
                                    newPost.child("date").setValue(yn);
                                    newPost.child("image").setValue(t);
                                    newPost.child("id").setValue(requestId);

                                    Toast.makeText(TeacherResult.this, "Result is Update Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(TeacherResult.this, ParentResult.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                    clearAll();
                }
            }
        });
    }

    private void clearAll() {
        txtResult.setText(" ");
        imageButtonResult.setImageURI(Uri.parse(" "));
        teacherName3.setText(" ");
        txtDate3.setText(" ");
    }
}