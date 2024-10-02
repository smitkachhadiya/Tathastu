package com.example.tathastu.NGO_Package.NGO_History;

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

public class NGOAdapter_History_payment extends RecyclerView.Adapter<NGOAdapter_History_payment.EventViewHolder> {

    private final List<NGOModel_History_payment> paymentList;

    public NGOAdapter_History_payment(List<NGOModel_History_payment> paymentList) {
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_ngo_personal_history, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        NGOModel_History_payment event = paymentList.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView receivedFrom,amount,dateTime,email,mobile;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            receivedFrom=itemView.findViewById(R.id.receivedFrom);
            amount=itemView.findViewById(R.id.amount);
            dateTime=itemView.findViewById(R.id.dateTime);
            email=itemView.findViewById(R.id.email);
            mobile=itemView.findViewById(R.id.mobile);
        }

        public void bind(NGOModel_History_payment event) {
            Date date = new Date(Long.parseLong(event.getDateTime()) * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

            amount.setText("₹ "+ String.format("%.2f",Float.parseFloat(event.getAmount()) / 100));
            dateTime.setText(sdf.format(date).toString());
            receivedFrom.setText(event.getReceivedFrom());
            email.setText(event.getEmail());
            mobile.setText(event.getMobile());
        }
    }
}
