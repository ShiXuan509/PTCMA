package com.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class parentrequestchat extends AppCompatActivity {

    EditText getmessage;
    ImageView btnsendmsg, btnBack;
    TextView roomTitle;
    //androidx.appcompat.widget.Toolbar toolbarofspecificchat;
    //TextView nameofspecificuser;

    private String enteredmessage;
    Intent intent;
    String receivername, sendername, receiveruid, senderuid;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String senderroom, receiverroom;

    RecyclerView messagerecyclerview;
    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    MessagesAdapter messagesAdapter;
    ArrayList<Messages> messagesArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentrequestchat);

        roomTitle = findViewById(R.id.chatUser);
        getmessage = findViewById(R.id.message);
        btnsendmsg = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
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
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Parent").child(senderMail);
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                sendername = snapshot.child("name").getValue().toString();
                                Messages messages = new Messages(enteredmessage,sendername,receivername,date.getTime(),currenttime);
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