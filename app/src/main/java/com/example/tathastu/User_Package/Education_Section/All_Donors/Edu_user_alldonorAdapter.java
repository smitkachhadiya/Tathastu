package com.example.tathastu.User_Package.Education_Section.All_Donors;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.Education_Section.edu_user_model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


// Home screen/Home screen2 - camp card
public class Edu_user_alldonorAdapter extends RecyclerView.Adapter<Edu_user_alldonorAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<edu_user_model> donor;

//    private edu_user_model userModel ;


    Edu_user_alldonorAdapter(Edu_user_all_donors context, ArrayList<edu_user_model> donor) {
        this.layoutInflater = LayoutInflater.from(context);
        this.donor = donor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.cardview_edu_donor_details,viewGroup,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // bind the textview with data received
        edu_user_model data = donor.get(i);
        viewHolder.textname.setText(data.getName());
        viewHolder.textnumber.setText(data.getMobile());
        viewHolder.textloc.setText(data.getAddress());
        viewHolder.textnote.setText(data.getDescription());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), Edu_user_allDonors_indetails.class);
                i.putExtra("name", data.getName());
                i.putExtra("number", data.getMobile());
                i.putExtra("email", data.getEmail());
                i.putExtra("loc", data.getAddress());
                i.putExtra("note", data.getDescription());
                view.getContext().startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(view.getContext());
            }
        });
        // similarly you can set new image for each card and descriptions

    }

    @Override
    public int getItemCount() {
        return donor.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textname,textnumber,textloc,textnote;
        FloatingActionButton BTN_edu_donor_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            textname = itemView.findViewById(R.id.txt_edu_donor_name);
            textnumber = itemView.findViewById(R.id.txt_edu_donor_mno);
            textloc = itemView.findViewById(R.id.txt_edu_donor_location);
            textnote = itemView.findViewById(R.id.txt_edu_donor_details);
            BTN_edu_donor_delete=itemView.findViewById(R.id.BTN_edu_donor_delete);
            BTN_edu_donor_delete.setVisibility(View.GONE);
//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View view) {
//            int position = getAdapterPosition();
////            Intent i = new Intent(view.getContext(), Edu_user_allDonors_indetails.class);
////            i.putExtra("name", userModel.getName());
////            i.putExtra("number", userModel.getMobile());
////            i.putExtra("email", userModel.getEmail());
////            i.putExtra("loc", userModel.getAddress());
////            i.putExtra("note", userModel.getDescription());
////            view.getContext().startActivity(i);
//        }

    }
}



