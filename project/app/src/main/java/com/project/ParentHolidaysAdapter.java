package com.project;

import android.app.AlertDialog;
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

import org.w3c.dom.Text;

public class ParentHolidaysAdapter extends FirebaseRecyclerAdapter <HolidaysModel, ParentHolidaysAdapter.viewholder> {
    Task<Void> myTask;
    DatabaseReference myRef;
    RecyclerView recyclerView;
    TextView text;
    String name;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     * @param recyclerView
     * @param emptyText
     */
    public ParentHolidaysAdapter(@NonNull FirebaseRecyclerOptions<HolidaysModel> options, RecyclerView recyclerView, TextView emptyText, String name) {
        super(options);
        this.recyclerView = recyclerView;
        this.text = emptyText;
        this.name = name;
    }


    @Override
    protected void onBindViewHolder(@NonNull viewholder holder, int position, @NonNull HolidaysModel model) {

        holder.reason.setText(model.getReason());
        holder.date.setText(model.getDate());
        holder.studentName.setText(model.getStudentName());

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
                                myRef = FirebaseDatabase.getInstance().getReference().child("Holidays Request").child(model.getId());
                                myTask = myRef.removeValue();
                                myTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        myRef = FirebaseDatabase.getInstance().getReference().child("Parent Holidays Request").child(name).child(model.getId());
                                        myTask = myRef.removeValue();
                                        myTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(view.getContext(), "deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });

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

    @Override
    public void onDataChanged() {
        if(getItemCount() == 0) {
            text.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            text.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewholidays,parent,false);
        return new viewholder(v);
    }

    class viewholder extends RecyclerView.ViewHolder{

        TextView studentName, date, reason;
        ImageView delete;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            studentName = (TextView) itemView.findViewById(R.id.studentName);
            date = (TextView) itemView.findViewById(R.id.date);
            reason = (TextView) itemView.findViewById(R.id.reason);
            delete = (ImageView) itemView.findViewById(R.id.delete_holidays);
        }
    }

}

