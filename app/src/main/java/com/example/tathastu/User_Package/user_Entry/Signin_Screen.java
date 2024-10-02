package com.example.tathastu.User_Package.user_Entry;

import static com.example.tathastu.R.style.CustomDatePickerStyle;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.NGO_Package.NGO_Profile.NGO_Profile_Model;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Common_Screens.Terms_C_activity;
import com.example.tathastu.User_Package.user_DashBoard.profile_getset;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Signin_Screen extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private ConnectivityReceiver connectivityReceiver;
    public TextInputEditText dob1;
    private SimpleDateFormat dateFormatter;

    private LinearLayout signin_LinearLayout;
    public ExtendedFloatingActionButton  BTN_sigin;
    public TextView BTN_signin_login,BTN_signin_TC;
    public TextInputEditText edtfname, edtlname, edtemail, edtmob, edtpwd, edtcpwd;
    public TextInputLayout txtlayout_Signin_fname, txtlayout_Signin_lname, txtlayout_Signin_email, txtlayout_Signin_mno, txtlayout_Signin_dob;
    public CheckBox ckbox;
    public int minYear;
    public int maxYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_screen);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dob1 = findViewById(R.id.txt_Signindob);
        BTN_signin_TC = findViewById(R.id.BTN_signin_TC);
        BTN_sigin = findViewById(R.id.BTN_signin);
        BTN_signin_login = findViewById(R.id.BTN_signin_login);
        edtfname = findViewById(R.id.txt_Signin_Fname);
        edtlname = findViewById(R.id.txt_Signin_Lname);
        edtemail = findViewById(R.id.txt_Signinemail);
        edtmob = findViewById(R.id.txt_Signinmno);
        edtpwd = findViewById(R.id.txt_Signinpwd);
        edtcpwd = findViewById(R.id.txt_Signincpwd);
        signin_LinearLayout=findViewById(R.id.signin_parentlayout);

        ckbox = findViewById(R.id.chk_signintc);
        txtlayout_Signin_fname = findViewById(R.id.txtlayout_Signin_fname);
        txtlayout_Signin_lname = findViewById(R.id.txtlayout_Signin_lname);
        txtlayout_Signin_email = findViewById(R.id.txtlayout_Signin_email);
        txtlayout_Signin_mno = findViewById(R.id.txtlayout_Signin_mno);
        txtlayout_Signin_dob = findViewById(R.id.txtlayout_Signin_dob);

        // Set up touch listener for the parent layout
        signin_LinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Clear focus from EditText when touched outside
                edtfname.clearFocus();
                edtlname.clearFocus();
                edtemail.clearFocus();
                edtmob.clearFocus();
                edtpwd.clearFocus();
                edtcpwd.clearFocus();
                dob1.clearFocus();


                hideSoftKeyboard(signin_LinearLayout);
                return false;
            }
        });

        FloatingActionButton BTN_back=findViewById(R.id.BTN_back);
        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i =new Intent(Signin_Screen.this, Login_Screen.class);
