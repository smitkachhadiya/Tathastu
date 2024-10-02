package com.example.tathastu.NGO_Package.NGO_Education_Camp;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.NGO_Package.NGO_Education_Camp.Donor.NGO_edu_user_request;
import com.example.tathastu.NGO_Package.NGO_Education_Camp.History.NGO_edu_camp_History;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class NGO_Educamp_Selection extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    CardView BTN_edu_req,BTN_edu_history,BTN_edu_user_request;
    // INTERNET
    private ConnectivityReceiver connectivityReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_educamp_selection);
        BTN_edu_req=findViewById(R.id.BTN_edu_req);
        BTN_edu_history=findViewById(R.id.BTN_edu_history);
        BTN_edu_user_request=findViewById(R.id.BTN_edu_user_request);

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

                finish();    Animatoo.INSTANCE.animateSlideRight(NGO_Educamp_Selection.this);
            }
        });

        //PERSON REQUIREMENT
        BTN_edu_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NGO_Educamp_Selection.this, NGO_EduCamp_Request.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Educamp_Selection.this);
            }
        });

        //CAMP REQUIREMENT
        BTN_edu_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NGO_Educamp_Selection.this, NGO_edu_camp_History.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Educamp_Selection.this);
            }
        });
//CAMP REQUIREMENT
        BTN_edu_user_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NGO_Educamp_Selection.this, NGO_edu_user_request.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Educamp_Selection.this);
            }
        });
    }
    //----------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(NGO_Educamp_Selection.this);
        finish();
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