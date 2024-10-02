package com.example.tathastu.NGO_Package.NGO_Food_Camp.Request;

import static com.example.tathastu.R.style.CustomDatePickerStyle;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.NGO_Package.NGO_Food_Camp.History.NGO_food_camp_history;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NGO_food_camp_request extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,ConnectivityReceiver.ConnectivityReceiverListener {

    TextInputEditText edtName,edtstartdate,edtenddate,edtmno,edtemail,edtaddress,edtdesc;
    private SimpleDateFormat dateFormatter;
    LinearLayout linear_layout_food;
    Button BTN_add;
    private DatePickerDialog datePickerDialog;
    private ConnectivityReceiver connectivityReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_food_camp_request);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        edtName = findViewById(R.id.txt_name);
        edtstartdate = findViewById(R.id.txt_start_date);
        edtenddate = findViewById(R.id.txt_end_date);
        edtmno = findViewById(R.id.txt_mno);
        edtemail = findViewById(R.id.txt_email);
        edtaddress = findViewById(R.id.txt_location);
        edtdesc = findViewById(R.id.txt_note);
        linear_layout_food = findViewById(R.id.linear_layout_food);
        BTN_add = findViewById(R.id.BTN_add); // replace 'your_button_id' with the actual ID

        datePickerDialog = new DatePickerDialog(this, R.style.CustomDatePickerStyle, this, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        // Set up touch listener for the parent layout
        linear_layout_food.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Clear focus from EditText when touched outside
                edtName.clearFocus();
                edtstartdate.clearFocus();
                edtenddate.clearFocus();
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

                finish();  Animatoo.INSTANCE.animateSlideRight(NGO_food_camp_request.this);
            }
        });

// START DATE
        edtstartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(edtstartdate);
                showDatePicker(edtstartdate);
            }
        });

// END DATE
        edtenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(edtenddate);
                showDatePicker(edtenddate);
            }
        });


        //ADD
        BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtName.clearFocus();
                edtstartdate.clearFocus();
                edtenddate.clearFocus();
                edtmno.clearFocus();
                edtemail.clearFocus();
                edtaddress.clearFocus();
                edtdesc.clearFocus();

                String fname = edtName.getText().toString();
                String sdate = edtstartdate.getText().toString();
                String edate = edtenddate.getText().toString();
                String mno = edtmno.getText().toString();
                String email = edtemail.getText().toString();
                String address = edtaddress.getText().toString();
                String desc = edtdesc.getText().toString();

                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    if (fname.isEmpty() || sdate.isEmpty() || edate.isEmpty() || mno.isEmpty() || email.isEmpty() || address.isEmpty() || desc.isEmpty()) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter required details...");
                    } else if (mno.length() < 10) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid mobile number...");
                    } else if (!isValidEmail(email)) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid email address (Gmail, Yahoo, or Outlook)...");
                    } else {

                        SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
                        String userId = sharedPreferences1.getString("userId","");

                        String key= String.valueOf(System.currentTimeMillis());


                        DatabaseReference userdata = FirebaseDatabase.getInstance().getReference("ngo_food");

                        Map<String, Object> map = new HashMap<>();
                        map.put("ngo_name", fname);
                        map.put("start_date",sdate);
                        map.put("end_date", edate);
                        map.put("mobile",mno);
                        map.put("email",email);
                        map.put("c_address",address);
                        map.put("description",desc);
                        map.put("userId",userId);
                        map.put("key",key);


                        userdata.child(key).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(NGO_food_camp_request.this, "Request Created Successfuly", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(NGO_food_camp_request.this, NGO_food_camp_history.class);
                                        startActivity(i);
                                        Animatoo.INSTANCE.animateSlideLeft(NGO_food_camp_request.this);

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

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                NGO_food_camp_request.this,
                CustomDatePickerStyle,
                NGO_food_camp_request.this,
                year, month, dayOfMonth
        );

        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                // Set the selected date to the specified EditText
                editText.setText(dateFormatter.format(newDate.getTime()));
            }
        });

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);

        // Set the selected date to the appropriate EditText
        if (edtstartdate.isFocused()) {
            edtstartdate.setText(dateFormatter.format(newDate.getTime()));
        } else if (edtenddate.isFocused()) {
            edtenddate.setText(dateFormatter.format(newDate.getTime()));
        }
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