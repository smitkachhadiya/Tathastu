package com.example.tathastu.User_Package.user_DashBoard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Entry.Login_Screen;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    //ALL
    TextInputEditText  txt_Profile_email, txt_Profile_mno, txt_Profile_dob;

    MaterialTextView txt_Profile_Fname;
    // INTERNET
    private ConnectivityReceiver connectivityReceiver;

    //PROFILE IMAGE
    int SELECT_PICTURE = 200;
    ShapeableImageView img_profile_photo;
    Uri selectedImageUri;

    //EDIT PROFILE
    ExtendedFloatingActionButton BTN_Profile_edit,BTN_Profile_logout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        fetchProfileData();

        img_profile_photo = findViewById(R.id.img_profile_photo);
        txt_Profile_Fname = findViewById(R.id.txt_Profile_Fname);
        txt_Profile_email = findViewById(R.id.txt_Profile_email);
        txt_Profile_mno = findViewById(R.id.txt_Profile_mno);
        txt_Profile_dob = findViewById(R.id.txt_Profile_dob);
        BTN_Profile_edit = findViewById(R.id.BTN_Profile_edit);
        BTN_Profile_logout = findViewById(R.id.BTN_Profile_logout);


        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        FloatingActionButton BTN_back=findViewById(R.id.BTN_back);
        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Animatoo.INSTANCE.animateSlideRight(Profile_Screen.this);
            }
        });

        // LOGOUT BUTTON
        BTN_Profile_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showlogoutDialog();
            }
        });

        BTN_Profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Profile_Screen.this, Update_Profile_Screen.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Profile_Screen.this);
            }
        });
    }

    private void fetchProfileData() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
        String userId = sharedPreferences1.getString("userId","");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    profile_getset data = snapshot.getValue(profile_getset.class);
                    String fname = data.getFname();
                    String lname=data.getLname();
                    String email = data.getEmail();
                    String mno=data.getMobile();
                    String dob=data.getDob();
                    String photo = data.getPhoto();


                    txt_Profile_Fname.setText(fname+" "+lname);
                    txt_Profile_email.setText(email);
                    txt_Profile_mno.setText(mno);
                    txt_Profile_dob.setText(dob);

                    // Check if photo is not null or empty
                    if (photo != null && !photo.isEmpty()) {
                        // Load the profile photo using Glide
                        Glide.with(Profile_Screen.this)
                                .load(photo)
                                .centerCrop()
                                .into(img_profile_photo);
                    } else {
                        // Set a default image when no image is found or not set
                        img_profile_photo.setImageResource(R.drawable.profile_img_icon_foreground);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//------------------------------------------------------------------

    private void showlogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.logout_exit_dialog, null);
        builder.setView(dialogView);

        ExtendedFloatingActionButton btnExitYes = dialogView.findViewById(R.id.BTN_exit_yes);
        ExtendedFloatingActionButton btnExitNo = dialogView.findViewById(R.id.BTN_exit_no);


        final AlertDialog dialog = builder.create();

        btnExitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle 'Yes' button click

                // This is use when user Log Out the SharedPreference value is clear
                SharedPreferences sharedPreferences = getSharedPreferences("UserLogin",MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("hasLoggedIn",false).apply();
                sharedPreferences.edit().clear().apply();
                finish();
                Intent logout = new Intent(Profile_Screen.this, Login_Screen.class);
                startActivity(logout);
                Animatoo.INSTANCE.animateSlideRight(Profile_Screen.this);
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
        @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(Profile_Screen.this);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    img_profile_photo.setImageURI(selectedImageUri);
                }
            }
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the receiver to avoid memory leaks
        unregisterReceiver(connectivityReceiver);
    }

    //SNACKBAR
    private void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();

        // Inflate custom layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View customView = inflater.inflate(R.layout.custom_snackbar_layout, null);

        // Set text
        TextView textView = customView.findViewById(android.R.id.text1);
        textView.setText(message);

        // Add custom view to Snackbar
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarView;
        snackbarLayout.removeAllViews(); // Remove all default views
        snackbarLayout.setPadding(1, 1, 1, 1);
        snackbarLayout.addView(customView, 0);

        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
        }
    }
}
