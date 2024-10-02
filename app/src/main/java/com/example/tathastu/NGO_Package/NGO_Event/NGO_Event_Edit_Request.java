package com.example.tathastu.NGO_Package.NGO_Event;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.tathastu.NGO_Package.NGO_Campaign.NGO_Add_New_Campaign_Request;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Event.events;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class NGO_Event_Edit_Request extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private ConnectivityReceiver connectivityReceiver;

    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;
    TextInputEditText txt_eventdate, txt_eventname, txt_description, txt_organizer, txt_organizermno, txt_eventaddress, txt_eventcity, txt_vtotal;
    ExtendedFloatingActionButton btn_addimage;
    Button btn_confirm;
    ImageView eimage;
    int Select_Picture = 200;
    boolean validate;
    Boolean checkImage = true, checkAlert = true;
    Uri selectImageUri;

    public String iename, iedes, ioname, ieaddress, iecity, iedate, ietotal, imageUrl, ieparticipated,key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_event_edit_request);

        txt_eventdate = findViewById(R.id.txt_eventdate);
        txt_eventname = findViewById(R.id.txt_eventname);
        txt_description = findViewById(R.id.txt_description);
        txt_organizer = findViewById(R.id.txt_organizer);
        txt_eventaddress = findViewById(R.id.txt_eventaddress);
        txt_eventcity = findViewById(R.id.txt_eventcity);
        txt_vtotal = findViewById(R.id.txt_vtotal);
        eimage = findViewById(R.id.eimage);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_addimage = findViewById(R.id.btn_addimage);
        //txt_organizermno = findViewById(R.id.txt_organizermno);

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        FloatingActionButton BTN_back = findViewById(R.id.BTN_back);
        // BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish(); // Finish the activity when the back button is clicked  Animatoo.INSTANCE.animateSlideRight(NGO_Event_Edit_Request.this);
            }
        });


        Intent intent = this.getIntent();
        key = intent.getStringExtra("key");

        reference = FirebaseDatabase.getInstance().getReference().child("events").child(key);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                events events = snapshot.getValue(events.class);
                if (events != null) {
                    iename = events.getName();
                    iedes = events.getDescription();
                    ioname = events.getOrganizer_name();
                    ieaddress = events.getAddress();
                    iecity = events.getCity();
                    iedate = events.getDate();
                    ietotal = events.getTotal_volunteer();
                    ieparticipated = events.getVolunteer_get();

                    imageUrl = events.getImageUrl();

                    txt_eventname.setText(iename);
                    txt_description.setText(iedes);
                    txt_organizer.setText(ioname);
                    txt_eventaddress.setText(ieaddress);
                    txt_eventcity.setText(iecity);
                    txt_eventdate.setText(iedate);
                    txt_vtotal.setText(ietotal);
                    Glide.with(NGO_Event_Edit_Request.this).load(imageUrl).into(eimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        txt_eventdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar eventCalendar = Calendar.getInstance();

                int year = eventCalendar.get(Calendar.YEAR);
                int month = eventCalendar.get(Calendar.MONTH);
                int day = eventCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        NGO_Event_Edit_Request.this,
                        R.style.CustomDatePickerStyle, // Apply the custom style here
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                txt_eventdate.setText(selectedDay + "-" + (selectedMonth + 1) + "-" + selectedYear);
                            }
                        },
                        year,
                        month,
                        day
                );

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate = CheckValidation();
                if (validate) {
                    if (eimage.getDrawable() == null) {
                        Toast.makeText(NGO_Event_Edit_Request.this, " Select Campaign Image ", Toast.LENGTH_SHORT).show();
                    } else if (txt_eventname.getText().toString().equals(iename)
                            && txt_description.getText().toString().equals(iedes)
                            && txt_organizer.getText().toString().equals(ioname)
                            && txt_eventaddress.getText().toString().equals(ieaddress)
                            && txt_eventcity.getText().toString().equals(iecity)
                            && txt_eventdate.getText().toString().equals(iedate)
                            && txt_vtotal.getText().toString().equals(ietotal)
                            && checkImage) {
                        Toast.makeText(NGO_Event_Edit_Request.this, " No Changes Made ", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(txt_vtotal.getText().toString()) < Integer.parseInt(ieparticipated)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(NGO_Event_Edit_Request.this,R.style.CustomAlertDialog));
                        builder.setMessage("Total number of volunteer is less than the number of volunteer we already get.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        txt_vtotal.setError("provide value greater than " + ieparticipated);
                    } else {
                        String name = txt_eventname.getText().toString();
                        String message = "Are you sure, you want to change " + name + "\'s details.";

                        AlertDialog.Builder builder = new AlertDialog.Builder(NGO_Event_Edit_Request.this);
                        builder.setMessage(message);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // coding to store data

                                String ename = txt_eventname.getText().toString();
                                String edesc = txt_description.getText().toString();
                                String eoname = txt_organizer.getText().toString();
                                String eaddress = txt_eventaddress.getText().toString();
                                String ecity = txt_eventcity.getText().toString();
                                String edate = txt_eventdate.getText().toString();
                                String etotalVolunteer = txt_vtotal.getText().toString();

                                reference.child("name").setValue(ename);
                                reference.child("description").setValue(edesc);
                                reference.child("organizer_name").setValue(eoname);
                                reference.child("address").setValue(eaddress);
                                reference.child("city").setValue(ecity);
                                reference.child("date").setValue(edate);
                                reference.child("total_volunteer").setValue(etotalVolunteer);

                                // change image in firebase storage
                                if (!checkImage) {
                                    if (selectImageUri != null) {
                                        final ProgressDialog  p = new ProgressDialog(new ContextThemeWrapper(NGO_Event_Edit_Request.this, R.style.CustomProgressDialog));
                                        p.setTitle("Uploading.....");
                                        p.show();
                                        StorageReference reference1 = storageReference.child("events/" + key);

                                        StorageReference r = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                                        r.delete();

                                        reference1.putFile(selectImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                p.dismiss();

                                                // storing image url in realtime database

                                                reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        reference.child("imageUrl").setValue(uri.toString());
                                                    }
                                                });


                                                AlertDialog.Builder br = new AlertDialog.Builder(NGO_Event_Edit_Request.this);
                                                br.setMessage(iename + " event is successfully Edited.");
                                                br.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        finish();
                                                    }
                                                });
                                                AlertDialog alertConfirm = br.create();
                                                alertConfirm.show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(NGO_Event_Edit_Request.this, "Image uploading fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                } else {
                                    AlertDialog.Builder br = new AlertDialog.Builder(NGO_Event_Edit_Request.this);
                                    br.setMessage(iename + "\'s details is successfully changed.");
                                    br.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    });
                                    AlertDialog alertConfirm = br.create();
                                    alertConfirm.show();
                                }

                            }
                        });
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                }
            }
        });

        btn_addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
    }

