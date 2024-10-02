package com.example.tathastu.NGO_Package.NGO_Event;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tathastu.R;

import java.util.ArrayList;

public class volunteerListAdapter extends ArrayAdapter {
    private final Activity contex;
    ArrayList<volunteer> volunteerList;

    public volunteerListAdapter(Activity context,ArrayList<volunteer> volunteerList){
        super(context, R.layout.cardview_ngo_volunteer_list,volunteerList);
        this.contex = context;
        this.volunteerList = volunteerList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =contex.getLayoutInflater();
        View rowview = inflater.inflate(R.layout.cardview_ngo_volunteer_list,null,true);
        TextView txt_username = (TextView) rowview.findViewById(R.id.txt_username);
        TextView txt_email = (TextView) rowview.findViewById(R.id.txt_email);
        TextView txt_contact = (TextView) rowview.findViewById(R.id.txt_contact);
        TextView txt_age = (TextView) rowview.findViewById(R.id.txt_age);
        TextView txt_id= (TextView) rowview.findViewById(R.id.txt_id);

        volunteer v = volunteerList.get(position);
        txt_username.setText(v.getName());
        txt_email.setText(v.getEmail());
        txt_contact.setText(v.getContact_no());
        txt_age.setText(v.getAge());
        txt_id.setText(v.getVid());
        return rowview;

    }

}
