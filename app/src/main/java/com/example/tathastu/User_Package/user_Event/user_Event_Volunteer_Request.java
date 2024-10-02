package com.example.tathastu.User_Package.user_Event;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_DashBoard.profile_getset;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class user_Event_Volunteer_Request extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private ConnectivityReceiver connectivityReceiver;

    DatabaseReference reference, reference1;
    //    FirebaseStorage storage;
//    StorageReference storageReference;
    String iename, ieparticipated, ietotal,key;

    TextInputEditText txt_vname, txt_vemail, txt_vcno, txt_vaddress, txt_vage;
    Button btn_volunteer;

    SharedPreferences sharedPreferences;

    MaterialCheckBox chk_ex_event_user;

    boolean validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_event_volunteer_request);

        sharedPreferences = getSharedPreferences("USER",MODE_PRIVATE);


        txt_vname = findViewById(R.id.txt_vname);
        txt_vemail = findViewById(R.id.txt_vemail);
        txt_vcno = findViewById(R.id.txt_vcno);
        txt_vaddress = findViewById(R.id.txt_vaddress);
        txt_vage = findViewById(R.id.txt_vage);
        btn_volunteer = (Button) findViewById(R.id.btn_volunteer);
        chk_ex_event_user = findViewById(R.id.chk_ex_event_user);


        chk_ex_event_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (chk_ex_event_user.isChecked()) {

                    SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
                    String userId = sharedPreferences1.getString("userId","");

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(userId);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                profile_getset data = snapshot.getValue(profile_getset.class);
                                String fname = data.getFname();
                                String lname=data.getLname();
                                String email = data.getEmail();
                                String mno=data.getMobile();

                                txt_vname.setText(fname+" "+lname);
                                txt_vemail.setText(email);
                                txt_vcno.setText(mno);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {

                    txt_vname.setText("");
                    txt_vcno.setText("");
                    txt_vemail.setText("");

                }

            }
        });

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

                Animatoo.INSTANCE.animateSlideRight(user_Event_Volunteer_Request.this);
            }
        });


        String vname = txt_vname.getText().toString();
        String vemail = txt_vemail.getText().toString();
        String vcno = txt_vcno.getText().toString();
        String vaddress = txt_vaddress.getText().toString();
        String vage = txt_vage.getText().toString();

        Intent intent = this.getIntent();
        key = intent.getStringExtra("key");

        reference = FirebaseDatabase.getInstance().getReference().child("events").child(key);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                events events = snapshot.getValue(events.class);
                if (events != null) {
                    iename = events.getName();
                    ieparticipated = events.getVolunteer_get();
                    ietotal = events.getTotal_volunteer();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btn_volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check validation
                ProgressDialog progressDialog = new ProgressDialog(new ContextThemeWrapper(user_Event_Volunteer_Request.this,R.style.CustomProgressDialog));
                progressDialog.setTitle("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                validate = CheckValidation();

                if (validate) {

                    // checking if event is full

                    if (Integer.parseInt(ietotal) <= Integer.parseInt(ieparticipated)) {
                        progressDialog.dismiss(); // Dismiss progress dialog
                        Toast.makeText(user_Event_Volunteer_Request.this, "Sorry, the event is full", Toast.LENGTH_SHORT).show();
                    } else {
                        String message;
                        if (Integer.parseInt(txt_vage.getText().toString()) < 18) {
                            message = "Volunteers under age of 18 are assign and overlooked by guide \n\nAre you sure, you want to participate in " + iename + " event.";
                        } else {
                            message = "Are you sure, you want to participate in " + iename + " event.";
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(user_Event_Volunteer_Request.this, R.style.CustomAlertDialog));
                        builder.setMessage(message);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // storing data in firebase

                                String vid= String.valueOf(System.currentTimeMillis());

                                String id = String.valueOf(new Random().nextInt(99999));
                                String userId = sharedPreferences.getString("userId","");
                                String vname = txt_vname.getText().toString();
                                String vemail = txt_vemail.getText().toString();
                                String vcontactno = txt_vcno.getText().toString();
                                String vaddress = txt_vaddress.getText().toString();
                                String vage = txt_vage.getText().toString();

                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        reference.child("Volunteers").child(vid).child("vid").setValue(id);
                                        reference.child("Volunteers").child(vid).child("name").setValue(vname);
                                        reference.child("Volunteers").child(vid).child("email").setValue(vemail);
                                        reference.child("Volunteers").child(vid).child("contact_no").setValue(vcontactno);
                                        reference.child("Volunteers").child(vid).child("address").setValue(vaddress);
                                        reference.child("Volunteers").child(vid).child("age").setValue(vage);
                                        reference.child("Volunteers").child(vid).child("userId").setValue(userId);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                                // also add volunteer id in user data


                                // changing the number of volunteer participated

                                reference1 = FirebaseDatabase.getInstance().getReference().child("events").child(key).child("Volunteers");
                                reference1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dsnapshot) {
                                        Integer v = (int) dsnapshot.getChildrenCount();

                                        reference.child("volunteer_get").setValue(Integer.toString(v));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                // Dismiss progress dialog after 3 seconds
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(user_Event_Volunteer_Request.this, "Participation is successful", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }, 3000); // 3 seconds delay
                            }
                        });
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.dismiss(); // Dismiss progress dialog

                                finish();

                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                } else {
                    progressDialog.dismiss(); // Dismiss progress dialog
                }

            }
        });



    }
//--------------------------------------------------------------------------------------
@Override
public void onBackPressed() {
    super.onBackPressed();
    Animatoo.INSTANCE.animateSlideRight(this);
    finish();
}

    // Helper method to validate Gmail, Yahoo, and Outlook addresses
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com)$";
        return email.matches(emailPattern);
    }

    private boolean CheckValidation() {
        if (txt_vname.length() < 3) {
            txt_vname.setError("At least add 3 characters");
            return false;
        }

        if (!isValidEmail(txt_vemail.getText().toString())) {
            txt_vemail.setError("Please enter a valid email address (Gmail, Yahoo, or Outlook)");
            return false;
        }

        if (txt_vemail.length() < 10) {
            txt_vemail.setError("At least add 10 characters");
            return false;
        }

        if (txt_vcno.length() == 0) {
            txt_vcno.setError("Field Required");
            return false;
        }

        if (txt_vcno.length() != 13) {
            txt_vcno.setError("Enter a valid contact number");
            return false;
        }

        if (txt_vaddress.length() < 10) {
            txt_vaddress.setError("At least add 10 characters");
            return false;
        }

        if (txt_vage.length() == 0) {
            txt_vage.setError("Field Required");
            return false;
        }

        if (txt_vage.length() < 2) {
            txt_vage.setError("Not Qualified");
            return false;
        }

        return true;
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