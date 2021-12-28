package com.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class teachermanagetimetable extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    private ImageButton imageButton;
    private EditText etclass, description;
    private Button btninsert;

    private static final int Gallery_Code=1;
    Uri imageUrl = null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachermanagetimetable);

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        etclass = (EditText) findViewById(R.id.etClass);
        description = (EditText) findViewById(R.id.etDescription);
        btninsert = (Button) findViewById(R.id.btnInsert);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Time Table");
        firebaseStorage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,Gallery_Code);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Gallery_Code && resultCode==RESULT_OK){
            imageUrl = data.getData();
            imageButton.setImageURI(imageUrl);
        }

        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String requestId = databaseReference.push().getKey();
                String fn=etclass.getText().toString().trim();
                String ln=description.getText().toString().trim();

                if (!(fn.isEmpty() && ln.isEmpty() && imageUrl!=null)){
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();

                    StorageReference filepath= firebaseStorage.getReference().child("imagePost").child(imageUrl.getLastPathSegment());
                    filepath.putFile(imageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String t=task.getResult().toString();

                                    DatabaseReference newPost = databaseReference.child(requestId);

                                    newPost.child("id").setValue(requestId);
                                    newPost.child("className").setValue(fn);
                                    newPost.child("description").setValue(ln);
                                    newPost.child("image").setValue(t);
                                    progressDialog.dismiss();

                                    Intent intent = new Intent(teachermanagetimetable.this, parentviewtimetable.class);
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
        etclass.setText(" ");
        description.setText(" ");
        imageButton.setImageURI(Uri.parse(" "));
    }
}