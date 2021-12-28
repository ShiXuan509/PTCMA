package com.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NoticesAdapter extends FirebaseRecyclerAdapter <NoticesModel, NoticesAdapter.viewholder> {

    Task<Void> myTask;
    DatabaseReference myRef;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoticesAdapter(@NonNull FirebaseRecyclerOptions<NoticesModel> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull viewholder holder, int position, @NonNull NoticesModel model) {

        holder.tvNotices.setText(model.getNotices());
        holder.tvTeacherName2.setText(model.getTeacherName2());
        holder.tvDate2.setText(model.getDate2());
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
                                myRef = FirebaseDatabase.getInstance().getReference().child("Notices").child(model.getId());
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
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewnotices,parent, false);
        return new viewholder(v);
    }

    class viewholder extends RecyclerView.ViewHolder{

        TextView tvNotices, tvDate2, tvTeacherName2;
        ImageView delete;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            tvNotices = (TextView) itemView.findViewById(R.id.tvNotices);
            tvDate2 = (TextView) itemView.findViewById(R.id.tvDate2);
            tvTeacherName2 = (TextView) itemView.findViewById(R.id.tvTeacherName2);
            delete = (ImageView) itemView.findViewById(R.id.delete_notices);
        }
    }
}

