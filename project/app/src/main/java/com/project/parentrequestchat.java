package com.project;

import static android.provider.MediaStore.Images.Media.getBitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class parentrequestchat extends AppCompatActivity {

    EditText getmessage;
    ImageView btnsendmsg, btnBack,btnUpload;
    TextView roomTitle;
    //androidx.appcompat.widget.Toolbar toolbarofspecificchat;
    //TextView nameofspecificuser;

    private String enteredmessage;
    Intent intent;
    String receivername, sendername, receiveruid, senderuid;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    String senderroom, receiverroom;

    private static final int Gallery_Code=1;
    private static final int Camera_Code=2;

    //String[] cameraPermissions;
    //String[] storagePermissions;

    RecyclerView messagerecyclerview;
    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    MessagesAdapter messagesAdapter;
    ArrayList<Messages> messagesArrayList;

    Uri fileUri=null;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentrequestchat);

        roomTitle = findViewById(R.id.chatUser);
        getmessage = findViewById(R.id.message);
        btnsendmsg = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
        btnUpload=findViewById(R.id.btnUpload);

        intent = getIntent();
        messagesArrayList = new ArrayList<>();
        messagerecyclerview = findViewById(R.id.recyclerView);

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm");

        storageReference = FirebaseStorage.getInstance().getReference();

        senderuid = firebaseAuth.getCurrentUser().getUid();
        String[] senderEmail = firebaseAuth.getCurrentUser().getEmail().split("@");
        String senderMail = senderEmail[0];


        receiveruid = getIntent().getStringExtra("receiveruid");
        receivername = getIntent().getStringExtra("name");

        roomTitle.setText(receivername);

        senderroom = senderuid+receiveruid;
        receiverroom = receiveruid+senderuid;

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromCamera();
            }
        });


        btnsendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredmessage = getmessage.getText().toString();
                if (enteredmessage.isEmpty()){
                    Toast.makeText(getApplicationContext(), "No Message", Toast.LENGTH_SHORT).show();
                }else{
                    Date date = new Date();
                    currenttime = simpleDateFormat.format(calendar.getTime());
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Parent").child(senderMail);
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                sendername = snapshot.child("name").getValue().toString();
                                Messages messages = new Messages(enteredmessage,"text",sendername,receivername,date.getTime(),currenttime);
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                        .child(senderroom);
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                                    .child(senderroom)
                                                    .child("messages")
                                                    .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    getmessage.setText(null);
                                                }
                                            });
                                        } else {
                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                                    .child(receiverroom);
                                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()){
                                                        FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                                                .child(receiverroom)
                                                                .child("messages")
                                                                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                getmessage.setText(null);
                                                            }
                                                        });
                                                    } else {
                                                        FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                                                .child(senderroom)
                                                                .child("messages")
                                                                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                getmessage.setText(null);
                                                            }
                                                        });
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messagerecyclerview.setHasFixedSize(true);
        messagerecyclerview.setLayoutManager(linearLayoutManager);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Parent").child(senderMail);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    sendername = snapshot.child("name").getValue().toString();
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("Parent Send Request and Inquiry").child(senderroom).child("messages");
                    messagesAdapter = new MessagesAdapter(parentrequestchat.this,messagesArrayList, sendername);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                messagesArrayList.clear();
                                for (DataSnapshot snapshot1 : snapshot.getChildren())
                                {
                                    Messages messages = snapshot1.getValue(Messages.class);
                                    messagesArrayList.add(messages);
                                }
                                messagesAdapter.notifyDataSetChanged();
                            } else {
                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Parent Send Request and Inquiry").child(receiverroom).child("messages");
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            messagesArrayList.clear();
                                            for (DataSnapshot snapshot1 : snapshot.getChildren())
                                            {
                                                Messages messages = snapshot1.getValue(Messages.class);
                                                messagesArrayList.add(messages);
                                            }
                                            messagesAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    messagerecyclerview.setAdapter(messagesAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagesAdapter = new MessagesAdapter(parentrequestchat.this,messagesArrayList, sendername);
        messagerecyclerview.setAdapter(messagesAdapter);

        messagesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                messagerecyclerview.smoothScrollToPosition(messagesAdapter.getItemCount());
            }
        });
    }

    private void pickFromCamera()
    {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Photo");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your photo");
        fileUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values
        );
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        startActivityForResult(intent,Camera_Code);
    }

    private void sendImageMessage(String file){
        String[] senderEmail = firebaseAuth.getCurrentUser().getEmail().split("@");
        String senderMail = senderEmail[0];
        enteredmessage= file;
            if (file.isEmpty()){
                Toast.makeText(getApplicationContext(), "No Message", Toast.LENGTH_SHORT).show();
            }else{
                Date date = new Date();
                currenttime = simpleDateFormat.format(calendar.getTime());
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Parent").child(senderMail);
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            sendername = snapshot.child("name").getValue().toString();
                            Messages messages = new Messages(enteredmessage,"image",sendername,receivername,date.getTime(),currenttime);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                    .child(senderroom);
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                                .child(senderroom)
                                                .child("messages")
                                                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                getmessage.setText(null);
                                            }
                                        });
                                    } else {
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                                .child(receiverroom);
                                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                                            .child(receiverroom)
                                                            .child("messages")
                                                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            getmessage.setText(null);
                                                        }
                                                    });
                                                } else {
                                                    FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                                            .child(senderroom)
                                                            .child("messages")
                                                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            getmessage.setText(null);
                                                        }
                                                    });
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            /*StorageReference filepath= firebaseStorage.getReference().child("imagePost").child(fileUri.getLastPathSegment());
            filepath.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            enteredmessage=task.getResult().toString();
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Parent").child(senderMail);
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        sendername = snapshot.child("name").getValue().toString();
                                        Messages messages = new Messages(enteredmessage,"image",sendername,receivername,date.getTime(),currenttime);
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                                .child(senderroom);
                                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                                            .child(senderroom)
                                                            .child("messages")
                                                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            getmessage.setText(null);
                                                        }
                                                    });
                                                } else {
                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                                            .child(receiverroom);
                                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()){
                                                                FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                                                        .child(receiverroom)
                                                                        .child("messages")
                                                                        .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        getmessage.setText(null);
                                                                    }
                                                                });
                                                            } else {
                                                                FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                                                        .child(senderroom)
                                                                        .child("messages")
                                                                        .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        getmessage.setText(null);
                                                                    }
                                                                });
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            progressDialog.dismiss();

                        }
                    });
                }
            });*/
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            if(requestCode== Camera_Code){
                String file = null;
                try {
                    ParcelFileDescriptor parcelFileDescriptor =
                            getContentResolver().openFileDescriptor(fileUri, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    byte[] byteFormat = stream.toByteArray();
                    String encodedImage = android.util.Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                    file = encodedImage;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendImageMessage(file);

            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (messagesAdapter != null){
            messagesAdapter.notifyDataSetChanged();
        }
    }
}