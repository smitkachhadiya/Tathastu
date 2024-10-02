package com.example.tathastu.NGO_Package.NGO_Food_Camp;

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
import com.example.tathastu.NGO_Package.NGO_Food_Camp.Donor.NGO_food_user_request;
import com.example.tathastu.NGO_Package.NGO_Food_Camp.History.NGO_food_camp_history;
import com.example.tathastu.NGO_Package.NGO_Food_Camp.Request.NGO_food_camp_request;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class NGO_FoodCamp_Selection extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    CardView BTN_food_req,BTN_food_history,BTN_food_user_request;
    // INTERNET
    private ConnectivityReceiver connectivityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_food_camp_selection);

        BTN_food_req=findViewById(R.id.BTN_food_req);
        BTN_food_history=findViewById(R.id.BTN_food_history);
        BTN_food_user_request=findViewById(R.id.BTN_food_user_request);

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

                finish();   Animatoo.INSTANCE.animateSlideRight(NGO_FoodCamp_Selection.this);
            }
        });

        //PERSON REQUIREMENT
        BTN_food_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NGO_FoodCamp_Selection.this, NGO_food_camp_request.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_FoodCamp_Selection.this);
            }
        });

        //CAMP REQUIREMENT
        BTN_food_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NGO_FoodCamp_Selection.this, NGO_food_camp_history.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_FoodCamp_Selection.this);
            }
        });
//CAMP REQUIREMENT
        BTN_food_user_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NGO_FoodCamp_Selection.this, NGO_food_user_request.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_FoodCamp_Selection.this);
            }
        });
    }
    //----------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(NGO_FoodCamp_Selection.this);
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