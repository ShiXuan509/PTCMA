package com.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.ColorSpace;
import android.media.Image;
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

public class NewsAdapter extends FirebaseRecyclerAdapter <NewsModel,NewsAdapter.viewholder> {
    Task<Void> myTask;
    DatabaseReference myRef;

    public NewsAdapter(@NonNull FirebaseRecyclerOptions<NewsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewholder holder, int position, @NonNull NewsModel model) {

        holder.tvNews.setText(model.getNews());
        holder.tvTeacherName1.setText(model.getTeacherName1());
        holder.tvDate1.setText(model.getDate1());

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
                                myRef = FirebaseDatabase.getInstance().getReference().child("News").child(model.getId());
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewnews,parent,false);
        return new viewholder(v);
    }

    class viewholder extends RecyclerView.ViewHolder{

        TextView tvNews, tvDate1, tvTeacherName1;
        ImageView delete;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            tvNews = (TextView) itemView.findViewById(R.id.tvNews);
            tvDate1 = (TextView) itemView.findViewById(R.id.tvDate1);
            tvTeacherName1= (TextView) itemView.findViewById(R.id.tvTeacherName1);
            delete = (ImageView) itemView.findViewById(R.id.delete_news);
        }
    }


}
