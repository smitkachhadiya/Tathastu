// Event_Notifications_Screen.java

package com.example.tathastu.User_Package.user_Event;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_DashBoard.Update_Profile_Screen;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Event_Notifications_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private ConnectivityReceiver connectivityReceiver;

    List<events> eventsList;
    DatabaseReference reference;
    FloatingActionButton btn_back;
    ListView admin_eventlist;

    String[] ename ={
            "Tapi Revival Project",
            "Serve Orphanage",
            "Animal Rescue",
            "Woman Empowerment",
    };
    String[] edesc ={
            "The prime objective of the work is to improve the water quality of river tapi, which is the major resource of drinking water for citizens of surat city.Projecting of river tapi from any further degradation and thereby improvisation and restoration of the river water quality.",
            "The prime objective of project is to made a significant impact by improving living condition at the orphanage , which is achieved by helping to create opportunities for children through supporting their education and care. ",
            "Combine your passion for animal and desire to give to society by improving the unfortunate situation of abandoned street animals. During this volunteer program, You can help look for a new safe and beautiful home for stray dogs and cats. ",
            "Woman Empowerment is running by tathastu, this campaign aims is to supports womans by providing education and other services to the woman in specially rural areas.",
    };

    String[] oname ={
            "Youth Foundation",
            "Helping Hand Foundation",
            "Green Foundation",
            "Woman Empowerment",
    };
    String[] eaddress = {
            "River front,",
            "pavadar gali",
            "renuka nagar",
            "sardar chowk"
    };
    String[] ecity = {
            "surat",
            "ahemedabad",
            "gandhinagar",
            "vaddodara"
    };

    String[] edate = {
            "05-11-2024",
            "02-03-2024",
            "18-05-2024",
            "22-03-2024"
    };

    Integer[] etotal = {
            350,400,360,850
    };
    Integer[] eparticipated = {
            226,200,260,200
    };
    Integer[] eimgid = {
            R.drawable.event_cleaning,
            R.drawable.campaign_orphan,
            R.drawable.campaign_animal,
            R.drawable.campaign_woman,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_notifications_screen);

        admin_eventlist = findViewById(R.id.admin_eventlist);
        btn_back = findViewById(R.id.BTN_back);

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

                Animatoo.INSTANCE.animateSlideRight(Event_Notifications_Screen.this);
            }
        });

        ArrayList<events> evetsList = new ArrayList<>();

        eventListAdapter eadapter = new eventListAdapter(this, evetsList);
        admin_eventlist.setAdapter(eadapter);
        admin_eventlist.setClickable(true);

        reference = FirebaseDatabase.getInstance().getReference().child("events");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    evetsList.clear();

                    for(DataSnapshot cdataSnapshot : snapshot.getChildren()){
                        events e = cdataSnapshot.getValue(events.class);

                        try{
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Date eventDate = dateFormat.parse(e.getDate());

                            Date currentDate = new Date();

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(currentDate);
                            calendar.add(Calendar.DAY_OF_MONTH, -1);

                            Date mcurrentDate =calendar.getTime();

                            if(eventDate.after(mcurrentDate))
                            {
                                evetsList.add(e);
                            }

                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }

                    }

                    eadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        admin_eventlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent i = new Intent(Event_Notifications_Screen.this,user_Event_inDetails.class);
                i.putExtra("key",evetsList.get(position).getKey());
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Event_Notifications_Screen.this);
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
