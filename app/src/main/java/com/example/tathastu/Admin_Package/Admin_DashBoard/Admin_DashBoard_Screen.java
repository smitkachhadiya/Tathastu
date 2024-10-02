package com.example.tathastu.Admin_Package.Admin_DashBoard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.Admin_Package.Admin_Entry.Admin_Login_Screen;
import com.example.tathastu.Admin_Package.Admin_Feedback.Admin_All_Feedback;
import com.example.tathastu.Admin_Package.Admin_NGO.All_Data.Admin_NGO_Data;
import com.example.tathastu.Admin_Package.Admin_user.All_Data.Admin_User_Data;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Common_Screens.About_us_Screen;
import com.example.tathastu.User_Package.user_Common_Screens.Contact_us_Screen;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;

public class Admin_DashBoard_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    ImageButton admin_BTN_dash_aboutus, admin_BTN_dash_contactus;
    CardView  admin_card_dash_userData,admin_card_dash_compaignData,admin_card_dash_feedbackData;

    FloatingActionButton admin_BTN_dash_logout;

    ShapeableImageView admin_profile_icon;
    // Admin data
    private String adminEmail;
    private String adminName;
    private String adminMno;
    private String adminPwd;

    private ConnectivityReceiver connectivityReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board_screen);

        admin_BTN_dash_aboutus=findViewById(R.id.admin_BTN_dash_aboutus);
        admin_BTN_dash_contactus=findViewById(R.id.admin_BTN_dash_contactus);
        admin_card_dash_userData=findViewById(R.id.admin_card_dash_userData);
        admin_card_dash_compaignData=findViewById(R.id.admin_card_dash_compaignData);
        admin_card_dash_feedbackData=findViewById(R.id.admin_card_dash_feedbackData);
        admin_BTN_dash_logout=findViewById(R.id.admin_BTN_dash_logout);
        admin_profile_icon=findViewById(R.id.admin_profile_icon);

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        //CARD USER DATA
        admin_card_dash_userData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Admin_DashBoard_Screen.this, Admin_User_Data.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Admin_DashBoard_Screen.this);
            }
        });


        //CARD FEEDBACK DATA
        admin_card_dash_feedbackData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Admin_DashBoard_Screen.this, Admin_All_Feedback.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Admin_DashBoard_Screen.this);
            }
        });

        //CARD CAMP DATA
        admin_card_dash_compaignData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Admin_DashBoard_Screen.this, Admin_NGO_Data.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Admin_DashBoard_Screen.this);
            }
        });

        // LOGOUT BUTTON
        admin_BTN_dash_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showlogout();
            }
        });


        //FOR CONTACT US>>>
        admin_BTN_dash_contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Admin_DashBoard_Screen.this, Contact_us_Screen.class);
                i.putExtra("source","admin_contact");
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Admin_DashBoard_Screen.this);
            }
        });

        //FOR ABOUT US >>>>
        admin_BTN_dash_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Admin_DashBoard_Screen.this, About_us_Screen.class);
                i.putExtra("source","admin_about");
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Admin_DashBoard_Screen.this);
            }
        });

        // Retrieve intent data
        Intent intent = getIntent();
        if (intent != null) {
            adminEmail = intent.getStringExtra("admin_email");
            adminName = intent.getStringExtra("admin_name");
            adminMno = intent.getStringExtra("admin_mno");
            adminPwd = intent.getStringExtra("admin_pwd");
        }


    }
//-----------------------------------------------------------------------------------------
    // ON BACK PRESSED
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        showExitDialog();
    }



    // Show exit confirmation dialog
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
                Intent logout = new Intent(Admin_DashBoard_Screen.this, Admin_Login_Screen.class);
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
    // Show exit confirmation dialog
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.exit_dialog, null);
        builder.setView(dialogView);

        ExtendedFloatingActionButton btnExitYes = dialogView.findViewById(R.id.BTN_exit_yes);
        ExtendedFloatingActionButton btnExitNo = dialogView.findViewById(R.id.BTN_exit_no);


        final AlertDialog dialog = builder.create();

        btnExitYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle 'Yes' button click
                finishAffinity(); // Finish this activity and all activities immediately below it
                System.exit(0); // Exit the app entirely
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

    //PROFILE METHOD
    public void onProfileClick(View view) {
        // Handle click on the profile icon
        // Pass admin data to the profile screen
        Intent profileIntent = new Intent(Admin_DashBoard_Screen.this, Admin_Profile_Screen.class);
        profileIntent.putExtra("admin_email", adminEmail);
        profileIntent.putExtra("admin_name", adminName);
        profileIntent.putExtra("admin_mno", adminMno);
        profileIntent.putExtra("admin_pwd", adminPwd);

        startActivity(profileIntent);
    }
}