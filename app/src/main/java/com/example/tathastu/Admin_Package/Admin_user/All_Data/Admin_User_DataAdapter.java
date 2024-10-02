package com.example.tathastu.Admin_Package.Admin_user.All_Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.Admin_Package.Admin_user.Person_User_Data.Admin_Person_Details;
import com.example.tathastu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Admin_User_DataAdapter extends RecyclerView.Adapter<Admin_User_DataAdapter.EventViewHolder> {

    private final List<Admin_User_DataModel> userDataModelList;
    private final Context context;


    public Admin_User_DataAdapter(Context context, List<Admin_User_DataModel> userDataModelList) {
        this.context = context;
        this.userDataModelList = userDataModelList;
    }


    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_admin_user_data, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Admin_User_DataModel user = userDataModelList.get(position);
        holder.bind(user);
    }


    @Override
    public int getItemCount() {
        return userDataModelList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView admin_user_Name;
        private AppCompatTextView admin_user_Number;
        private AppCompatTextView admin_user_Email;
        private ImageButton userDetailButton;
        private ImageButton deleteButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            admin_user_Name = itemView.findViewById(R.id.admin_user_Name);
            admin_user_Number = itemView.findViewById(R.id.admin_user_Number);
            admin_user_Email = itemView.findViewById(R.id.admin_user_Email);
            userDetailButton = itemView.findViewById(R.id.BTN_user_inDetail);
            deleteButton = itemView.findViewById(R.id.BTN_user_Delete);
        }

        public void bind(Admin_User_DataModel user) {
            admin_user_Name.setText(user.getFname());
            admin_user_Number.setText(user.getMobile());
            admin_user_Email.setText(user.getEmail());

            // Set click listener for user detail
            userDetailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle click, e.g., start UserInDetail activity
                    Intent intent = new Intent(itemView.getContext(), Admin_Person_Details.class);
                    intent.putExtra("userId",user.getUserId());
                    itemView.getContext().startActivity(intent);

                    Animatoo.INSTANCE.animateSlideLeft(itemView.getContext());
                }
            });

            // Set click listener for delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    View dialogView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.custom_delete_dialog, null);
                    builder.setView(dialogView);

                    ExtendedFloatingActionButton btnExitYes = dialogView.findViewById(R.id.BTN_exit_yes);
                    ExtendedFloatingActionButton btnExitNo = dialogView.findViewById(R.id.BTN_exit_no);


                    final AlertDialog dialog = builder.create();

                    btnExitYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Handle click, e.g., delete the item
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("user");
                                databaseRef.child(user.getUserId()).removeValue()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(itemView.getContext(), "User Deleted Successfully!!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(itemView.getContext(), "Failed To Delete User!!", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                deleteItem(position);
                            }
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

    public void deleteItem(int position) {

        userDataModelList.remove(position);
        notifyItemRemoved(position);
    }

}
