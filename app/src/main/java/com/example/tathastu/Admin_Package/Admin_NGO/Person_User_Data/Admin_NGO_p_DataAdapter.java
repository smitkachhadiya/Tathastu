package com.example.tathastu.Admin_Package.Admin_NGO.Person_User_Data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tathastu.R;

import java.util.List;

public class Admin_NGO_p_DataAdapter extends RecyclerView.Adapter<Admin_NGO_p_DataAdapter.EventViewHolder> {

    private final List<Admin_NGO_p_DataModel> userDataModelList;

    public Admin_NGO_p_DataAdapter(List<Admin_NGO_p_DataModel> userDataModelList) {
        this.userDataModelList = userDataModelList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_ngo_personal_info, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Admin_NGO_p_DataModel event = userDataModelList.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return userDataModelList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView fname1,address1,type1,mobile1,website1,email1,instagram1,linkedin1,facebook1,twitter1,youtube1;


        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            fname1=itemView.findViewById(R.id.fname);
            address1=itemView.findViewById(R.id.address);
            type1=itemView.findViewById(R.id.type);
            mobile1=itemView.findViewById(R.id.mobile);
            website1=itemView.findViewById(R.id.website);
            email1=itemView.findViewById(R.id.txt_NGO_email);
            instagram1=itemView.findViewById(R.id.instagram);
            linkedin1=itemView.findViewById(R.id.linkedin);
            facebook1=itemView.findViewById(R.id.facebook);
            twitter1=itemView.findViewById(R.id.twitter);
            youtube1=itemView.findViewById(R.id.youtube);
        }

        public void bind(Admin_NGO_p_DataModel event) {
            fname1.setText(event.getFname());
            address1.setText(event.getAddress());
            type1.setText(event.getType());
            mobile1.setText(event.getMobile());
            website1.setText(event.getWebsite());
            email1.setText(event.getEmail());
            instagram1.setText(event.getInstagram());
            linkedin1.setText(event.getLinkedin());
            facebook1.setText(event.getFacebook());
            twitter1.setText(event.getTwitter());
            youtube1.setText(event.getYoutube());

        }
    }
}
