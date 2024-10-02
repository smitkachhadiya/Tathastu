package com.example.tathastu.User_Package.user_Common_Screens;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class Terms_C_activity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private ConnectivityReceiver connectivityReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_c_activity);
        FloatingActionButton BTN_tc_insta=findViewById(R.id.BTN_tc_insta);
        FloatingActionButton BTN_tc_fb=findViewById(R.id.BTN_tc_fb);
        FloatingActionButton BTN_tc_twitter=findViewById(R.id.BTN_tc_twitter);


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
                // Handle the back button based on the source
                String source = getIntent().getStringExtra("source");
                if ("signin".equals(source)) {
                    // If source is login, go back to LoginActivity
//                    Intent intent = new Intent(Terms_C_activity.this, Signin_Screen.class);
//                    startActivity(intent);
                    finish();
                    Animatoo.INSTANCE.animateSlideDown(Terms_C_activity.this);
                } else if ("about".equals(source)) {
                    // If source is signin, go back to SignInActivity
//                    Intent intent = new Intent(Terms_C_activity.this, About_us_Screen.class);
//                    startActivity(intent);
                    finish();
                    Animatoo.INSTANCE.animateSlideRight(Terms_C_activity.this);
                } else {
                    // Default behavior (handle appropriately)
                    onBackPressed();
                    finish();
                    Animatoo.INSTANCE.animateSlideRight(Terms_C_activity.this);
                }
            }
        });


        //FOR FOLLOW US ON
        BTN_tc_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    String instagramLink = "https://www.instagram.com/tathastu.g052?igsh=OGQ5ZDc2ODk2ZA==\n";
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramLink));
                    startActivity(i);
                    Animatoo.INSTANCE.animateSwipeLeft(Terms_C_activity.this);
                } else {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                }
            }
        });

        BTN_tc_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    String facebookLink = "https://www.facebook.com/tathastu.g052?mibextid=ZbWKwL\n";
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookLink));
                    startActivity(i);
                    Animatoo.INSTANCE.animateSwipeLeft(Terms_C_activity.this);
                } else {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                }
            }
        });
        BTN_tc_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                    String twitterLink = "https://twitter.com/tathastu_g052?t=MrAtvaTuvcF1hbSMDqd5_A&s=09\n";
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterLink));
                    startActivity(i);
                    Animatoo.INSTANCE.animateSwipeLeft(Terms_C_activity.this);
                }
                else {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                }
            }
        });
    }
//--------------------------------------------------------------------------------------------------


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.INSTANCE.animateSlideDown(Terms_C_activity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the receiver to avoid memory leaks
        unregisterReceiver(connectivityReceiver);
    }
    // Helper method to check if the device is connected to the internet
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
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