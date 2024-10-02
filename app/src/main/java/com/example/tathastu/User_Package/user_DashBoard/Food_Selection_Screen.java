package com.example.tathastu.User_Package.user_DashBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.Food_Section.All_Donors.Food_user_all_donors;
import com.example.tathastu.User_Package.Food_Section.Camp.Food_Donation_Camp;
import com.example.tathastu.User_Package.Food_Section.Food_User_Request;
import com.example.tathastu.User_Package.Food_Section.History.Food_user_History;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class Food_Selection_Screen extends AppCompatActivity  implements ConnectivityReceiver.ConnectivityReceiverListener{
    private ConnectivityReceiver connectivityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_selection_screen);
        CardView BTN_food_request = findViewById(R.id.BTN_food_request);
        CardView BTN_food_camp = findViewById(R.id.BTN_food_camp);
        CardView BTN_food_request_history = findViewById(R.id.BTN_food_request_history);
        CardView BTN_other_food_donors = findViewById(R.id.BTN_other_food_donors);

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        FloatingActionButton BTN_back = findViewById(R.id.BTN_back);
        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                Animatoo.INSTANCE.animateSlideRight(Food_Selection_Screen.this);
            }
        });

        //Request
        BTN_food_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Food_Selection_Screen.this, Food_User_Request.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Food_Selection_Screen.this);
            }
        });

        //CAMP
        BTN_food_camp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Food_Selection_Screen.this, Food_Donation_Camp.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Food_Selection_Screen.this);
            }
        });
        //History
        BTN_food_request_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Food_Selection_Screen.this, Food_user_History.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Food_Selection_Screen.this);
            }
        });
//All DOnors
        BTN_other_food_donors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Food_Selection_Screen.this, Food_user_all_donors.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Food_Selection_Screen.this);
            }
        });

    }

    //----------------------------------------------------------------------------------------------------


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