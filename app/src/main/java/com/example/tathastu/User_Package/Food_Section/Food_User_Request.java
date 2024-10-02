package com.example.tathastu.User_Package.Food_Section;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.Food_Section.History.Food_user_History;
import com.example.tathastu.User_Package.user_DashBoard.profile_getset;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Food_User_Request extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    TextInputEditText edtName,edtmno,edtemail,edtaddress,edtdesc;

    LinearLayout linear_layout_food;
    Button BTN_add;

   MaterialCheckBox chk_user;
    ProgressDialog p;
    private ConnectivityReceiver connectivityReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_user_request);

        edtName = findViewById(R.id.txt_name);
        edtmno = findViewById(R.id.txt_mno);
        edtemail = findViewById(R.id.txt_email);
        edtaddress = findViewById(R.id.txt_location);
        edtdesc = findViewById(R.id.txt_note);
        linear_layout_food = findViewById(R.id.linear_layout_food);
        BTN_add = findViewById(R.id.BTN_add); // replace 'your_button_id' with the actual ID
        chk_user = findViewById(R.id.chk_ex_user);

        // Set up touch listener for the parent layout
        linear_layout_food.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Clear focus from EditText when touched outside
                edtName.clearFocus();
                edtmno.clearFocus();
                edtemail.clearFocus();
                edtaddress.clearFocus();
                edtdesc.clearFocus();


                hideSoftKeyboard(linear_layout_food);
                return false;
            }
        });

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
//                Intent i =new Intent(History_Screen.this, DashBoard_Screen.class);
//                startActivity(i);
                finish();
                Animatoo.INSTANCE.animateSlideRight(Food_User_Request.this);
            }
        });

        chk_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chk_user.isChecked()) {

                    SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
                    String userId = sharedPreferences1.getString("userId","");

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(userId);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                profile_getset data = snapshot.getValue(profile_getset.class);
                                String fname = data.getFname();
                                String lname=data.getLname();
                                String email = data.getEmail();
                                String mno=data.getMobile();

                                edtName.setText(fname+" "+lname);
                                edtemail.setText(email);
                                edtmno.setText(mno);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {

                    edtName.setText("");
                    edtemail.setText("");
                    edtmno.setText("");

                }

            }
        });

        //ADD
        BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtName.clearFocus();
                edtmno.clearFocus();
                edtemail.clearFocus();
                edtaddress.clearFocus();
                edtdesc.clearFocus();

                String fname = edtName.getText().toString();
                String mno = edtmno.getText().toString();
                String email = edtemail.getText().toString();
                String address = edtaddress.getText().toString();
                String desc = edtdesc.getText().toString();
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    if (fname.isEmpty() || mno.isEmpty() || email.isEmpty() || address.isEmpty() || desc.isEmpty()) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter required details...");
                    } else if (mno.length() < 13) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid mobile number...");
                    } else if (!isValidEmail(email)) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid email address (Gmail, Yahoo, or Outlook)...");
                    } else {
                        SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
                        String userId = sharedPreferences1.getString("userId","");

                        String key= String.valueOf(System.currentTimeMillis());


                        DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("food");

                        Map<String, Object> map = new HashMap<>();
                        map.put("name", fname);
                        map.put("email",email);
                        map.put("mobile", mno);
                        map.put("address",address);
                        map.put("description",desc);
                        map.put("userId",userId);
                        map.put("key",key);


                        userdata.child(key).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(Food_User_Request.this, "Request Created Successfuly", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Food_User_Request.this, Food_user_History.class);
                                startActivity(i);
                                Animatoo.INSTANCE.animateSlideLeft(Food_User_Request.this);
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
    }
    //--------------------------------------------------------------------------------------------


    private void showDatePicker(final TextInputEditText editText) {
        Calendar newCalendar = Calendar.getInstance();
        int year = newCalendar.get(Calendar.YEAR);
        int month = newCalendar.get(Calendar.MONTH);
        int dayOfMonth = newCalendar.get(Calendar.DAY_OF_MONTH);
    }
    // Helper method to validate Gmail, Yahoo, and Outlook addresses
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com)$";
        return email.matches(emailPattern);
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

    //HIDE THE KEYBOARD
    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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