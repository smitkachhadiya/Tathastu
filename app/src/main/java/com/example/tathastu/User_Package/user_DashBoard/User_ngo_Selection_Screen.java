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
import com.example.tathastu.User_Package.user_NGO_list.New_NGO_list.direct_contact_to_new_NGO;
import com.example.tathastu.User_Package.user_NGO_list.direct_contact_to_NGO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class User_ngo_Selection_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private ConnectivityReceiver connectivityReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ngo_selection_screen);
        CardView BTN_user_new_ngo = findViewById(R.id.BTN_user_new_ngo);
        CardView BTN_user_old_ngo = findViewById(R.id.BTN_user_old_ngo);

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        FloatingActionButton BTN_back = findViewById(R.id.BTN_user_ngolist_back);
        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                Animatoo.INSTANCE.animateSlideRight(User_ngo_Selection_Screen.this);
            }
        });

        Intent i = getIntent();

        String source = i.getStringExtra("source");
        
        //Request
        BTN_user_new_ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(User_ngo_Selection_Screen.this, direct_contact_to_new_NGO.class);
                i.putExtra("source",source);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(User_ngo_Selection_Screen.this);
            }
        });

        //CAMP
        BTN_user_old_ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(User_ngo_Selection_Screen.this, direct_contact_to_NGO.class);
                i.putExtra("source",source);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(User_ngo_Selection_Screen.this);
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