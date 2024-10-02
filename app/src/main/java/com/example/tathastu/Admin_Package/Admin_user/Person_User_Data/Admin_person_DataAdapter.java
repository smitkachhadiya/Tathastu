package com.example.tathastu.Admin_Package.Admin_user.Person_User_Data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tathastu.R;

import java.util.List;

public class Admin_person_DataAdapter extends RecyclerView.Adapter<Admin_person_DataAdapter.EventViewHolder> {

    private final List<Admin_person_DataModel> userDataModelList;

    public Admin_person_DataAdapter(List<Admin_person_DataModel> userDataModelList) {
        this.userDataModelList = userDataModelList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_user_personal_info, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Admin_person_DataModel event = userDataModelList.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return userDataModelList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView txt_user_name;
        private AppCompatTextView txt_user_mno;
        private AppCompatTextView txt_user_email;
        private AppCompatTextView txt_user_dob;



        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            txt_user_mno = itemView.findViewById(R.id.txt_user_mno);
            txt_user_email = itemView.findViewById(R.id.txt_user_email);
            txt_user_dob = itemView.findViewById(R.id.txt_user_dob);
        }

        public void bind(Admin_person_DataModel event) {
            txt_user_name.setText(event.getFname()+" "+event.getLname());
            txt_user_mno.setText(event.getMobile());
            txt_user_email.setText(event.getEmail());
            txt_user_dob.setText(event.getDob());
        }
    }
}
