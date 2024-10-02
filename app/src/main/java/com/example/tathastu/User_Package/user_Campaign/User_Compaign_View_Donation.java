package com.example.tathastu.User_Package.user_Campaign;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.NGO_Package.NGO_Campaign.donation;
import com.example.tathastu.NGO_Package.NGO_Campaign.donationListAdapter;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class User_Compaign_View_Donation extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private ConnectivityReceiver connectivityReceiver;

    List<donation> donationList;
    DatabaseReference reference,reference1;
    ListView donation_list;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_campaign_view_donation);

        sharedPreferences = getSharedPreferences("USER",MODE_PRIVATE);

        donation_list = findViewById(R.id.donation_list);
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
//                Intent i =new Intent(Event_Notifications_Screen.this, DashBoard_Screen.class);
//                startActivity(i);
                finish();
                Animatoo.INSTANCE.animateSlideRight(User_Compaign_View_Donation.this);
            }
        });


        ArrayList<donation> donationList = new ArrayList<>();

        donationListAdapter dadapter = new donationListAdapter(this,donationList);
        donation_list.setAdapter(dadapter);

        Intent intent = this.getIntent();
        String key = intent.getStringExtra("key");



        reference1 = FirebaseDatabase.getInstance().getReference().child("campaigns").child(key);

        // Checking if Donation data is available
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.hasChild("Donations")){

                    // Fetching Donation Data

                    reference = FirebaseDatabase.getInstance().getReference().child("campaigns").child(key).child("Donations");

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange( @NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                donationList.clear();

                                // adding data in Array List

                                for(DataSnapshot cdataSnapshot : snapshot.getChildren()){
                                    donation d = cdataSnapshot.getValue(donation.class);

                                    if(sharedPreferences.getString("userId","").matches(d.getUserId()))
                                    {
                                        donationList.add(d);

                                    }
                                }

                                dadapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }
                else{
                    AlertDialog.Builder builder= new AlertDialog.Builder(new ContextThemeWrapper(User_Compaign_View_Donation.this,R.style.CustomAlertDialog));
                    builder.setMessage("No Data Available");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    AlertDialog alertConfirm = builder.create();
                    alertConfirm.show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }
}