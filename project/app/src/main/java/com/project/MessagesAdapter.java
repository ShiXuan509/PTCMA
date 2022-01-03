package com.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 0;
    String sendername;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList, String sendername) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
        this.sendername = sendername;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND){
            View v = LayoutInflater.from(context).inflate(R.layout.senderchatlayout,parent,false);
            return new SenderViewHolder(v);
        }else {
            View v = LayoutInflater.from(context).inflate(R.layout.receiverchatlayout,parent,false);
            return new ReceiverViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages = messagesArrayList.get(position);
        String type = messagesArrayList.get(position).getType();

        if (getItemViewType(position) == ITEM_SEND){
            if(type.equals("text"))
            {
                SenderViewHolder viewHolder = (SenderViewHolder) holder;
                viewHolder.textViewmessage.setVisibility(View.VISIBLE);
                viewHolder.imageViewmessage.setVisibility(View.GONE);

                viewHolder.textViewmessage.setText(messages.getMessage());
                viewHolder.timeofmessage.setText(messages.getCurrenttime());
            }
            else
            {
                SenderViewHolder viewHolder = (SenderViewHolder) holder;
                viewHolder.textViewmessage.setVisibility(View.GONE);
                viewHolder.imageViewmessage.setVisibility(View.VISIBLE);

                //Picasso.get().load(messages.getMessage()).placeholder(R.drawable.ic_image).into(viewHolder.imageViewmessage);
                viewHolder.timeofmessage.setText(messages.getCurrenttime());
            }

        } else {

            if(type.equals("text"))
            {
                ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
                viewHolder.textViewmessage.setVisibility(View.VISIBLE);
                viewHolder.imageViewmessage.setVisibility(View.GONE);

                viewHolder.textViewmessage.setText(messages.getMessage());
                viewHolder.timeofmessage.setText(messages.getCurrenttime());
            }
            else
            {
                ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
                viewHolder.textViewmessage.setVisibility(View.GONE);
                viewHolder.imageViewmessage.setVisibility(View.VISIBLE);

                //Picasso.get().load(messages.getMessage()).placeholder(R.drawable.ic_image).into(viewHolder.imageViewmessage);
                viewHolder.timeofmessage.setText(messages.getCurrenttime());
            }
            /*ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.textViewmessage.setText(messages.getMessage());
            viewHolder.timeofmessage.setText(messages.getCurrenttime());*/
        }
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);

        if (sendername.equals(messages.getSenderName())){
            return ITEM_SEND;
        }else {
            return ITEM_RECEIVE;
        }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

//    public class myViewHolder extends RecyclerView.ViewHolder{
//
//        TextView textViewmessage;
//        TextView timeofmessage;
//
//        public myViewHolder(View view) {
//            super(view);
//
//            textViewmessage = itemView.findViewById(R.id.senderMsg);
//            timeofmessage = itemView.findViewById(R.id.timeofMsg);
//        }
//
//    }

    class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView textViewmessage;
        ImageView imageViewmessage;
        TextView timeofmessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage = itemView.findViewById(R.id.senderMsg);
            imageViewmessage = itemView.findViewById(R.id.senderImg);
            timeofmessage = itemView.findViewById(R.id.timeofMsg);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView textViewmessage;
        ImageView imageViewmessage;
        TextView timeofmessage;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage = itemView.findViewById(R.id.senderMsg);
            imageViewmessage = itemView.findViewById(R.id.senderImg);
            timeofmessage = itemView.findViewById(R.id.timeofMsg);
        }
    }

}
