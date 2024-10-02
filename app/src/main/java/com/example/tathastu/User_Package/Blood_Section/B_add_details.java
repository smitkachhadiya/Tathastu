package com.example.tathastu.User_Package.Blood_Section;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_DashBoard.profile_getset;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class B_add_details extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    FloatingActionButton btn_back;
    TextInputEditText txt_name, txt_age, txt_weight, txt_location, txt_note, txt_mno;
    MaterialAutoCompleteTextView txt_type;
    MaterialCheckBox chk_user;
    private ConnectivityReceiver connectivityReceiver;
    Button btn_add;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badd_details);

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        btn_back = findViewById(R.id.BTN_back);
        btn_add = findViewById(R.id.BTN_add);
        txt_name = findViewById(R.id.txt_name);
        txt_age = findViewById(R.id.txt_age);
        txt_weight = findViewById(R.id.txt_weight);
        txt_type = findViewById(R.id.txt_type);
        txt_location = findViewById(R.id.txt_location);
        txt_note = findViewById(R.id.txt_note);
        txt_mno = findViewById(R.id.txt_mno);
        chk_user = findViewById(R.id.chk_ex_blood_user);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Animatoo.INSTANCE.animateSlideRight(B_add_details.this);

            }
        });

        chk_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chk_user.isChecked()) {

                    SharedPreferences sharedPreferences1 = getSharedPreferences("USER", MODE_PRIVATE);
                    String userId = sharedPreferences1.getString("userId", "");

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(userId);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                profile_getset data = snapshot.getValue(profile_getset.class);
                                String fname = data.getFname();
                                String lname = data.getLname();
                                String email = data.getEmail();
                                String mno = data.getMobile();

                                txt_name.setText(fname + " " + lname);
                                txt_mno.setText(mno);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {

                    txt_name.setText("");
                    txt_mno.setText("");

                }

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = txt_name.getText().toString();
                String age = txt_age.getText().toString();
                String weight = txt_weight.getText().toString();
                String blood_group = txt_type.getText().toString();
                String location = txt_location.getText().toString();
                String description = txt_note.getText().toString();
                String mobile = txt_mno.getText().toString();

                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    if (name.isEmpty() || age.isEmpty() || weight.isEmpty() || blood_group.isEmpty() || location.isEmpty() || description.isEmpty() || mobile.isEmpty()) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter required details...");
                    } else if (mobile.length() < 13) {
                        txt_mno.setError("Please set +91 or enter a valid mobile number...");
                    } else {

                        SharedPreferences sharedPreferences1 = getSharedPreferences("USER", MODE_PRIVATE);
                        String userId = sharedPreferences1.getString("userId", "");

                        String key = String.valueOf(System.currentTimeMillis());

                        DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("blood");

                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name);
                        map.put("age", age);
                        map.put("weight", weight);
                        map.put("blood_group", blood_group);
                        map.put("mobile", mobile);
                        map.put("address", location);
                        map.put("description", description);
                        map.put("userId", userId);
                        map.put("key", key);

                        userdata.child(key).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(B_add_details.this, "Request Created Successfuly", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(B_add_details.this, B_Request_page.class);
                                        startActivity(i);
                                        Animatoo.INSTANCE.animateSlideLeft(B_add_details.this);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                }
            }
        });

        // Define the blood groups array within the same class
        String[] bloodGroups = new String[]{
                "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        };

        // Create an ArrayAdapter using the blood groups array and a default layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                bloodGroups
        );

        // Set the adapter to the AutoCompleteTextView
        txt_type.setAdapter(adapter);
    }

    //--------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(B_add_details.this);
        finish();
    }

    // Helper method to check if the internet connection is available
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
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