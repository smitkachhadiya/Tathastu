package com.example.tathastu.Admin_Package.Admin_user.Person_User_Data;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tathastu.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Admin_person_History_DataAdapter extends RecyclerView.Adapter<Admin_person_History_DataAdapter.EventViewHolder> {

    private final List<Admin_person_History_DataModel> paymentList;

    public Admin_person_History_DataAdapter(List<Admin_person_History_DataModel> paymentList) {
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_user_personal_history, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Admin_person_History_DataModel event = paymentList.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView sentTo,method,amount,dateTime,status,transactionId;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            sentTo = itemView.findViewById(R.id.sentTo);
            method = itemView.findViewById(R.id.method);
            amount = itemView.findViewById(R.id.amount);
            dateTime = itemView.findViewById(R.id.dateTime);
            status = itemView.findViewById(R.id.status);
            transactionId = itemView.findViewById(R.id.transactionId);
        }

        public void bind(Admin_person_History_DataModel event) {

            Date date = new Date(Long.parseLong(event.getDateTime()) * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

            if(event.getStatus().equals("authorized"))
            {
                status.setText("Success");
            }else{
                status.setText(event.getStatus());
                status.setTextColor(Color.RED);
            }

            sentTo.setText(event.getSentTo());
            method.setText("Method : "+event.getMethod());
            amount.setText("₹ "+ String.format("%.2f",Float.parseFloat(event.getAmount()) / 100));
            dateTime.setText(sdf.format(date).toString());
            transactionId.setText("Transaction Id : "+event.getTransactionId());
        }
    }
}
