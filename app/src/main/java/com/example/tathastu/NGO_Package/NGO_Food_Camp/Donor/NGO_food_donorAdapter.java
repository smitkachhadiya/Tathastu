package com.example.tathastu.NGO_Package.NGO_Food_Camp.Donor;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.Food_Section.History.food_user_model;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


// Home screen/Home screen2 - camp card
public class NGO_food_donorAdapter extends RecyclerView.Adapter<NGO_food_donorAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<food_user_model> donor;


    NGO_food_donorAdapter(NGO_food_user_request context, ArrayList<food_user_model> donor) {
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
        food_user_model data = donor.get(i);
        viewHolder.textname.setText(data.getName());
        viewHolder.textnumber.setText(data.getMobile());
        viewHolder.textloc.setText(data.getAddress());
        viewHolder.textnote.setText(data.getDescription());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), NGO_food_user_request_inDetails.class);
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
        FloatingActionButton BTN_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            textname = itemView.findViewById(R.id.txt_food_donor_name);
            textnumber = itemView.findViewById(R.id.txt_food_donor_mno);
            textloc = itemView.findViewById(R.id.txt_food_donor_location);
            textnote = itemView.findViewById(R.id.txt_food_donor_fooddetails);
            BTN_delete = itemView.findViewById(R.id.BTN_food_donor_delete);
            BTN_delete.setVisibility(View.GONE);
//            itemView.setOnClickListener(this);

            BTN_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog();

                }

                // Show exit confirmation dialog
                private void showDeleteConfirmationDialog() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    View dialogView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.custom_delete_dialog, null);
                    builder.setView(dialogView);

                    ExtendedFloatingActionButton btnExitYes = dialogView.findViewById(R.id.BTN_exit_yes);
                    ExtendedFloatingActionButton btnExitNo = dialogView.findViewById(R.id.BTN_exit_no);


                    final AlertDialog dialog = builder.create();

                    btnExitYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(layoutInflater.getContext(), "Delete clicked", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    btnExitNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle 'No' button click
                            dialog.dismiss();
                        }
                    });

                    dialog.setCancelable(false); // Prevent dismiss on outside touch
                    dialog.show();
                }
            });
        }

    }
}