//                startActivity(i);
                finish();
                Animatoo.INSTANCE.animateSlideRight(Signin_Screen.this);
            }
        });

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //Terms & Conditions
        BTN_signin_TC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    Intent i = new Intent(Signin_Screen.this, Terms_C_activity.class);
                    i.putExtra("source","signin");
                    startActivity(i);
                    Animatoo.INSTANCE.animateSlideUp(Signin_Screen.this);
                }
            }
        });

        //Signin
        BTN_sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname = edtfname.getText().toString();
                String lname = edtlname.getText().toString();
                String email = edtemail.getText().toString();
                String mob = edtmob.getText().toString();
                String dob = dob1.getText().toString();
                String pwd = edtpwd.getText().toString();
                String cpwd = edtcpwd.getText().toString();
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || mob.isEmpty() || dob.isEmpty() || pwd.isEmpty() || cpwd.isEmpty()) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter required details...");
                    } else if (mob.length() < 10) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid mobile number...");
                    } else if (!isValidEmail(email)) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid email address (Gmail, Yahoo, or Outlook)...");
                    } else if (pwd.length() < 8 || cpwd.length() < 8) {
                        showSnackbar(findViewById(android.R.id.content), "Password should be 8 characters long...");
                    } else if (!isValidPassword(pwd)) {
                        showSnackbar(findViewById(android.R.id.content), "Password must include at least one uppercase letter, one lowercase letter, one special character, and one digit...");
                    } else if (!pwd.equals(cpwd)) {
                        showSnackbar(findViewById(android.R.id.content), "Confirm password doesn't match...");
                    } else if (ckbox.isChecked()) {
                        final int[] flag = {0};
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()){

                                    for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                        profile_getset data = snapshot1.getValue(profile_getset.class);
                                        String mobile1 = data.getMobile();
//                                        String email1=data.getEmail();

//                                        if(data!=null)
//                                        {
//                                            if (("+91"+mno).equals(mobile) || email.equals(email1)) {
//                                                flag[0] =1;
//                                                break;
//                                            } else {
//                                                flag[0]=0;
//                                            }
//                                        }

                                        if(data!=null)
                                        {
                                            if (("+91"+mob).equals(mobile1)) {
                                                flag[0] =1;
                                                break;
                                            } else {
                                                flag[0]=0;
                                            }
                                        }
                                    }
                                    if(flag[0]==1)
                                    {
                                        showSnackbar(findViewById(android.R.id.content), "Donor with this mobile number and email already exists...");
                                    }else{
                                        verification(fname, lname, email, mob, dob, cpwd);
                                    }
                                }else{
                                    verification(fname, lname, email, mob, dob, cpwd);
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        showSnackbar(findViewById(android.R.id.content), "Please accept the Terms & Conditions...");
                    }
                }
            }
        });


        //Signin_Login
        BTN_signin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    Intent i = new Intent(Signin_Screen.this, Login_Screen.class);
                    startActivity(i);
                    Animatoo.INSTANCE.animateSlideRight(Signin_Screen.this);
                }
            }
        });

        //DOB
        dob1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(dob1);
                showDatePicker();

            }
        });
    }
//----------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
//        Intent i=new Intent(Signin_Screen.this,Login_Screen.class);
//        startActivity(i);
        finish();
    }

    //HIDE THE KEYBOARD
    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Helper method to validate Gmail, Yahoo, and Outlook addresses
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com)$";
        return email.matches(emailPattern);
    }

    // Helper method to validate password
    private boolean isValidPassword(String password) {
        // Password should have minimum 8 characters, 1 uppercase, 1 special character, 1 lowercase, and 1 number
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()-=_+{};:'\\\"<>,.?/\\\\|]).{8,}$";
        return password.matches(passwordPattern);
    }

    //SHOW DATE PICKER
    private void showDatePicker() {
        Calendar newCalendar = Calendar.getInstance();
        int year = newCalendar.get(Calendar.YEAR);
        int month = newCalendar.get(Calendar.MONTH);
        int dayOfMonth = newCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Signin_Screen.this,
                CustomDatePickerStyle,
                //AlertDialog.THEME_HOLO_LIGHT,
                Signin_Screen.this,
                year, month, dayOfMonth
        );
        minYear = 1950;
        maxYear = Calendar.getInstance().get(Calendar.YEAR) - 18;

        DatePicker datePicker = datePickerDialog.getDatePicker();
        if (datePicker != null) {
            datePicker.setMinDate(getMinDate());
            datePicker.setMaxDate(getMaxDate());
        }
        datePickerDialog.show();
    }

    public long getMinDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(minYear, 0, 1);
        return calendar.getTimeInMillis();
    }

    public long getMaxDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(maxYear, 11, 31);
        return calendar.getTimeInMillis();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        Calendar newDate = Calendar.getInstance();
        newDate.set(year, monthOfYear, dayOfMonth);
        dob1.setText(dateFormatter.format(newDate.getTime()));
    }

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


    private void verification(String fname, String lname, String email, String mob, String dob, String cpwd) {

        String ch = "register";

        Intent i = new Intent(Signin_Screen.this, Otp_Screen.class);
        i.putExtra("source", "signin");
        i.putExtra("mobile", "+91" + mob);
        i.putExtra("fname",fname);
        i.putExtra("lname",lname);
        i.putExtra("email",email);
        i.putExtra("dob",dob);
        i.putExtra("password",cpwd);
        i.putExtra("check",ch);
        startActivity(i);
        Animatoo.INSTANCE.animateSlideLeft(Signin_Screen.this);

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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
        }
    }
}
