// Event_Notifications_Screen.java

package com.example.tathastu.NGO_Package.NGO_History;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Base64;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NGO_History_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private ConnectivityReceiver connectivityReceiver;
    private ProgressDialog progressDialog;
    MaterialTextView txt_history_transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_history_screen);

        // Assuming you have a RecyclerView with the id "userdata" in your layout
        RecyclerView recycle_history_transaction_Ngomodel = findViewById(R.id.recycle_history_transaction_NGOmodel);

        progressDialog = new ProgressDialog(new ContextThemeWrapper(NGO_History_Screen.this, R.style.CustomProgressDialog));
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        // Create a LinearLayoutManager for the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycle_history_transaction_Ngomodel.setLayoutManager(layoutManager);

        // Create a list of UserModel_Event_Notify objects (replace these with your actual data)
        List<NGOModel_History_payment> paymentList=fetchPaymentHistory();

        // Create an instance of UserAdapter_Event_Notify and set it to the RecyclerView
        NGOAdapter_History_payment adapter = new NGOAdapter_History_payment(paymentList);
        recycle_history_transaction_Ngomodel.setAdapter(adapter);

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        FloatingActionButton BTN_back=findViewById(R.id.BTN_back);
        txt_history_transaction=findViewById(R.id.txt_history_transaction);


        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i =new Intent(History_Screen.this, DashBoard_Screen.class);
//                startActivity(i);

                finish();Animatoo.INSTANCE.animateSlideRight(NGO_History_Screen.this);
            }
        });

        //TRANSACTION HISTORY
        txt_history_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRecyclerViewVisibility(recycle_history_transaction_Ngomodel);
            }
        });


    }


//--------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(NGO_History_Screen.this);
        finish();
    }

    // Function to toggle visibility of RecyclerView
    private void toggleRecyclerViewVisibility(RecyclerView recyclerView) {
        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
            txt_history_transaction.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_keyboard_arrow_down_24, 0);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            txt_history_transaction.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.round_keyboard_arrow_up_24, 0);
        }
    }

    private List<NGOModel_History_payment> fetchPaymentHistory() {
        progressDialog.show();
        List<NGOModel_History_payment> dataList = new ArrayList<>();


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
                                JSONObject jsonObject = new JSONObject(response.toString());
                                JSONArray paymentsArray = jsonObject.getJSONArray("items");

                                for (int i = 0; i < paymentsArray.length(); i++) {
                                    JSONObject payment = paymentsArray.getJSONObject(i);

                                    String amount = payment.getString("amount");
                                    String dateTime = payment.getString("created_at");
                                    String email = payment.getString("email");
                                    String contact = payment.getString("contact");

                                    String receivedFrom = "";
                                    String mobile="";

                                    if (payment.has("notes")) {
                                        JSONObject notesObject = payment.optJSONObject("notes");
                                        if (notesObject != null && notesObject.has("note1")) {
                                            receivedFrom = notesObject.getString("note1");
                                        }
                                        if (notesObject != null && notesObject.has("note2")) {
                                            mobile = notesObject.getString("note2");
                                        }
                                    }
                                    SharedPreferences sharedPreferences1 = getSharedPreferences("NGO",MODE_PRIVATE);
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    if(contact.equals(sharedPreferences1.getString("mobile",""))) {
                                        NGOModel_History_payment data = new NGOModel_History_payment(receivedFrom, amount, dateTime, email,mobile);
                                        dataList.add(data);
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
                // Handle error
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(NGO_History_Screen.this, "Failed To Fetch Payment History.", Toast.LENGTH_SHORT).show();
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

        return dataList;
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
