package com.example.tathastu.Admin_Package.Admin_DashBoard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.tathastu.Admin_Package.Admin_Entry.AdminModel;
import com.example.tathastu.Admin_Package.Admin_Entry.Admin_Login_Screen;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_DashBoard.Profile_Screen;
import com.example.tathastu.User_Package.user_DashBoard.profile_getset;
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

public class Admin_Profile_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    // ALL
    TextInputEditText  admin_txt_Profile_email, admin_txt_Profile_mno, admin_txt_Profile_pwd;

    MaterialTextView admin_txt_Profile_Fname;
    // INTERNET
    private ConnectivityReceiver connectivityReceiver;

    ShapeableImageView admin_img_profile_photo;

    ExtendedFloatingActionButton BTN_Profile_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile_screen);

        fetchadmindata();

        admin_img_profile_photo = findViewById(R.id.admin_img_profile_photo);
        admin_txt_Profile_Fname = findViewById(R.id.admin_txt_Profile_Fname);
        admin_txt_Profile_email = findViewById(R.id.admin_txt_Profile_email);
        admin_txt_Profile_mno = findViewById(R.id.admin_txt_Profile_mno);
        admin_txt_Profile_pwd = findViewById(R.id.admin_txt_Profile_pwd);
        BTN_Profile_logout = findViewById(R.id.BTN_Profile_logout);

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        FloatingActionButton BTN_back = findViewById(R.id.BTN_back);


        // LOGOUT BUTTON
        BTN_Profile_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showlogout();

            }
        });


        // Retrieve intent data
//        Intent intent = getIntent();
//        if (intent != null) {
//            String adminEmail = intent.getStringExtra("admin_email");
//            String adminName = intent.getStringExtra("admin_name");
//            String adminMno = intent.getStringExtra("admin_mno");
//            String adminPwd = intent.getStringExtra("admin_pwd");
//
//            // Set data to TextInputEditText fields
//            admin_txt_Profile_Fname.setText(adminName);
//            admin_txt_Profile_email.setText(adminEmail);
//            admin_txt_Profile_mno.setText(adminMno);
//            admin_txt_Profile_pwd.setText(adminPwd);
//        }

        // BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Animatoo.INSTANCE.animateSlideRight(Admin_Profile_Screen.this);

            }
        });
    }

    //------------------------------------------------------------------
    private void showlogout() {
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
                SharedPreferences sharedPreferences = getSharedPreferences("AdminLogin",MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("hasLoggedIn",false).apply();
                sharedPreferences.edit().clear().apply();
                finish();
                Intent logout = new Intent(Admin_Profile_Screen.this, Admin_Login_Screen.class);
                startActivity(logout);
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

    public void fetchadmindata() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("ADMIN",MODE_PRIVATE);
        String userId = sharedPreferences1.getString("adminId","");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("admin").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    AdminModel data = snapshot.getValue(AdminModel.class);
                    String fname = data.getName();
                    String mobile=data.getMno();
                    String mail = data.getEmail();
                    String password=data.getPwd();


                    admin_txt_Profile_Fname.setText(fname);
                    admin_txt_Profile_mno.setText(mobile);
                    admin_txt_Profile_email.setText(mail);
                    admin_txt_Profile_pwd.setText(password);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the receiver to avoid memory leaks
        unregisterReceiver(connectivityReceiver);
    }

    // SNACKBAR
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
