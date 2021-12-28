package com.project;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ResultAdapter extends FirebaseRecyclerAdapter <ResultModel, ResultAdapter.viewholder> {

    Task<Void> myTask;
    DatabaseReference myRef;


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ResultAdapter(@NonNull FirebaseRecyclerOptions<ResultModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ResultAdapter.viewholder holder, int position, @NonNull ResultModel model) {
        holder.tvText.setText(model.getText());
        holder.tvTeacherName3.setText(model.getTeacherName());
        holder.tvDate3.setText(model.getDate());

        String imageUri = null;
        imageUri = model.getImage();
        Picasso.get().load(imageUri).fit().centerCrop().into(holder.rvResult);

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
                                myRef = FirebaseDatabase.getInstance().getReference().child("Result").child(model.getId());
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewresult,parent,false);
        return new viewholder(v);
    }

    class viewholder extends RecyclerView.ViewHolder{

        TextView tvText, tvDate3, tvTeacherName3;
        ImageView delete, rvResult;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            tvText = (TextView) itemView.findViewById(R.id.tvText);
            tvDate3 = (TextView) itemView.findViewById(R.id.tvDate3);
            tvTeacherName3= (TextView) itemView.findViewById(R.id.tvTeacherName3);
            rvResult = (ImageView) itemView.findViewById(R.id.recyclerviewresult);
            delete = (ImageView) itemView.findViewById(R.id.delete_result);
        }
    }
}