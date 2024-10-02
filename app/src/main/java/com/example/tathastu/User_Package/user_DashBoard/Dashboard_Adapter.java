package com.example.tathastu.User_Package.user_DashBoard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Event.events;
import com.example.tathastu.User_Package.user_Event.user_Event_inDetails;

import java.util.ArrayList;
import java.util.List;

public class Dashboard_Adapter extends RecyclerView.Adapter<Dashboard_Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    Context context;
    private List<events> events_ad;

    Dashboard_Adapter(Context context, ArrayList<events> events_ad) {
        this.layoutInflater = LayoutInflater.from(context);
        this.events_ad = events_ad;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_event_notify,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        events events = events_ad.get(position);
        holder.enameText.setText(events.getName());
        holder.elocation.setText(events.getAddress() + ", " + events.getCity());
        holder.edate.setText(events.getDate());
        holder.evparticipated.setText(events.getVolunteer_get());
        holder.evtotal.setText(events.getTotal_volunteer());
        Glide.with(holder.itemView.getContext()).load(events_ad.get(position).getImageUrl()).into(holder.eimage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), user_Event_inDetails.class);
                intent.putExtra("key",events_ad.get(position).getKey());
                view.getContext().startActivity(intent);
                Animatoo.INSTANCE.animateSlideLeft(view.getContext());
            }
        });

    }

    @Override
    public int getItemCount() {
        return events_ad.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView enameText,elocation,edate,evparticipated,evtotal;
        ImageView eimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            enameText = (TextView) itemView.findViewById(com.example.tathastu.R.id.txt_eventname);
            elocation = (TextView) itemView.findViewById(com.example.tathastu.R.id.txt_eventlocation);
            edate = (TextView) itemView.findViewById(com.example.tathastu.R.id.txt_eventdate);
            evparticipated = (TextView) itemView.findViewById(com.example.tathastu.R.id.txt_eparticipated);
            evtotal = (TextView) itemView.findViewById(com.example.tathastu.R.id.txt_etotal);
            eimage= (ImageView) itemView.findViewById(com.example.tathastu.R.id.eventimage);

        }
    }

}
