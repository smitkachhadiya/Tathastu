package com.example.tathastu.User_Package.Blood_Section;

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

import java.util.ArrayList;
import java.util.List;


// Home page - blood request
public class adapter1 extends RecyclerView.Adapter<adapter1.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<blood_user_model> donor;

    adapter1(B_Home_Screen context, ArrayList<blood_user_model> donor){
        this.layoutInflater = LayoutInflater.from(context);
        this.donor = donor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.b_person_card_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // bind the textview with data received
        blood_user_model blood_user_model = donor.get(i);
        viewHolder.textname.setText(blood_user_model.getName());
        viewHolder.textage.setText(blood_user_model.getAge());
        viewHolder.textmno.setText(blood_user_model.getMobile());
        viewHolder.txtlocation.setText(blood_user_model.getAddress());
        viewHolder.txttype.setText(blood_user_model.getBlood_group());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), B_Details_of_person.class);
                i.putExtra("title", blood_user_model.getName());
                i.putExtra("age", blood_user_model.getAge());
                i.putExtra("mno", blood_user_model.getMobile());
                i.putExtra("loc", blood_user_model.getAddress());
                i.putExtra("bgroup", blood_user_model.getBlood_group());
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
        TextView textname,textage,textmno,txtlocation,txttype;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            textname = itemView.findViewById(R.id.txt_name);
            textage = itemView.findViewById(R.id.txt_age);
            textmno = itemView.findViewById(R.id.txt_mno);
            txtlocation = itemView.findViewById(R.id.txt_location);
            txttype = itemView.findViewById(R.id.txt_type);


        }
    }
}



