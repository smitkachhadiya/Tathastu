package com.example.tathastu.Admin_Package.Admin_user.Person_User_Data;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Base64;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.example.tathastu.User_Package.user_History.History_Screen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Admin_Person_Details extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private ConnectivityReceiver connectivityReceiver;

    MaterialTextView txt_userPersonal_data;
    private ProgressDialog progressDialog;
    MaterialTextView txt_userHistory_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_person_details);

        progressDialog = new ProgressDialog(new ContextThemeWrapper(Admin_Person_Details.this, R.style.CustomProgressDialog));
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        // Assuming you have a RecyclerView with the id "userdata" in your layout
        RecyclerView recycle_userPersonal_model = findViewById(R.id.recycle_userPersonal_model);
        RecyclerView recycle_userHistory_model = findViewById(R.id.recycle_userHistory_model);

        // Create a LinearLayoutManager for the RecyclerView
        LinearLayoutManager layoutManagerPersonal = new LinearLayoutManager(this);
        LinearLayoutManager layoutManagerHistory = new LinearLayoutManager(this);

        // Set layout managers for respective RecyclerViews
        recycle_userPersonal_model.setLayoutManager(layoutManagerPersonal);
        recycle_userHistory_model.setLayoutManager(layoutManagerHistory);

        // Create a list of UserModel_Event_Notify objects (replace these with your actual data)
        List<Admin_person_DataModel> userDataModelList = fetchUserPerData();

        // Create an instance of UserAdapter_Event_Notify and set it to the RecyclerView
        Admin_person_DataAdapter adapter = new Admin_person_DataAdapter(userDataModelList);
        recycle_userPersonal_model.setAdapter(adapter);

        List<Admin_person_History_DataModel> userHistoryDataModelList = fetchHisData();

        // Create an instance of UserAdapter_Event_Notify and set it to the RecyclerView
        Admin_person_History_DataAdapter historyadapter = new Admin_person_History_DataAdapter(userHistoryDataModelList);
        recycle_userHistory_model.setAdapter(historyadapter);

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        FloatingActionButton BTN_back=findViewById(R.id.BTN_back);
        txt_userPersonal_data=findViewById(R.id.txt_userPersonal_data);
        txt_userHistory_data=findViewById(R.id.txt_userHistory_data);


        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

                Animatoo.INSTANCE.animateSlideRight(Admin_Person_Details.this);
            }
        });
        //PERSONAL INFO
        txt_userPersonal_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRecyclerViewVisibility(recycle_userPersonal_model);
            }
        });

//TRANSACTION HISTORY
        txt_userHistory_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRecyclerHistoryViewVisibility(recycle_userHistory_model);
            }
        });


    }
//--------------------------------------------------------------------------------------------------



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }
    // Function to toggle visibility of RecyclerView
    private void toggleRecyclerViewVisibility(RecyclerView recyclerView) {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
            txt_userPersonal_data.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_keyboard_arrow_down_24, 0);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            txt_userPersonal_data.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_keyboard_arrow_up_24, 0);
        }
    }
    // Function to toggle visibility of RecyclerView
    private void toggleRecyclerHistoryViewVisibility(RecyclerView recyclerView) {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
            txt_userHistory_data.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_keyboard_arrow_down_24, 0);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            txt_userHistory_data.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_keyboard_arrow_up_24, 0);
        }
    }

    //dummy data FOR PERSONAL INFO
    private List<Admin_person_DataModel> fetchUserPerData() {
        // Replace this method with your actual data retrieval logic
        List<Admin_person_DataModel> dataPerList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.child("user").child(getIntent().getStringExtra("userId")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChildren() && snapshot != null)
                    {
                        Admin_person_DataModel data = snapshot.getValue(Admin_person_DataModel.class);
                        dataPerList.add(data);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return dataPerList;
    }

    //dummy data FOR HISTORY
    private List<Admin_person_History_DataModel> fetchHisData() {
        progressDialog.show();
        // Replace this method with your actual data retrieval logic
        List<Admin_person_History_DataModel> dataHisList = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.razorpay.com/v1/payments?count=100";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response

                        if(!response.isEmpty())
                        {
                            try{

                                dataHisList.clear();

                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray paymentsArray = jsonObject.getJSONArray("items");

                                for (int i = 0; i < paymentsArray.length(); i++) {
                                    JSONObject payment = paymentsArray.getJSONObject(i);

                                    String transactionId = payment.getString("id");
                                    String amount = payment.getString("amount");
                                    String status = payment.getString("status");
                                    String method = payment.getString("method");
                                    String sentTo = payment.getString("description");
                                    String dateTime = payment.getString("created_at");
                                    String email = payment.getString("email");

                                    String receivedFrom = "";
                                    String mobile="";
                                    String userId="";
                                    if (payment.has("notes")) {
                                        JSONObject notesObject = payment.optJSONObject("notes");
                                        if (notesObject != null && notesObject.has("note1")) {
                                            receivedFrom = notesObject.getString("note1");
                                        }
                                        if (notesObject != null && notesObject.has("note2")) {
                                            mobile = notesObject.getString("note2");
                                        }
                                        if (notesObject != null && notesObject.has("address")) {
                                            userId = notesObject.getString("address");
                                        }
                                    }
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    if(getIntent().getStringExtra("userId").equals(userId))
                                    {
                                        Admin_person_History_DataModel data = new Admin_person_History_DataModel(sentTo,method,amount,dateTime,status,transactionId,email);
                                        dataHisList.add(data);
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                // Handle error
                Toast.makeText(Admin_Person_Details.this, "Failed To Fetch Payment History.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                // Add Basic Authentication header
                String credentials = "rzp_test_iiWet5Chi79qWI" + ":" + "JbRNCuo2zjYJLCXdgbl7o8MZ";
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        // Add more dummy data as needed

        return dataHisList;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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
