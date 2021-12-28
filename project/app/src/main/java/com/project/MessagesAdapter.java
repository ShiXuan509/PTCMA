package com.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        if (getItemViewType(position) == ITEM_SEND){
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.textViewmessage.setText(messages.getMessage());
            viewHolder.timeofmessage.setText(messages.getCurrenttime());
        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.textViewmessage.setText(messages.getMessage());
            viewHolder.timeofmessage.setText(messages.getCurrenttime());
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
        TextView timeofmessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage = itemView.findViewById(R.id.senderMsg);
            timeofmessage = itemView.findViewById(R.id.timeofMsg);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView textViewmessage;
        TextView timeofmessage;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage = itemView.findViewById(R.id.senderMsg);
            timeofmessage = itemView.findViewById(R.id.timeofMsg);
        }
    }

}
