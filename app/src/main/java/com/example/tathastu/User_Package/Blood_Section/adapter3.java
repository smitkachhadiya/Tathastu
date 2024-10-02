package com.example.tathastu.User_Package.Blood_Section;

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
import com.example.tathastu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


// My Blood Request page
public class adapter3 extends RecyclerView.Adapter<adapter3.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<blood_user_model> B_donor;

    private Context context;

    adapter3(Context context, ArrayList<blood_user_model> B_donor){
        this.layoutInflater = LayoutInflater.from(context);
        this.B_donor = B_donor;
        this.context = context; // Initialize context
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.b_my_request_card_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // bind the textview with data received
        blood_user_model blood_user_model = B_donor.get(i);
        viewHolder.textname.setText(blood_user_model.getName());
        viewHolder.textage.setText(blood_user_model.getAge());
        viewHolder.textmno.setText(blood_user_model.getMobile());
        viewHolder.txtlocation.setText(blood_user_model.getAddress());
        viewHolder.txttype.setText(blood_user_model.getBlood_group());

        viewHolder.BTN_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, B_Edit_detail_page.class); // Use activity context
                i.putExtra("name",blood_user_model.getName());
                i.putExtra("mno",blood_user_model.getMobile());
                i.putExtra("weight",blood_user_model.getWeight());
                i.putExtra("blood_group",blood_user_model.getBlood_group());
                i.putExtra("age",blood_user_model.getAge());
                i.putExtra("location",blood_user_model.getAddress());
                i.putExtra("type",blood_user_model.getDescription());
                i.putExtra("key",blood_user_model.getKey());
                view.getContext().startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(context);
            }
        });

        viewHolder.BTN_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }

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

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("blood").child(blood_user_model.getKey());

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
        return B_donor.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;

        FloatingActionButton BTN_edit, BTN_delete;
        TextView textname,textage,textmno,txtlocation,txttype;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            textname = itemView.findViewById(R.id.txt_name);
            textage = itemView.findViewById(R.id.txt_age);
            textmno = itemView.findViewById(R.id.txt_mno);
            txtlocation = itemView.findViewById(R.id.txt_location);
            txttype = itemView.findViewById(R.id.txt_type);
//            itemView.setOnClickListener(this);

            BTN_edit = itemView.findViewById(R.id.BTN_edit);
            BTN_delete = itemView.findViewById(R.id.BTN_delete);

        }

    }
}



