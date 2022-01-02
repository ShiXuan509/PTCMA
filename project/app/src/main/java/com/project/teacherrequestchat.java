package com.project;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class teacherrequestchat extends AppCompatActivity {

    TextView roomTitle;
    EditText getmessage;
    ImageView btnsendmsg, btnBack, btnUpload,img_preview;
    //androidx.appcompat.widget.Toolbar toolbarofspecificchat;
    //TextView nameofspecificuser;

    String enteredmessage;
    Intent intent;
    String receivername, sendername, receiveruid, senderuid;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String senderroom, receiverroom;

    FirebaseStorage firebaseStorage;
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

    Uri fileUri= null;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherrequestchat);

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

        btnsendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredmessage = getmessage.getText().toString();
                if (enteredmessage.isEmpty()){
                    Toast.makeText(getApplicationContext(), "No Message", Toast.LENGTH_SHORT).show();
                }else{
                    Date date = new Date();
                    currenttime = simpleDateFormat.format(calendar.getTime());
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher").child(senderMail);
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

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromCamera();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messagerecyclerview.setHasFixedSize(true);
        messagerecyclerview.setLayoutManager(linearLayoutManager);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher").child(senderMail);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    sendername = snapshot.child("name").getValue().toString();
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("Parent Send Request and Inquiry").child(senderroom).child("messages");
                    messagesAdapter = new MessagesAdapter(teacherrequestchat.this,messagesArrayList, sendername);
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

        messagesAdapter = new MessagesAdapter(teacherrequestchat.this,messagesArrayList, sendername);
        messagerecyclerview.setAdapter(messagesAdapter);

        messagesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            public void onItemRangeInserted(int positionStart, int itemCount) {
                messagerecyclerview.smoothScrollToPosition(messagesAdapter.getItemCount());
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Camera_Code)
        {
            if(resultCode == RESULT_OK)
            {
                try {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                            getContentResolver(),fileUri
                    );
                    img_preview.setImageBitmap(thumbnail);
                    img_preview.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }catch (IOException e){

                }
            }
        }
    }*/

    /*private void showImagePickDialog()
    {
        String[] options ={"Camera", "Gallery"};

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Choose image from");
        builder.setItems(options,((dialog, which) -> {
            if(which==0)
            {
                if(!checkCameraPermission())
                {
                    requestCameraPermission();
                }else{
                    pickFromCamera();
                }
            }
            if(which==1)
            {
                if(!checkStoragePermission())
                {
                    requestStoragePermission();
                }else
                {
                    pickFromGallery();
                }
            }
        }));
    }
    private void pickFromGallery()
    {
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Gallery_Code);
    }


    private boolean checkStoragePermission()
    {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(this, storagePermissions, Camera_Code);
    }

    private boolean checkCameraPermission()
    {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission()
    {
        ActivityCompat.requestPermissions(this, cameraPermissions, Camera_Code);
    }*/

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

    private void sendImageMessage(Uri fileUri){
        String[] senderEmail = firebaseAuth.getCurrentUser().getEmail().split("@");
        String senderMail = senderEmail[0];

        ProgressDialog progressDialog= new ProgressDialog(this);

        if (fileUri!=null){
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            Date date = new Date();
            currenttime = simpleDateFormat.format(calendar.getTime());

            StorageReference filepath= firebaseStorage.getReference().child("imagePost").child(fileUri.getLastPathSegment());
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
            });
        }
    }

    /*private void sendImageMessage(Uri fileUri){
        ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Sending image..");
        progressDialog.show();

        Date date = new Date();
        String timeStamp =""+System.currentTimeMillis();
        String fileNameAndPath= "Parent Send Request and Inquiry";

        Bitmap bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),fileUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte[] data =baos.toByteArray();
        StorageReference ref = FirebaseStorage.getInstance().getReference().child(fileNameAndPath);
        ref.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        String downloadUri =uriTask.getResult().toString();

                        if(uriTask.isSuccessful())
                        {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            Messages messages = new Messages(enteredmessage,"image",sendername,receivername,date.getTime(),currenttime);
                            HashMap<String,Object> hashMap = new HashMap<>();
                            //hashMap.put("sender", );
                            hashMap.put("message", downloadUri);
                            hashMap.put("timestamp", timeStamp);
                            hashMap.put("type","image");
                            databaseReference.child("Chats").push().setValue(hashMap);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Parent Send Request and Inquiry")
                                    .child(senderroom);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            if(requestCode== Camera_Code){
                fileUri = data.getData();
                sendImageMessage(fileUri);

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