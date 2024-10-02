package com.example.tathastu.User_Package.user_Event;

import static android.os.Build.VERSION_CODES.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tathastu.R;

import java.util.ArrayList;

public class eventListAdapter extends ArrayAdapter{
    private final Activity context;
    ArrayList<events> eventsList;

    public eventListAdapter( Activity context, ArrayList<events> eventsList) {
        super(context, com.example.tathastu.R.layout.card_event_notify,eventsList);
        this.context = context;
        this.eventsList = eventsList;

    }

    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        LayoutInflater inflater =context.getLayoutInflater();
        View rowview = inflater.inflate(com.example.tathastu.R.layout.card_event_notify,null,true);
        TextView enameText = (TextView) rowview.findViewById(com.example.tathastu.R.id.txt_eventname);
        TextView elocation = (TextView) rowview.findViewById(com.example.tathastu.R.id.txt_eventlocation);
        TextView edate = (TextView) rowview.findViewById(com.example.tathastu.R.id.txt_eventdate);
        TextView evparticipated = (TextView) rowview.findViewById(com.example.tathastu.R.id.txt_eparticipated);
        TextView evtotal = (TextView) rowview.findViewById(com.example.tathastu.R.id.txt_etotal);
        ImageView eimage= (ImageView) rowview.findViewById(com.example.tathastu.R.id.eventimage);

        events e = eventsList.get(position);

        enameText.setText(e.getName());
        elocation.setText(e.getAddress() + ", " + e.getCity());
        edate.setText(e.getDate());
        evparticipated.setText(e.getVolunteer_get());
        evtotal.setText(e.getTotal_volunteer());
        Glide.with(context).load(eventsList.get(position).getImageUrl()).into(eimage);
        return rowview;
    }
}
