package com.example.tathastu.NGO_Package.NGO_Campaign;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tathastu.R;

import java.util.ArrayList;

public class donationListAdapter extends ArrayAdapter {

    private final Activity contex;
    ArrayList<donation> donationList;

    public donationListAdapter(Activity context,ArrayList<donation> donationList){
        super(context, R.layout.cardview_campaign_donation_history,donationList);
        this.contex = context;
        this.donationList = donationList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =contex.getLayoutInflater();
        View rowview = inflater.inflate(R.layout.cardview_campaign_donation_history,null,true);
        TextView txt_username = (TextView) rowview.findViewById(R.id.txt_username);
        TextView txt_amount = (TextView) rowview.findViewById(R.id.txt_amount);
        TextView txt_transactionId = (TextView) rowview.findViewById(R.id.txt_transactionId);
        TextView txt_date = (TextView) rowview.findViewById(R.id.txt_date);

        donation d = donationList.get(position);
        txt_username.setText(d.getName());
        txt_amount.setText("₹"+d.getAmount());
        txt_transactionId.setText(d.getTransaction_id());
        txt_date.setText(d.getDate());
        return rowview;

    }
}
