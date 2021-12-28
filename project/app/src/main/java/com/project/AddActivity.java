package com.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private EditText name, email, title, password;
    private Button add, back;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name = (EditText) findViewById(R.id.txtName);
        email = (EditText) findViewById(R.id.txtEmail);
        title = (EditText) findViewById(R.id.txtTitle);
        password = (EditText) findViewById(R.id.txtPassword);
        add = (Button) findViewById(R.id.btnAdd);
        back= (Button) findViewById(R.id.btnBack);
        firebaseAuth = FirebaseAuth.getInstance();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString().trim();

                String userType = title.getText().toString();
                String[] textMail = email.getText().toString().split("@");
                String mail = textMail[0];

                Map<String, Object> map = new HashMap<>();
                map.put("name",name.getText().toString());
                map.put("email",email.getText().toString());
                map.put("title",title.getText().toString());
                map.put("password",password.getText().toString());

                firebaseAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String uid = firebaseAuth.getCurrentUser().getUid();

                            map.put("uid",uid);

                            if (userType.equals("teacher")) {
                                FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher").child(mail)
                                        .setValue(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                firebaseAuth.signOut();
                                                firebaseAuth.signInWithEmailAndPassword("hongshixuan1998@gmail.com", "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(AddActivity.this,"User Added Successfully", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddActivity.this,"Failed...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                FirebaseDatabase.getInstance().getReference().child("Users").child("Parent").child(mail)
                                        .setValue(map)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                firebaseAuth.signOut();
                                                firebaseAuth.signInWithEmailAndPassword("hongshixuan1998@gmail.com", "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(AddActivity.this,"User Added Successfully", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AddActivity.this,"Failed...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }else{
                            Toast.makeText(AddActivity.this,"Failed..." + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                clearAll();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void clearAll(){
        name.setText("");
        email.setText("");
        title.setText("");
        password.setText("");
    }
}