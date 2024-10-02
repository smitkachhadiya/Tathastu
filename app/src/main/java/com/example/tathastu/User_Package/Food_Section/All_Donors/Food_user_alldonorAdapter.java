package com.example.tathastu.User_Package.Food_Section.All_Donors;

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
import com.example.tathastu.User_Package.Food_Section.History.food_user_model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


// Home screen/Home screen2 - camp card
public class Food_user_alldonorAdapter extends RecyclerView.Adapter<Food_user_alldonorAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<food_user_model> donor;


    Food_user_alldonorAdapter(Food_user_all_donors context, ArrayList<food_user_model> donor) {
        this.layoutInflater = LayoutInflater.from(context);
        this.donor = donor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.cardview_food_donor_request,viewGroup,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // bind the textview with data received
        food_user_model fuserModel = donor.get(i);
        viewHolder.textname.setText(fuserModel.getName());
        viewHolder.textnumber.setText(fuserModel.getMobile());
        viewHolder.textloc.setText(fuserModel.getAddress());
        viewHolder.textnote.setText(fuserModel.getDescription());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), Food_user_allDonors_indetails.class);
                i.putExtra("name", fuserModel.getName());
                i.putExtra("number", fuserModel.getMobile());
                i.putExtra("email", fuserModel.getEmail());
                i.putExtra("loc", fuserModel.getAddress());
                i.putExtra("note", fuserModel.getDescription());
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView textname,textnumber,textloc,textnote;
        FloatingActionButton BTN_food_donor_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            textname = itemView.findViewById(R.id.txt_food_donor_name);
            textnumber = itemView.findViewById(R.id.txt_food_donor_mno);
            textloc = itemView.findViewById(R.id.txt_food_donor_location);
            textnote = itemView.findViewById(R.id.txt_food_donor_fooddetails);
            BTN_food_donor_delete=itemView.findViewById(R.id.BTN_food_donor_delete);
            BTN_food_donor_delete.setVisibility(View.GONE);
//            itemView.setOnClickListener(this);
        }

    }
}



