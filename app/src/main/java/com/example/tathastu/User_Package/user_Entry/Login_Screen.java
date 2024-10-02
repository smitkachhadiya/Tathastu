package com.example.tathastu.User_Package.user_Entry;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.Common_Screens.Selection_Screen;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_DashBoard.profile_getset;
import com.example.tathastu.User_Package.user_Entry.Forgot_PWD.Reset_pwd_mno;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public ExtendedFloatingActionButton BTN_login ;

    private LinearLayout login_parentLayout;

    public MaterialTextView BTN_login_sigin;
    public TextInputEditText edtmno,edtpwd;
    TextInputLayout txtlayout_login_mno;
    private ConnectivityReceiver connectivityReceiver;
    public  static final String PREFS_NAME = "myprefs";
    public  static final String KEY_FIRST_TIME_LOGIN = "FirstTimeLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        BTN_login = findViewById(R.id.BTN_login);
        BTN_login_sigin = findViewById(R.id.BTN_login_signin);
        edtmno = findViewById(R.id.txt_Loginmno);
        edtpwd=findViewById(R.id.txt_login_pwd);
        txtlayout_login_mno = findViewById(R.id.txtlayout_login_mno);
        login_parentLayout=findViewById(R.id.login_parentlayout);

        // Set up touch listener for the parent layout
        login_parentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Clear focus from EditText when touched outside
                edtmno.clearFocus();
                edtpwd.clearFocus();


                hideSoftKeyboard(login_parentLayout);
                return false;
            }
        });

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mob = edtmno.getText().toString();
                String pwd=edtpwd.getText().toString();
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content),"Please check your internet connection...");
                    return;
                }else {
                    if (mob.isEmpty()) {
                        // Set an error message
                        showSnackbar(findViewById(android.R.id.content), "Please enter mobile number...");
                    }else if (mob.length() < 10) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid mobile number...");
                    }else if (pwd.isEmpty()) {
                        // Set an error message
                        showSnackbar(findViewById(android.R.id.content), "Please enter password...");
                    }
                    else if (pwd.length() < 8) {
                        showSnackbar(findViewById(android.R.id.content), "Password should be 8 characters long...");
                    } else if (!isValidPassword(pwd)) {
                        showSnackbar(findViewById(android.R.id.content), "Password must include at least one uppercase letter, one lowercase letter, one special character, and one digit...");
                    } else if (!isValidNumber(mob)) {
                        showSnackbar(findViewById(android.R.id.content),"Please enter a valid mobile number...");

                    } else {

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");

                        profile_getset[] getset = new profile_getset[1];

                        int flag[]={0};

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()){

                                    for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                        getset[0] = snapshot1.getValue(profile_getset.class);
                                        String mno = getset[0].getMobile();
                                        String pass = getset[0].getPassword();

                                        if (("+91"+mob).equals(mno) && pwd.equals(pass)) {
                                            flag[0]=1;
                                            break;

                                        } else {
                                            flag[0]=0;
                                        }

                                    }
                                    if(flag[0]==1)
                                    {
                                        String email = getset[0].getEmail();
                                        String fname = getset[0].getFname();
                                        String lname = getset[0].getLname();
                                        String userID = getset[0].getUserId();

                                        Intent i = new Intent(Login_Screen.this, Otp_Screen.class);
                                        i.putExtra("source", "login");
                                        i.putExtra("mobile", "+91" + mob);
                                        i.putExtra("email",email);
                                        i.putExtra("fname",fname);
                                        i.putExtra("lname",lname);
                                        i.putExtra("userId",userID);
                                        i.putExtra("check","login");

                                        startActivity(i);
                                        Animatoo.INSTANCE.animateSlideLeft(Login_Screen.this);
                                    }else{
                                        Toast.makeText(Login_Screen.this, "you haven't registered yet", Toast.LENGTH_SHORT).show();

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }
        });



        BTN_login_sigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    Intent i = new Intent(Login_Screen.this, Signin_Screen.class);
                    startActivity(i);
                    Animatoo.INSTANCE.animateSlideLeft(Login_Screen.this);
                }
            }
        });
    }

    //--------------------------------------------------------------------------------------------


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(Login_Screen.this, Selection_Screen.class);
        startActivity(i);
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }

    //HIDE THE KEYBOARD
    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    public void forgot_pwd_Screen(View view){
        startActivity(new Intent(Login_Screen.this, Reset_pwd_mno.class));
    }

    // Helper method to validate phone
    private boolean isValidNumber(String number) {
        String numberPattern = "\\b\\d{10}\\b";
        return number.matches(numberPattern);
    }

    // Helper method to validate password
    private boolean isValidPassword(String password) {
        // Password should have minimum 8 characters, 1 uppercase, 1 special character, 1 lowercase, and 1 number
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()-=_+{};:'\\\"<>,.?/\\\\|]).{8,}$";
        return password.matches(passwordPattern);
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
        snackbarLayout.setPadding(1,1,1,1);
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
