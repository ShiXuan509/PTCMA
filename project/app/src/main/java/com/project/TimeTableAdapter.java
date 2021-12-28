package com.project;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.MissingResourceException;

public class TimeTableAdapter extends FirebaseRecyclerAdapter <TimeTableModel, TimeTableAdapter.viewholder> {

    Task<Void> myTask;
    DatabaseReference myRef;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TimeTableAdapter(@NonNull FirebaseRecyclerOptions<TimeTableModel> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull viewholder holder, int position, @NonNull TimeTableModel model) {
        holder.tvClass.setText(model.getClassName());
        holder.tvDescription.setText(model.getDescription());

        String imageUri = null;
        imageUri = model.getImage();
        Picasso.get().load(imageUri).fit().centerCrop().into(holder.rvTimeTable);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Confirm to delete?")
                        .setCancelable(false)
                        .setMessage("Delete data can't be undo?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myRef = FirebaseDatabase.getInstance().getReference().child("Time Table").child(model.getId());
                                myTask = myRef.removeValue();
                                myTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(view.getContext(), "deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(view.getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });

    }

    @NonNull
    @Override
    public TimeTableAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewtimetable,parent,false);
        return new viewholder(v);
    }

    class viewholder extends RecyclerView.ViewHolder{

        TextView tvDescription, tvClass;
        ImageView delete, rvTimeTable;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvClass = (TextView) itemView.findViewById(R.id.tvClass);
            delete = (ImageView) itemView.findViewById(R.id.delete_timeTable);
            rvTimeTable = (ImageView) itemView.findViewById(R.id.recyclerviewtimetable);
        }
    }
}
