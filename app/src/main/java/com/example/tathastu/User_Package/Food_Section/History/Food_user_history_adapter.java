package com.example.tathastu.User_Package.Food_Section.History;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Food_user_history_adapter extends RecyclerView.Adapter<Food_user_history_adapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<food_user_model> donor;
    
    private Context context;

    Food_user_history_adapter(Food_user_History context, ArrayList<food_user_model> donor) {
        this.layoutInflater = LayoutInflater.from(context);
        this.donor = donor;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.food_request_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // bind the textview with data received
        food_user_model data = donor.get(i);
        viewHolder.textname.setText(data.getName());
        viewHolder.textmno.setText(data.getMobile());
        viewHolder.txtlocation.setText(data.getAddress());
        viewHolder.txttype.setText(data.getDescription());

        viewHolder.BTN_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();

            }
            // Show exit confirmation dialog
            private void showDeleteConfirmationDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context); // Use activity context
                View dialogView = LayoutInflater.from(viewHolder.itemView.getContext()).inflate(R.layout.custom_delete_dialog, null);
                builder.setView(dialogView);

                ExtendedFloatingActionButton btnExitYes = dialogView.findViewById(R.id.BTN_exit_yes);
                ExtendedFloatingActionButton btnExitNo = dialogView.findViewById(R.id.BTN_exit_no);


                final AlertDialog dialog = builder.create();

                btnExitYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("food").child(data.getKey());

                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // User data has been successfully deleted
                                    Toast.makeText(context, "Deleted Sucessfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Failed to delete user data
                                    Toast.makeText(context, "Failed to delete.", Toast.LENGTH_SHORT).show();
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

        viewHolder.BTN_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Food_user_history_edit_request.class);
                i.putExtra("name",data.getName());
                i.putExtra("mno",data.getMobile());
                i.putExtra("email",data.getEmail());
                i.putExtra("location",data.getAddress());
                i.putExtra("type",data.getDescription());
                i.putExtra("key",data.getKey());
                view.getContext().startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(context);
            }
        });

        // similarly you can set new image for each card and descriptions

    }

    @Override
    public int getItemCount() {
        return donor.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textname, txttype, textmno, txtlocation;
        FloatingActionButton BTN_edit, BTN_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            textname = itemView.findViewById(R.id.txt_name);
            textmno = itemView.findViewById(R.id.txt_mno);
            txtlocation = itemView.findViewById(R.id.txt_location);
            txttype = itemView.findViewById(R.id.txt_type);

            itemView.setOnClickListener(this);

            BTN_edit = itemView.findViewById(R.id.BTN_edit);
            BTN_delete = itemView.findViewById(R.id.BTN_delete);

        }

        @Override
        public void onClick(View v) {

        }
    }
}