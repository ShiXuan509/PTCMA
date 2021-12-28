package com.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Calendar;

public class parentrequestholiday extends AppCompatActivity {

    private EditText Name, Reason, txtDate;
    private Button Send;
    private ImageView cal;
    private int mDate, mMonth, mYear;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parentrequestholiday);

        Name = (EditText) findViewById(R.id.etName);
        Reason = (EditText) findViewById(R.id.etReason);
        Send = (Button) findViewById(R.id.btnSend);
        txtDate = (EditText) findViewById(R.id.date);
        cal = (ImageView) findViewById(R.id.datepicker);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        String[] textMail = firebaseAuth.getCurrentUser().getEmail().split("@");
        String id = textMail[0];

        DatabaseReference myRef = databaseReference.child("Users").child("Parent").child(id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    name = snapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar Cal= Calendar.getInstance();
                mDate = Cal.get(Calendar.DATE);
                mMonth = Cal.get(Calendar.MONTH);
                mYear = Cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(parentrequestholiday.
                        this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        txtDate.setText(date + "/" + (month + 1) + "/" + year);
                    }
                },mYear, mMonth, mDate);
                datePickerDialog.show();

            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fn = Name.getText().toString().trim();
                String ln = Reason.getText().toString().trim();
                String pn = txtDate.getText().toString().trim();

                if (fn.isEmpty() || ln.isEmpty() || pn.isEmpty()){

                    Toast.makeText(parentrequestholiday.this, "Please complete the field", Toast.LENGTH_SHORT).show();
                }else {
                    String requestId = databaseReference.push().getKey();
                    HolidaysModel holidaysModel = new HolidaysModel();
                    holidaysModel.setStudentName(fn);
                    holidaysModel.setReason(ln);
                    holidaysModel.setDate(pn);
                    holidaysModel.setId(requestId);

                    databaseReference = firebaseDatabase.getReference();
                    databaseReference.child("Parent Holidays Request").child(name).child(requestId).setValue(holidaysModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                databaseReference.child("Holidays Request").child(requestId).setValue(holidaysModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(parentrequestholiday.this, "Holidays Request is Successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(parentrequestholiday.this, "Failed...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //databaseReference.child("").child(fn).child(requestId).setValue(holidaysModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    //@Override
                    //public void onSuccess(Void unused) {
//                            Toast.makeText(parentrequestholiday.this, "Holidays Request is Successfully", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent (parentrequestholiday.this, teacherreceiveholiday.class);
//                            startActivity(intent);
//                            startActivity(new Intent(getApplicationContext(),parentrequestholiday.class));
//                            finish();
                    // }
                    // }).addOnFailureListener(new OnFailureListener() {
                    // @Override
                    //public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(parentrequestholiday.this, "Failed...", Toast.LENGTH_SHORT).show();
                    // }
                    //});
                    clearAll();
                }
            }
        });
    }

    private void clearAll() {
        txtDate.setText(" ");
        Name.setText(" ");
        Reason.setText(" ");
    }
}