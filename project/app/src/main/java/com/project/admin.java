package com.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executor;

public class admin extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    private Button BtnLogin;
    private TextView Forgot_Password;
    FirebaseAuth firebaseAuth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Email = (EditText) findViewById(R.id.etAdminEmail);
        Password = (EditText) findViewById(R.id.etAdminPassword);
        BtnLogin = (Button) findViewById(R.id.btnAdminLogin);
        Forgot_Password = (TextView) findViewById(R.id.tvAdminForgotPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS: //means that we can use fingerprint
                //txtMsg.setText("You can use the fingerprint sensor to login");
                //txtMsg.setTextColor(Color.parseColor("#Fafafa"));
                //break;
                //case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE: //mean the device don't have fingerprint sensor
                //txtMsg.setText("The device don't have fingerprint sensor");
                //BtnLogin.setVisibility(View.GONE);
                //break;
                //case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                //txtMsg.setText("The biometric sensor is currently unavailable");
                //BtnLogin.setVisibility(View.GONE);
                //break;
                //case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                //txtMsg.setText("Your device don't have any fingerprint saved, please check your security settings!");
                //BtnLogin.setVisibility(View.GONE);
                //break;
        }

        //now that we have checked if we are able or not to use the biometric sensors
        //First create on executor
        Executor executor = ContextCompat.getMainExecutor(this);
        //now create the biometric prompt callback
        //this will give the result of the authentication and if we can login or not
        BiometricPrompt biometricPrompt = new BiometricPrompt(admin.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override //this method is called when there is an error while the authentication
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override //this method is called when the authentication is success
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

//                startActivity(new Intent(getApplicationContext(),adminmenu.class));
                firebaseAuth.signInWithEmailAndPassword(Email.getText().toString(),Password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                //login successful
                                String id = firebaseAuth.getCurrentUser().getUid();
                                myRef = FirebaseDatabase.getInstance().getReference().child("Admin").child(id);
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Toast.makeText(getApplicationContext(), "Login Successfully!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(),adminmenu.class));
                                            finish();
                                        } else {
                                            firebaseAuth.signOut();
                                            Toast.makeText(getApplicationContext(), "No user found.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) { Toast.makeText(admin.this,e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override // this method is called if we have failed the authentication
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        //now create biometric dialog box
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("Use your fingerprint to login")
                .setNegativeButtonText("Cancel")
                .build();

        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //extract & validate
                if(Email.getText().toString().isEmpty()){
                    Email.setError("Email is Required.");
                    Email.requestFocus();
                    return;
                }
                if (Password.getText().toString().isEmpty()){
                    Password.setError("Password is Required.");
                    Password.requestFocus();
                    return;
                }

                biometricPrompt.authenticate(promptInfo);
                //data is valid
                //login user

            }
        });
        Forgot_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResetPasswordDialog();
            }
        });
    }

    private void showResetPasswordDialog() {
        //Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password?");
        builder.setMessage("Enter Your Email to get Password Reset Link");
        //set layout
        LinearLayout linearLayout= new LinearLayout(this);
        //views to set in dialog
        EditText etEmail = new EditText(this);
        etEmail.setHint("Enter Your Email");
        etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(etEmail);
        linearLayout.setPadding(50,10,10,40);
        builder.setView(linearLayout);
        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //input email
                String email = etEmail.getText().toString().trim();
                beginReset(email);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss dialog
                dialogInterface.dismiss();
            }
        });
        // show dialog
        builder.create().show();
    }

    private void beginReset(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(admin.this, "Check Your Email to get Your Reset Link",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(admin.this,"Failed...",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // get and show proper error message
                Toast.makeText(admin.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),adminmenu.class));
            finish();
        }
    }

    private void validate (String userEmail, String userPassword){
        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(admin.this, "Please fill in your email and password", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent intent = new Intent(admin.this, adminmenu.class);
            startActivity(intent);
            return;
        }
    }
}