//--------------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(NGO_Event_Edit_Request.this);
        finish();
    }

    private boolean CheckValidation() {
        //txt_campaignname,txt_description,txt_organizer,txt_organizermno,txt_targetamount;

        if (txt_eventname.length() < 3) {
            txt_eventname.setError("Atleast add 3 characters");
            return false;
        }

        if (txt_description.length() < 10) {
            txt_description.setError("Atleast add 10 characters");
            return false;
        }

        if (txt_organizer.length() < 3) {
            txt_organizer.setError("Atleast add 3 characters");
            return false;
        }

        if (txt_eventaddress.length() < 3) {
            txt_eventaddress.setError("Atleast add 3 characters");
            return false;
        }

        if (txt_eventcity.length() < 3) {
            txt_eventcity.setError("Atleast add 3 characters");
            return false;
        }

        if (txt_vtotal.length() == 0) {
            txt_vtotal.setError("Field Required");
            return false;
        }

        if (txt_eventdate.length() == 0) {
            txt_eventdate.setError("Field Required");
            return false;
        }
        return true;
    }

    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), Select_Picture);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Select_Picture) {
                checkImage = false;
                checkAlert = false;
                selectImageUri = data.getData();
                if (null != selectImageUri) {
                    eimage.setImageURI(selectImageUri);
                }
            }
        }
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