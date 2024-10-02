package com.example.tathastu.User_Package.Education_Section.Camp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.NGO_Package.NGO_Food_Camp.History.NGO_food_History_model;
import com.example.tathastu.R;

import java.util.ArrayList;
import java.util.List;


// Home screen/Home screen2 - camp card
public class Edu_Camp_Adapter extends RecyclerView.Adapter<Edu_Camp_Adapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<NGO_food_History_model> donor;


    Edu_Camp_Adapter(Edu_Donation_Camp context, ArrayList<NGO_food_History_model> donor){
        this.layoutInflater = LayoutInflater.from(context);
        this.donor = donor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.cardview_user_edu_camp,viewGroup,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // bind the textview with data received
        NGO_food_History_model foodHistoryModel = donor.get(i);
        viewHolder.textname.setText(foodHistoryModel.getNgo_name());
        viewHolder.textsdate.setText(foodHistoryModel.getStart_date());
        viewHolder.textedate.setText(foodHistoryModel.getEnd_date());
        viewHolder.textmno.setText(foodHistoryModel.getMobile());
        viewHolder.textlocation .setText(foodHistoryModel.getC_address());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), Edu_Camp_Indetails.class);
                i.putExtra("title", foodHistoryModel.getNgo_name());
                i.putExtra("sdate", foodHistoryModel.getStart_date());
                i.putExtra("edate", foodHistoryModel.getEnd_date());
                i.putExtra("loc", foodHistoryModel.getC_address());
                i.putExtra("mno", foodHistoryModel.getMobile());
                i.putExtra("description",foodHistoryModel.getDescription());
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
        TextView textname,textsdate,textedate,textmno,textlocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            textname = itemView.findViewById(R.id.txt_name);
            textsdate = itemView.findViewById(R.id.txt_startdate);
            textedate = itemView.findViewById(R.id.txt_enddate);
            textmno = itemView.findViewById(R.id.txt_mno);
            textlocation = itemView.findViewById(R.id.txt_location);

//            itemView.setOnClickListener(this);
        }
    }

}



