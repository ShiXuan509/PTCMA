package com.project;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class ParentAdapter extends FirebaseRecyclerAdapter <User,ParentAdapter.myviewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ParentAdapter(@NonNull FirebaseRecyclerOptions<User> options) {

        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull User model) {
        holder.name.setText(model.getName());
        holder.title.setText(model.getTitle());
        holder.email.setText(model.getEmail());
        holder.password.setText(model.getPassword());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.name.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1200)
                        .create();

                //dialogPlus.show();

                View v = dialogPlus.getHolderView();

                EditText name = v.findViewById(R.id.txtName);
                EditText email = v.findViewById(R.id.txtEmail);
                EditText title = v.findViewById(R.id.txtTitle);
                EditText password = v.findViewById(R.id.txtPassword);

                Button update = v.findViewById(R.id.btnUpdate);

                name.setText(model.getName());
                email.setText(model.getEmail());
                title.setText(model.getTitle());
                password.setText(model.getPassword());

                dialogPlus.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("name",name.getText().toString());
                        map.put("email",email.getText().toString());
                        map.put("title",title.getText().toString());
                        map.put("password",password.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Users").child("Parent")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.name.getContext(), "User Update Successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.name.getContext(),"Error While Updating",Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Are You Sure?");
                builder.setMessage("Delete data can't be undo!");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child("Parent")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.name.getContext(),"Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admindeleteadd, parent, false);
        return new myviewHolder(view);
    }

    class myviewHolder extends RecyclerView.ViewHolder {

        TextView name, title, email, password;

        Button edit, delete;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.addName);
            title = (TextView) itemView.findViewById(R.id.addTitle);
            email = (TextView) itemView.findViewById(R.id.addEmail);
            password = (TextView) itemView.findViewById(R.id.addPassword);

            edit = (Button) itemView.findViewById(R.id.btnEdit);
            delete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}
