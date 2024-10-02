package com.example.tathastu.NGO_Package.NGO_Campaign;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Campaign.campaigns;
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

public class NGO_Edit_New_Campaign_Request extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private ConnectivityReceiver connectivityReceiver;

    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;

    ExtendedFloatingActionButton btn_changeimage;
    Button btn_complete;
    ImageView cimage;
    TextInputEditText txt_campaignname, txt_description, txt_organizer, txt_organizermno;
    int Select_Picture = 200;
    Boolean checkImage = true, checkAlert = true;
    Boolean validate;
    Uri selectImageUri;
    public String description, ioname, iocontact, icdonated, imageUrl, icname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_edit_new_campaign_request);

        btn_changeimage = findViewById(R.id.btn_changeimage);
        cimage = findViewById(R.id.cimage);
        txt_campaignname = findViewById(R.id.txt_campaignname);
        txt_description = findViewById(R.id.txt_description);
        txt_organizer = findViewById(R.id.txt_organizer);
        txt_organizermno = findViewById(R.id.txt_organizermno);
        btn_complete = findViewById(R.id.btn_complete);

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
//                Intent i =new Intent(Event_Notifications_Screen.this, DashBoard_Screen.class);
//                startActivity(i);

                finish();
                Animatoo.INSTANCE.animateSlideRight(NGO_Edit_New_Campaign_Request.this);
            }
        });

        Intent intent = this.getIntent();
        String key = intent.getStringExtra("key");

        reference = FirebaseDatabase.getInstance().getReference().child("campaigns").child(key);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                campaigns campaign = snapshot.getValue(campaigns.class);
                if (campaign != null) {
                    description = campaign.getDescription();
                    ioname = campaign.getOrganizer_name();
                    iocontact = campaign.getOrganizer_contact();
                    icdonated = campaign.getDonation_received();
                    imageUrl = campaign.getImageUrl();
                    icname = campaign.getName();

                    txt_description.setText(description);
                    txt_campaignname.setText(icname);
                    txt_organizer.setText(ioname);
                    txt_organizermno.setText(iocontact);
                    Glide.with(NGO_Edit_New_Campaign_Request.this).load(imageUrl).into(cimage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_changeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate = CheckValidation();

                if (validate) {
                    if (cimage.getDrawable() == null) {
                        Toast.makeText(NGO_Edit_New_Campaign_Request.this, " Select Campaign Image ", Toast.LENGTH_SHORT).show();
                    } else if (txt_campaignname.getText().toString().equals(icname)
                            && txt_description.getText().toString().equals(description)
                            && txt_organizer.getText().toString().equals(ioname)
                            && txt_organizermno.getText().toString().equals(iocontact)
                            && checkImage) {
                        Toast.makeText(NGO_Edit_New_Campaign_Request.this, " No Changes Made ", Toast.LENGTH_SHORT).show();
                    } else {
                        String name = txt_campaignname.getText().toString();
                        String message = "Are you sure, you want to change " + name + "\'s details.";


                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(NGO_Edit_New_Campaign_Request.this,R.style.CustomAlertDialog));
                        builder.setMessage(message);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                // coding to store data

                                String cname = txt_campaignname.getText().toString();
                                String cdesc = txt_description.getText().toString();
                                String coname = txt_organizer.getText().toString();
                                String cocontact = txt_organizermno.getText().toString();


                                reference.child("name").setValue(cname);
                                reference.child("description").setValue(cdesc);
                                reference.child("organizer_name").setValue(coname);
                                reference.child("organizer_contact").setValue(cocontact);


                                // change image in firebase storage
                                if (!checkImage) {
                                    if (selectImageUri != null) {
                                        final ProgressDialog p = new ProgressDialog(new ContextThemeWrapper(NGO_Edit_New_Campaign_Request.this, R.style.CustomProgressDialog));
                                        p.setTitle("Uploading.....");
                                        p.show();
                                        StorageReference reference1 = storageReference.child("campaign/" + cname);

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


                                                AlertDialog.Builder br = new AlertDialog.Builder(NGO_Edit_New_Campaign_Request.this);
                                                br.setMessage(cname + " Campaign is successfully Edited.");
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
                                                Toast.makeText(NGO_Edit_New_Campaign_Request.this, "Image uploading fail", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                } else {
                                    AlertDialog.Builder br = new AlertDialog.Builder(NGO_Edit_New_Campaign_Request.this);
                                    br.setMessage(name + "\'s details is successfully changed.");
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
    }
//-------------------------------------------------------------------------------------------------------------------


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(NGO_Edit_New_Campaign_Request.this);
        finish();
    }

    private boolean CheckValidation() {
        //txt_campaignname,txt_description,txt_organizer,txt_organizermno,txt_targetamount;

        if (txt_campaignname.length() < 3) {
            txt_campaignname.setError("Atleast add 3 characters");
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

        if (txt_organizermno.length() == 0) {
            txt_organizermno.setError("Field Required");
            return false;
        }

        if (txt_organizermno.length() != 10) {
            txt_organizermno.setError("Invalid Number");
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
                    cimage.setImageURI(selectImageUri);

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