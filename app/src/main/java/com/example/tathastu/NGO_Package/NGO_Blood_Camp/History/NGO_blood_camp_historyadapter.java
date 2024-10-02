package com.example.tathastu.NGO_Package.NGO_Blood_Camp.History;

import android.content.Context;
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
import com.example.tathastu.NGO_Package.NGO_Food_Camp.History.NGO_food_History_model;
import com.example.tathastu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


// Home screen/Home screen2 - camp card
public class NGO_blood_camp_historyadapter extends RecyclerView.Adapter<NGO_blood_camp_historyadapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<NGO_food_History_model> b_donor;

    private Context context;


    NGO_blood_camp_historyadapter(NGO_blood_camp_history context, ArrayList<NGO_food_History_model> b_donor){
        this.layoutInflater = LayoutInflater.from(context);
        this.b_donor = b_donor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.cardview_ngo_blood,viewGroup,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // bind the textview with data received
        NGO_food_History_model data = b_donor.get(i);
        viewHolder.textname.setText(data.getNgo_name());
        viewHolder.textsdate.setText(data.getStart_date());
        viewHolder.textedate.setText(data.getEnd_date());
        viewHolder.textmno.setText(data.getMobile());
        viewHolder.textlocation.setText(data.getC_address());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), NGO_blood_camp_history_indetails.class);
                i.putExtra("title", data.getNgo_name());
                i.putExtra("sdate", data.getStart_date());
                i.putExtra("edate", data.getEnd_date());
                i.putExtra("loc", data.getC_address());
                i.putExtra("mno", data.getMobile());
                i.putExtra("description", data.getDescription());
                i.putExtra("key", data.getKey());
                view.getContext().startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(view.getContext());
            }
        });

        viewHolder.BTN_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();

            }

            // Show exit confirmation dialog
            private void showDeleteConfirmationDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext());
                View dialogView = LayoutInflater.from(viewHolder.itemView.getContext()).inflate(R.layout.custom_delete_dialog, null);
                builder.setView(dialogView);

                ExtendedFloatingActionButton btnExitYes = dialogView.findViewById(R.id.BTN_exit_yes);
                ExtendedFloatingActionButton btnExitNo = dialogView.findViewById(R.id.BTN_exit_no);


                final AlertDialog dialog = builder.create();

                btnExitYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ngo_blood").child(data.getKey());

                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // User data has been successfully deleted
                                    Toast.makeText(context, "User Deleted Sucessfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Failed to delete user data
                                    Toast.makeText(context, "Failed to delete user.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

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


        // similarly you can set new image for each card and descriptions

    }

    @Override
    public int getItemCount() {
        return b_donor.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textname,textsdate,textedate,textmno,textlocation;
        FloatingActionButton BTN_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            textname = itemView.findViewById(R.id.txt_name);
            textsdate = itemView.findViewById(R.id.txt_startdate);
            textedate = itemView.findViewById(R.id.txt_enddate);
            textmno = itemView.findViewById(R.id.txt_mno);
            textlocation = itemView.findViewById(R.id.txt_location);
            BTN_delete = itemView.findViewById(R.id.BTN_delete);
//            itemView.setOnClickListener(this);


        }

    }
}



