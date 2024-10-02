package com.example.tathastu.User_Package.user_History;

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

public class UserAdapter_History_payment extends RecyclerView.Adapter<UserAdapter_History_payment.EventViewHolder> {

    private final List<UserModel_History_payment> paymentList;

    public UserAdapter_History_payment(List<UserModel_History_payment> paymentList) {
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_history_payment, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        UserModel_History_payment event = paymentList.get(position);
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

        public void bind(UserModel_History_payment event) {

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
