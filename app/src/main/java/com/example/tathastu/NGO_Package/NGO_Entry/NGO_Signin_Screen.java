package com.example.tathastu.NGO_Package.NGO_Entry;

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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.NGO_Package.NGO_Profile.NGO_Profile_Model;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Common_Screens.Terms_C_activity;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NGO_Signin_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private ConnectivityReceiver connectivityReceiver;
    private LinearLayout signin_LinearLayout_ngo;
    public ExtendedFloatingActionButton BTN_sigin_ngo;
    public TextView BTN_signin_login_ngo, BTN_signin_TC_ngo;
    public TextInputEditText txt_Signin_Fname_ngo, txt_Signinemail_ngo, txt_Signinmno_ngo,txt_Signinmno_optional_ngo, txt_Signinaddress_ngo, txt_Signinpwd_ngo, txt_Signincpwd_ngo;
    public TextInputLayout txtlayout_Signin_fname_ngo, txtlayout_Signin_email_ngo, txtlayout_Signin_mno_ngo, txtlayout_Signin_address_ngo, txt_Signincat_ngo, txtlayout_Signin_pwd_ngo, txtlayout_Signin_cpwd_ngo;
    MaterialAutoCompleteTextView txt_type_ngo;
    public CheckBox ckbox_ngo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_signin_screen);

        BTN_signin_TC_ngo = findViewById(R.id.BTN_signin_TC_ngo);
        BTN_sigin_ngo = findViewById(R.id.BTN_signin_ngo);
        BTN_signin_login_ngo = findViewById(R.id.BTN_signin_login_ngo);
        txt_Signin_Fname_ngo = findViewById(R.id.txt_Signin_Fname_ngo);
        txt_Signinemail_ngo = findViewById(R.id.txt_Signinemail_ngo);
        txt_Signinmno_ngo = findViewById(R.id.txt_Signinmno_ngo);
        txt_Signinmno_optional_ngo=findViewById(R.id.txt_Signinmno_optional_ngo);
        txt_Signinaddress_ngo = findViewById(R.id.txt_Signinaddress_ngo);
        txt_Signinpwd_ngo = findViewById(R.id.txt_Signinpwd_ngo);
        txt_Signincpwd_ngo = findViewById(R.id.txt_Signincpwd_ngo);
        txt_type_ngo=findViewById(R.id.txt_type_ngo);
        signin_LinearLayout_ngo = findViewById(R.id.signin_parentlayout_ngo);

        ckbox_ngo = findViewById(R.id.chk_signintc_ngo);
        txtlayout_Signin_fname_ngo = findViewById(R.id.txtlayout_Signin_fname_ngo);
        txtlayout_Signin_email_ngo = findViewById(R.id.txtlayout_Signin_email_ngo);
        txtlayout_Signin_mno_ngo = findViewById(R.id.txtlayout_Signin_email_ngo);
        txtlayout_Signin_address_ngo = findViewById(R.id.txtlayout_Signin_mno_ngo);
        txt_Signincat_ngo = findViewById(R.id.txt_Signincat_ngo);
        txtlayout_Signin_pwd_ngo = findViewById(R.id.txtlayout_Signin_pwd_ngo);
        txtlayout_Signin_cpwd_ngo = findViewById(R.id.txtlayout_Signin_cpwd_ngo);

        // Define the blood groups array within the same class
        String[] bloodGroups = new String[]{
                "Food", "Blood", "Animal Welfare", "Cloth", "Woman Development","Education"
        };

        // Create an ArrayAdapter using the blood groups array and a default layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                bloodGroups
        );

        // Set the adapter to the AutoCompleteTextView
        txt_type_ngo.setAdapter(adapter);


        // Set up touch listener for the parent layout
        signin_LinearLayout_ngo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Clear focus from EditText when touched outside
                txt_Signin_Fname_ngo.clearFocus();
                txt_Signinemail_ngo.clearFocus();
                txt_Signinmno_ngo.clearFocus();
                txt_Signinmno_optional_ngo.clearFocus();
                txt_Signinaddress_ngo.clearFocus();
                txt_Signinpwd_ngo.clearFocus();
                txt_Signincpwd_ngo.clearFocus();
                txt_type_ngo.clearFocus();


                hideSoftKeyboard(signin_LinearLayout_ngo);
                return false;
            }
        });

        FloatingActionButton BTN_back_ngo = findViewById(R.id.BTN_back_ngo);
        //BACK
        BTN_back_ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Animatoo.INSTANCE.animateSlideRight(NGO_Signin_Screen.this);
            }
        });

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //Terms & Conditions
        BTN_signin_TC_ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    Intent i = new Intent(NGO_Signin_Screen.this, Terms_C_activity.class);
                    i.putExtra("source", "signin");
                    startActivity(i);
                    Animatoo.INSTANCE.animateSlideUp(NGO_Signin_Screen.this);
                }
            }
        });

        //Signin
        BTN_sigin_ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt_Signin_Fname_ngo.clearFocus();
                txt_Signinemail_ngo.clearFocus();
                txt_Signinmno_ngo.clearFocus();
                txt_Signinmno_optional_ngo.clearFocus();
                txt_Signinaddress_ngo.clearFocus();
                txt_Signinpwd_ngo.clearFocus();
                txt_Signincpwd_ngo.clearFocus();
                txt_type_ngo.clearFocus();

                String fname = txt_Signin_Fname_ngo.getText().toString();
                String email = txt_Signinemail_ngo.getText().toString();
                String mno = txt_Signinmno_ngo.getText().toString();
                String omno=txt_Signinmno_optional_ngo.getText().toString();
                String address = txt_Signinaddress_ngo.getText().toString();
                String pwd = txt_Signinpwd_ngo.getText().toString();
                String cpwd = txt_Signincpwd_ngo.getText().toString();
                String type = txt_type_ngo.getText().toString();

                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    if (fname.isEmpty() || email.isEmpty() || mno.isEmpty() || address.isEmpty() || pwd.isEmpty() || cpwd.isEmpty() || type.isEmpty()) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter required details...");
                    } else if (mno.length() < 10) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid mobile number...");
                    } else if (!isValidEmail(email)) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid email address (Gmail, Yahoo, or Outlook)...");
                    } else if (pwd.length() < 8 || cpwd.length() < 8) {
                        showSnackbar(findViewById(android.R.id.content), "Password should be 8 characters long...");
                    } else if (!isValidPassword(pwd)) {
                        showSnackbar(findViewById(android.R.id.content), "Password must include at least one uppercase letter, one lowercase letter, one special character, and one digit...");
                    } else if (!pwd.equals(cpwd)) {
                        showSnackbar(findViewById(android.R.id.content), "Confirm password doesn't match...");
                    } else if (ckbox_ngo.isChecked()) {
                        final int[] flag = {0};
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ngo");

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()){

                                    for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                        NGO_Profile_Model data = snapshot1.getValue(NGO_Profile_Model.class);
                                        String mobile = data.getMobile();
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
                                            if (("+91"+mno).equals(mobile) ) {
                                                flag[0] =1;
                                                break;
                                            } else {
                                                flag[0]=0;
                                            }
                                        }
                                    }
                                    if(flag[0]==1)
                                    {
                                        showSnackbar(findViewById(android.R.id.content), "NGO with this mobile number and email already exists...");
                                    }else{
                                        verification(fname,email, mno,omno,address,type,cpwd);
                                    }
                                }else{
                                    verification(fname,email, mno,omno,address,type,cpwd);
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
        BTN_signin_login_ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    Intent i = new Intent(NGO_Signin_Screen.this, NGO_Login_Screen.class);
                    startActivity(i);
                    Animatoo.INSTANCE.animateSlideRight(NGO_Signin_Screen.this);
                }
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


    private void verification(String fname, String email,String mno,String omno,String address,String type,String cpwd) {

        String ch = "register";

        Intent i = new Intent(NGO_Signin_Screen.this, NGO_OTP_Screen.class);
        i.putExtra("source", "signin");
        i.putExtra("fname",fname);
        i.putExtra("email",email);
        i.putExtra("mno", "+91" + mno);
        if(omno.equals(""))
        {
            i.putExtra("omno","");
        }else{
            i.putExtra("omno", "+91" + omno);
        }
        i.putExtra("address",address);
        i.putExtra("type",type);
        i.putExtra("cpwd",cpwd);
        i.putExtra("check",ch);
        startActivity(i);

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
