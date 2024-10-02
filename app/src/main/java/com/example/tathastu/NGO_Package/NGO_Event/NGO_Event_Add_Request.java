package com.example.tathastu.NGO_Package.NGO_Event;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.example.tathastu.User_Package.user_DashBoard.profile_getset;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NGO_Event_Add_Request extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private ConnectivityReceiver connectivityReceiver;

    FirebaseDatabase db;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;

    SharedPreferences sharedPreferences;
    TextInputEditText txt_eventdate,txt_eventname,txt_description,txt_organizer,txt_eventaddress,txt_eventcity,txt_vtotal;
    ExtendedFloatingActionButton btn_addimage;
    Button btn_add_event;
    ImageView eimage;
    Uri selectedImageUri;
    int Select_Picture = 200;
    boolean validate;

    ProgressDialog p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_event_add_request);

        sharedPreferences = getSharedPreferences("NGO",MODE_PRIVATE);

        txt_eventdate =  findViewById(R.id.txt_eventdate);
        txt_eventname =  findViewById(R.id.txt_eventname);
        txt_description =  findViewById(R.id.txt_description);
        txt_organizer =  findViewById(R.id.txt_organizer);
        txt_eventaddress =  findViewById(R.id.txt_eventaddress);
        txt_eventcity =  findViewById(R.id.txt_eventcity);
        txt_vtotal =findViewById(R.id.txt_vtotal);
        eimage = findViewById(R.id.eimage);
        btn_addimage =  findViewById(R.id.btn_addimage);
        btn_add_event = findViewById(R.id.btn_add_event);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseDatabase.getInstance();
        reference = db.getReference().child("events");

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
//                Intent i =new Intent(Event_Notifications_Screen.this, DashBoard_Screen.class);
//                startActivity(i);

                finish();  Animatoo.INSTANCE.animateSlideRight(NGO_Event_Add_Request.this);
            }
        });

        btn_addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Select Picture"),Select_Picture);
            }
        });


        btn_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate = CheckValidation();
                if(validate){
                    if (eimage.getDrawable() == null){
                        Toast.makeText(NGO_Event_Add_Request.this," Select Event Image ",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String name = txt_eventname.getText().toString();
                        String message = "Are you sure, you want to launch " + name +  " event.";

                        AlertDialog.Builder builder= new AlertDialog.Builder(NGO_Event_Add_Request.this);
                        builder.setMessage(message);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();

                                p = new ProgressDialog(new ContextThemeWrapper(NGO_Event_Add_Request.this, R.style.CustomProgressDialog));
                                p.setTitle("Uploading.....");
                                p.show();

                                //storing Image in Firebase Storage

                                if(selectedImageUri != null){
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference imageRef = storage.getReference().child("events/" + System.currentTimeMillis() + ".jpg");
                                    imageRef.putFile(selectedImageUri)
                                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        // storing image url in realtime database

                                                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {

                                                                p.hide();

                                                                addEvent(uri.toString());

                                                            }
                                                        });
                                                    }
                                                }
                                    });
                                }else {
                                    Toast.makeText(NGO_Event_Add_Request.this,"empty",Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }

                }
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
                        NGO_Event_Add_Request.this,
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
    }

    private void addEvent(String uri) {
        String key= String.valueOf(System.currentTimeMillis());
        String ename = txt_eventname.getText().toString();
        String edesc = txt_description.getText().toString();
        String eoname = txt_organizer.getText().toString();
        String eaddress = txt_eventaddress.getText().toString();
        String ecity = txt_eventcity.getText().toString();
        String edate = txt_eventdate.getText().toString();
        String etotalVolunteer = txt_vtotal.getText().toString();
        String cvolunteer = "0";

        Map<String,Object> data= new HashMap<>();

        data.put("name",ename);
        data.put("ngoId",sharedPreferences.getString("ngoId","").toString());
        data.put("description",edesc);
        data.put("organizer_name",eoname);
        data.put("address",eaddress);
        data.put("city",ecity);
        data.put("date",edate);
        data.put("total_volunteer",etotalVolunteer);
        data.put("volunteer_get",cvolunteer);
        data.put("key",key);
        data.put("imageUrl",uri.toString());

        reference.child(key)
                .setValue(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
//                                Toast.makeText(NGO_Event_Add_Request.this, "Data Inserted.", Toast.LENGTH_SHORT).show();
                                sendNotify();

                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(NGO_Event_Add_Request.this, "Failed To Insert Data.", Toast.LENGTH_SHORT).show();
                                    }
                                });


    }

    private void sendNotify(){
       DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
        reference1.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    for (DataSnapshot snapshot1 : snapshot.getChildren()){

                        profile_getset getset = snapshot1.getValue(profile_getset.class);
                        String token = getset.getToken();
                        sendNotification("New Event Launched !!",txt_eventname.getText().toString(),token);
                    }
                }
                Toast.makeText(NGO_Event_Add_Request.this, "Event Launched Successfully...", Toast.LENGTH_SHORT).show();
                p.hide();
                startActivity(new Intent(NGO_Event_Add_Request.this,NGO_event_notify.class));
                finish();
                Animatoo.INSTANCE.animateSlideLeft(NGO_Event_Add_Request.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

//-------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(NGO_Event_Add_Request.this);
        finish();
    }

    private void sendNotification(String title,String body,String token) {
        try{
            JSONObject jsonObject = new JSONObject();

            JSONObject notificationObject = new JSONObject();
            notificationObject.put("title",title);
            notificationObject.put("body",body);

            jsonObject.put("notification",notificationObject);
            jsonObject.put("to",token);

            callApi(jsonObject);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void callApi(JSONObject jsonObject)
    {
        MediaType JSON = MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();

        String Url="https://fcm.googleapis.com/fcm/send";

        RequestBody requestBody =RequestBody.create(jsonObject.toString(),JSON);

        Request request = new Request.Builder()
                .url(Url)
                .post(requestBody)
                .header("Authorization","Bearer AAAA02ouWdI:APA91bEV1RYlj61zA42etNHJ6dHrQna910khyUKzLhkcERj04uFpLDzRo13V8WxNEiVWFzy_BkFnHbTWRGlgBohaA8xE1hSXNEeKsKNFabXKKl8kYXGtTteTKtnAkrqcmW3hFxXbHn2G")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            }
        });
    }

public void onActivityResult(int requestCode,int resultCode,Intent data) {

    super.onActivityResult(requestCode, resultCode, data);
    if(resultCode == RESULT_OK){
        if(requestCode == Select_Picture){
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                Glide.with(NGO_Event_Add_Request.this)
                        .load(selectedImageUri)
                        .into(eimage);
            }
        }
    }
}

    private boolean CheckValidation(){
        //txt_campaignname,txt_description,txt_organizer,txt_organizermno,txt_targetamount;

        if(txt_eventname.length() < 3){
            txt_eventname.setError("Atleast add 3 characters");
            return false;
        }

        if(txt_description.length() < 10){
            txt_description.setError("Atleast add 10 characters");
            return false;
        }

        if(txt_organizer.length() < 3){
            txt_organizer.setError("Atleast add 3 characters");
            return false;
        }

        if(txt_eventaddress.length() < 3){
            txt_eventaddress.setError("Atleast add 3 characters");
            return false;
        }

        if(txt_eventcity.length() < 3){
            txt_eventcity.setError("Atleast add 3 characters");
            return false;
        }

        if(txt_vtotal.length() == 0){
            txt_vtotal.setError("Field Required");
            return false;
        }

        if(txt_eventdate.length() == 0){
            txt_eventdate.setError("Field Required");
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